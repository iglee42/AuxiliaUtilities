package fr.iglee42.auxiliautilities.blockentities;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.items.SingleUpgradeStackHandler;
import fr.iglee42.auxiliautilities.blocks.BlockCrusher;
import fr.iglee42.auxiliautilities.blocks.BlockFurnace;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUEnergyWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUTimedProgressWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.utils.Upgrade;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BEFurnace extends AUBlockEntity {

    private static final int BASE_ENERGY_PER_TICK = 40;
    private static final int MAX_ENERGY_TRANSFER_PER_TICK = 10000;

    private final EnergyStorage energyStorage = new EnergyStorage(100000, MAX_ENERGY_TRANSFER_PER_TICK) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    private final ItemStackHandler inventory = new ItemStackHandler(2);

    private final SingleUpgradeStackHandler upgrades = new SingleUpgradeStackHandler(Upgrade.SPEED);

    private RecipeHolder<SmeltingRecipe> currentRecipe;
    private int progress;

    public BEFurnace(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.FURNACE.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                AUBlockEntityTypes.FURNACE.get(),
                (be, ctx) -> be.inventory);

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                AUBlockEntityTypes.FURNACE.get(),
                (be, ctx) -> be.energyStorage);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void save(ValueOutput output, boolean forClient) {
        super.save(output, forClient);
        energyStorage.serialize(output.child("Energy"));
        inventory.serialize(output.child("Inventory"));
        output.putInt("Progress",progress);
        upgrades.serialize(output.child("Upgrades"));
        if (forClient)
            output.putBoolean("HasRecipe", currentRecipe != null);
    }

    @Override
    protected void load(ValueInput input) {
        super.load(input);
        energyStorage.deserialize(input.childOrEmpty("Energy"));
        inventory.deserialize(input.childOrEmpty("Inventory"));
        progress = input.getIntOr("Progress",0);
        upgrades.deserialize(input.childOrEmpty("Upgrades"));
        if (input.getBooleanOr("HasRecipe",false)) {
            /*currentRecipe = level.getRecipeManager().getAllRecipesFor(CrusherRecipe.Type.INSTANCE).stream()
                    .filter(r -> r.value().getIngredient().test(inventory.getStackInSlot(0)))
                    .findFirst().orElse(null);*/
        } else {
            currentRecipe = null;
        }
    }

    public boolean canWork() {
        if (currentRecipe == null || level == null)
            return false;
        ItemStack input = inventory.getStackInSlot(0);
        if (!currentRecipe.value().input().test(input))
            return false;
        ItemStack result = currentRecipe.value().assemble(new SingleRecipeInput(input.copy()),level.registryAccess());
        return inventory.insertItem(1, result, true).isEmpty();
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (currentRecipe == null) {
            Optional<RecipeHolder<SmeltingRecipe>> optional = level.recipeAccess()
                    .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(inventory.getStackInSlot(0)), level);
            if (optional.isPresent()) {
                currentRecipe = optional.get();
                progress = 0;
                return true;
            }
        }

        boolean isLit = state.getValue(BlockFurnace.LIT);
        if (isLit != canWork()) {
            level.setBlock(pos, state.setValue(BlockFurnace.LIT, canWork()), 3);
        }

        if (canWork()) {
            int maxProgress = calculateMaxProgress();
            if (progress >= maxProgress) {
                ItemStack result = currentRecipe.value().assemble(new SingleRecipeInput(inventory.getStackInSlot(0)),level.registryAccess());
                if (inventory.insertItem(1, result, false).isEmpty()) {
                    inventory.extractItem(0, 1, false);
                    this.progress = 0;
                    this.currentRecipe = null;
                }
            } else {
                int energyToDrain = calculateEnergyForProgressStep(progress, maxProgress);
                if (energyStorage.extractEnergy(energyToDrain, true) == energyToDrain) {
                    energyStorage.extractEnergy(energyToDrain, false);
                    this.progress++;
                }
            }
        } else {
            currentRecipe = null;
            progress = 0;
        }
        return true;
    }

    private int calculateMaxProgress() {
        if (currentRecipe == null)
            return 0;
        int cookingTime = currentRecipe.value().cookingTime();
        int recipeEnergy = cookingTime * BASE_ENERGY_PER_TICK;
        int baseProgress = cookingTime;
        int speedMultiplier = 1 + upgrades.getLevel(Upgrade.SPEED);
        int speedAdjustedProgress = Math.max(1, baseProgress / speedMultiplier);
        int minProgressForTransfer = Math.max(1,
                (int) Math.ceil((double) recipeEnergy / MAX_ENERGY_TRANSFER_PER_TICK));
        return Math.max(speedAdjustedProgress, minProgressForTransfer);
    }

    private int calculateEnergyForProgressStep(int currentProgress, int maxProgress) {
        if (currentRecipe == null || maxProgress <= 0)
            return 0;
        int totalEnergy = currentRecipe.value().cookingTime() * BASE_ENERGY_PER_TICK;
        int energyBefore = (int) ((long) totalEnergy * currentProgress / maxProgress);
        int energyAfter = (int) ((long) totalEnergy * (currentProgress + 1) / maxProgress);
        return energyAfter - energyBefore;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return calculateMaxProgress();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MenuFurnace(id, playerInv, this);
    }

    public class MenuFurnace extends AUBEMenu {

        public MenuFurnace(int id, Inventory playerInv, AUBlockEntity be) {
            super(AUMenus.FURNACE.get(), id, be);

            addTitle();
            AUTimedProgressWidget progressWidget = new AUTimedProgressWidget(74, 42) {
                @Override
                protected float getTime() {
                    return progress;
                }

                @Override
                protected float getMaxTime() {
                    return calculateMaxProgress();
                }
            };
            progressWidget.jeiCategory(ResourceLocation.withDefaultNamespace("smelting"));
            addWidget(progressWidget);
            addWidget(new SlotItemHandlerWidget(inventory, 0, 50, 42));
            addWidget(new SlotItemHandlerWidget(inventory, 1, 102, 42) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
            addWidget(upgrades.getSpeedSlot(10, 42));
            addWidget(new AUEnergyWidget(148, 8, energyStorage));
            cropAndAddPlayerSlots(playerInv);
            validate();
        }
    }
}


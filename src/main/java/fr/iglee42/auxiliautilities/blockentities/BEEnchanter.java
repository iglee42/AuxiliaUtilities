package fr.iglee42.auxiliautilities.blockentities;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.items.SingleUpgradeStackHandler;
import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUEnergyWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUTextWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUTimedProgressWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import fr.iglee42.auxiliautilities.recipes.inputs.EnchanterRecipeInput;
import fr.iglee42.auxiliautilities.utils.Upgrade;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BEEnchanter extends AUBlockEntity {

    private static final int BASE_ENERGY_PER_TICK = 40;
    private static final int MAX_ENERGY_TRANSFER_PER_TICK = 10000;

    private final EnergyStorage energyStorage = new EnergyStorage(200000, MAX_ENERGY_TRANSFER_PER_TICK) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == 1 && !stack.is(Tags.Items.GEMS_LAPIS))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }
    };

    private final SingleUpgradeStackHandler upgrades = new SingleUpgradeStackHandler(Upgrade.SPEED);

    private RecipeHolder<EnchanterRecipe> currentRecipe;
    private int progress;

    public BEEnchanter(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.ENCHANTER.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                AUBlockEntityTypes.ENCHANTER.get(),
                (be, ctx) -> be.inventory);

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                AUBlockEntityTypes.ENCHANTER.get(),
                (be, ctx) -> be.energyStorage);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    protected void save(CompoundTag tag, Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        tag.put("Energy", energyStorage.serializeNBT(registries));
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.put("Upgrades", upgrades.serializeNBT(registries));
        if (forClient)
            tag.putBoolean("HasRecipe", currentRecipe != null);
    }

    @Override
    protected void load(CompoundTag tag, Provider registries) {
        super.load(tag, registries);
        energyStorage.deserializeNBT(registries, tag.get("Energy"));
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        progress = tag.getInt("Progress");
        upgrades.deserializeNBT(registries, tag.getCompound("Upgrades"));
        if (tag.contains("HasRecipe") && tag.getBoolean("HasRecipe")) {
            currentRecipe = level.getRecipeManager().getAllRecipesFor(EnchanterRecipe.Type.INSTANCE).stream()
                    .filter(r -> r.value().getIngredient().test(inventory.getStackInSlot(0))
                            && r.value().getLapisIngredient().test(inventory.getStackInSlot(1)))
                    .findFirst().orElse(null);
        } else {
            currentRecipe = null;
        }
    }

    public boolean canWork() {
        if (currentRecipe == null || level == null)
            return false;
        return getEnchantmentPowerAround() >= 15 && currentRecipe.value().getIngredient().test(inventory.getStackInSlot(0))
                && currentRecipe.value().getLapisIngredient().test(inventory.getStackInSlot(1))
                && inventory.insertItem(2, currentRecipe.value().getResultItem(level.registryAccess()), true).isEmpty();
    }

    public float getEnchantmentPowerAround(){
        float enchantmentLevel = 0;
        for (BlockPos pos : EnchantingTableBlock.BOOKSHELF_OFFSETS){
            if (EnchantingTableBlock.isValidBookShelf(level,getBlockPos(),pos)){
                enchantmentLevel+= level.getBlockState(getBlockPos().offset(pos)).getEnchantPowerBonus(level,getBlockPos().offset(pos));
            }
        }
        return enchantmentLevel;
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (currentRecipe == null) {
            Optional<RecipeHolder<EnchanterRecipe>> optional = level.getRecipeManager()
                    .getRecipeFor(EnchanterRecipe.Type.INSTANCE, new EnchanterRecipeInput(this), level);
            if (optional.isPresent()) {
                currentRecipe = optional.get();
                progress = 0;
                return true;
            }

        }

        if (getEnchantmentPowerAround() < 15) {
            currentRecipe = null;
            progress = 0;
            return true;
        }
        if (canWork()) {
            if (progress >= calculateMaxProgress()) {
                if (inventory.insertItem(2, currentRecipe.value().getResultItem(level.registryAccess()), false)
                        .isEmpty()) {
                    inventory.extractItem(0, currentRecipe.value().getIngredient().count(), false);
                    inventory.extractItem(1, currentRecipe.value().getLapisIngredient().count(), false);
                    this.progress = 0;
                    this.currentRecipe = null;
                }
            } else {
                int maxProgress = calculateMaxProgress();
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

    @Override
    protected void clientTick(ClientLevel level, BlockPos pos, BlockState state) {
        super.clientTick(level, pos, state);
        if (canWork()){
            RandomSource random = RandomSource.create();
            for (BlockPos blockpos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
                if (random.nextInt(16) == 0 && EnchantingTableBlock.isValidBookShelf(level, pos, blockpos)) {
                    level.addParticle(
                            ParticleTypes.ENCHANT,
                            (double)pos.getX() + 0.5,
                            (double)pos.getY() + 2.0,
                            (double)pos.getZ() + 0.5,
                            (double)((float)blockpos.getX() + random.nextFloat()) - 0.5,
                            (double)((float)blockpos.getY() - random.nextFloat() - 1.0F),
                            (double)((float)blockpos.getZ() + random.nextFloat()) - 0.5
                    );
                }
            }
        }
    }

    private int calculateMaxProgress() {
        if (currentRecipe == null)
            return 0;
        int recipeEnergy = currentRecipe.value().getEnergy();
        int baseProgress = Math.max(1, recipeEnergy / BASE_ENERGY_PER_TICK);
        int speedMultiplier = 1 + upgrades.getLevel(Upgrade.SPEED);
        int speedAdjustedProgress = Math.max(1, baseProgress / speedMultiplier);
        int minProgressForTransfer = Math.max(1,
                (int) Math.ceil((double) recipeEnergy / MAX_ENERGY_TRANSFER_PER_TICK));
        // Prevent a single progress step from requiring more RF than can be extracted in one tick.
        return Math.max(speedAdjustedProgress, minProgressForTransfer);
    }

    private int calculateEnergyForProgressStep(int currentProgress, int maxProgress) {
        if (currentRecipe == null || maxProgress <= 0)
            return 0;
        int totalEnergy = currentRecipe.value().getEnergy();
        int energyBefore = (int) ((long) totalEnergy * currentProgress / maxProgress);
        int energyAfter = (int) ((long) totalEnergy * (currentProgress + 1) / maxProgress);
        return energyAfter - energyBefore;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MenuEnchanter(id, playerInv, this);
    }

    public class MenuEnchanter extends AUBEMenu {

        public MenuEnchanter(int id, Inventory playerInv, AUBlockEntity be) {
            super(AUMenus.ENCHANTER.get(), id, be);

            addTitle();
            AUTimedProgressWidget progressWidget = new AUTimedProgressWidget(74,42) {
                @Override
                protected float getTime() {
                    return progress;
                }

                @Override
                protected float getMaxTime() {
                    if (getEnchantmentPowerAround() < 15) return -1;
                    return calculateMaxProgress();
                }

                @Override
                public List<Component> getErrorMessages() {
                    if (getEnchantmentPowerAround() < 15)
                        return List.of(AULang.ENCHANTER_NEEDS_BOOKSHELVES.get());
                    return super.getErrorMessages();
                }
            };
            progressWidget.jeiCategory(AuxiliaUtilities.id(EnchanterRecipe.Type.ID));
            addWidget(progressWidget);
            addWidget(new SlotItemHandlerWidget(inventory,0, 30, 42));
            addWidget(new SlotItemHandlerWidget(inventory,1, 50, 42));
            addWidget(new SlotItemHandlerWidget(inventory,2, 102, 42){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
            addWidget(upgrades.getSpeedSlot(10,42));
            addWidget(new AUEnergyWidget(148, 8,energyStorage));
            cropAndAddPlayerSlots(playerInv);
            validate();
        }
    }

}

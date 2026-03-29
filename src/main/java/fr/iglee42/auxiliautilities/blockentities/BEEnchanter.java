package fr.iglee42.auxiliautilities.blockentities;

import java.util.Optional;

import fr.iglee42.auxiliautilities.blockentities.items.SingleUpgradeStackHandler;
import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import fr.iglee42.auxiliautilities.recipes.inputs.EnchanterRecipeInput;
import fr.iglee42.auxiliautilities.utils.Upgrade;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BEEnchanter extends AUBlockEntity {

    private final EnergyStorage energyStorage = new EnergyStorage(100000, 1000) {
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
        return currentRecipe.value().getIngredient().test(inventory.getStackInSlot(0))
                && currentRecipe.value().getLapisIngredient().test(inventory.getStackInSlot(1))
                && inventory.insertItem(2, currentRecipe.value().getResultItem(level.registryAccess()), true).isEmpty();
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
        if (canWork()) {
            if (progress >= calculateMaxProgress()) {
                if (inventory.insertItem(2, currentRecipe.value().getResultItem(level.registryAccess()), false)
                        .isEmpty()) {
                    inventory.extractItem(0, 1, false);
                    inventory.extractItem(1, currentRecipe.value().getLapisIngredient().count(), false);
                    this.progress = 0;
                    this.currentRecipe = null;
                }
            } else {
                int energyToDrain = Math.min(4 * (1 + upgrades.getLevel(Upgrade.SPEED)),
                        calculateMaxProgress() - progress);
                this.progress += energyStorage.extractEnergy(energyToDrain, false);
            }
            return true;
        } else {
            currentRecipe = null;
            progress = 0;
            return true;
        }
    }

    private int calculateMaxProgress() {
        if (currentRecipe == null)
            return 0;
        return currentRecipe.value().getEnergy();
    }

}

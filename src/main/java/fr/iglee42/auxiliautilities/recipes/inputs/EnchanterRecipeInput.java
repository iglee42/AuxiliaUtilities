package fr.iglee42.auxiliautilities.recipes.inputs;

import java.util.List;

import fr.iglee42.auxiliautilities.blockentities.BEEnchanter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class EnchanterRecipeInput implements RecipeInput {

    private final List<ItemStack> stacks;
    private final int energy;

    public EnchanterRecipeInput(BEEnchanter blockEntity) {
        this.stacks = List.of(blockEntity.getInventory().getStackInSlot(0),
                blockEntity.getInventory().getStackInSlot(1));
        this.energy = blockEntity.getEnergyStorage().getEnergyStored();
    }

    @Override
    public ItemStack getItem(int index) {
        if (index >= stacks.size()) {
            throw new IllegalArgumentException("No item for index " + index);
        } else {
            return this.stacks.get(index);
        }
    }

    @Override
    public int size() {
        return stacks.size();
    }

    public int getEnergy() {
        return energy;
    }

}

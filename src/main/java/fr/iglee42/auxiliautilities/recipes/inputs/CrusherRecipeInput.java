package fr.iglee42.auxiliautilities.recipes.inputs;

import fr.iglee42.auxiliautilities.blockentities.BECrusher;
import fr.iglee42.auxiliautilities.blockentities.BEEnchanter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleItemRecipe;

import java.util.List;

public class CrusherRecipeInput implements RecipeInput {

    private final ItemStack stacks;
    private final int energy;

    public CrusherRecipeInput(BECrusher blockEntity) {
        this.stacks = blockEntity.getInventory().getStackInSlot(0).copy();
        this.energy = blockEntity.getEnergyStorage().getEnergyStored();
    }

    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        } else {
            return stacks;
        }
    }

    @Override
    public int size() {
        return 1;
    }

    public int getEnergy() {
        return energy;
    }

}

package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public class BEGenCulinary extends AUGeneratorBlockEntity{
    public BEGenCulinary(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.CULINARY_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        return (int) (getEnergyRate(stack) * 10);
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return (int) getEnergyRate(item);
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return stack.has(DataComponents.FOOD);
    }

    public static int nerfLevels(double energy, double maxLevel) {
        if (energy < maxLevel)
            return (int) Math.ceil(energy);

        double divisor = 1.0;
        double totalEnergy = 0.0;

        while (energy > maxLevel) {
            totalEnergy += maxLevel / divisor;
            energy -= maxLevel;
            divisor++;
        }

        totalEnergy += energy / divisor;

        return (int) Math.ceil(totalEnergy);
    }

    public static float getEnergyRate(ItemStack stack) {

        FoodProperties food = stack.getFoodProperties(null);

        if (food == null)
            return 0;

        int healAmount = food.nutrition();

        return nerfLevels(
                healAmount * 8D,
                64.0D
        );
    }
}

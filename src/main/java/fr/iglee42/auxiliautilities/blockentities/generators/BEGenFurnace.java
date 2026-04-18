package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public class BEGenFurnace extends AUGeneratorBlockEntity{
    public BEGenFurnace(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.FURNACE_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        if (level == null) return 0;
        return stack.getBurnTime(null,level.fuelValues()) / 10;
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return 40;
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        if (level == null) return false;
        return stack.getBurnTime(null,level.fuelValues())>0;
    }
}

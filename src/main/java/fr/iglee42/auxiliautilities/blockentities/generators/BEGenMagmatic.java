package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class BEGenMagmatic extends AUGeneratorBlockEntity{
    public BEGenMagmatic(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.MAGMATIC_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredFluidAmount() {
        return 50;
    }

    @Override
    protected int getProgressPerFluid(FluidStack stack) {
        return 125;
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return 40;
    }

    @Override
    protected boolean isFluidValid(FluidStack stack) {
        return stack.is(Tags.Fluids.LAVA);
    }
}

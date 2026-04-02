package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class BEGenHeatedRedstone extends AUGeneratorBlockEntity{
    public BEGenHeatedRedstone(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected int requiredFluidAmount() {
        return 50;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        return 125;
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return 160;
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return stack.is(Tags.Items.DUSTS_REDSTONE);
    }

    @Override
    protected boolean isFluidValid(FluidStack stack) {
        return stack.is(Tags.Fluids.LAVA);
    }
}

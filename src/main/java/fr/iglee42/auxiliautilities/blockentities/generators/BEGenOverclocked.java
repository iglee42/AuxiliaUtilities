package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public class BEGenOverclocked extends AUGeneratorBlockEntity{
    public BEGenOverclocked(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.OVERCLOCKED_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        return 10;
    }

    @Override
    protected int getEnergyPerProgress(ItemStack stack, FluidStack fluid) {
        active = true;
        return stack.getBurnTime(null);
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return stack.getBurnTime(null)>0;
    }
}

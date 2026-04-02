package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

public class BEGenSurvival extends AUGeneratorBlockEntity{
    public BEGenSurvival(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.SURVIVAL_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        return stack.getBurnTime(null) * 10;
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return 5;
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return stack.getBurnTime(null)>0;
    }
}

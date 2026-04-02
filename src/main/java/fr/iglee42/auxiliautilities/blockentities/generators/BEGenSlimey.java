package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.world.item.Items;

public class BEGenSlimey extends AUGeneratorBlockEntity{
    public BEGenSlimey(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.SLIMEY_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 2;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        return slot == 1 ? 480 : 0;
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return 400;
    }

    @Override
    protected int getSlotLimit(int slot) {
        return slot == 1 ? 1 : super.getSlotLimit(slot);
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return slot == 1 ? stack.is(Tags.Items.BUCKETS_MILK) : stack.is(Tags.Items.SLIME_BALLS);
    }

    @Override
    protected ItemStack getReturnItem(int slot, ItemStack stack) {
        return slot == 1 ? new ItemStack(Items.BUCKET) : ItemStack.EMPTY;
    }

    @Override
    protected int getRequiredItemForSlot(int slot) {
        return slot == 0 ? 4 : super.getRequiredItemForSlot(slot);
    }
}

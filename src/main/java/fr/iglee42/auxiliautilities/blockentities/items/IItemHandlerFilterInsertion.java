package fr.iglee42.auxiliautilities.blockentities.items;

import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class IItemHandlerFilterInsertion<T extends IItemHandlerModifiable> implements IItemHandlerModifiable {

    public final T original;

    public IItemHandlerFilterInsertion(T original) {
        this.original = original;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.original.setStackInSlot(slot, stack);
    }

    public abstract boolean isValid(@Nonnull ItemStack stack);

    @Override
    public int getSlots() {
        return this.original.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.original.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (InventoryHelper.isStackEmpty(stack) || !isValid(stack))
            return stack;
        return this.original.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extract = this.original.extractItem(slot, amount, simulate);
        if (InventoryHelper.isStackEmpty(extract) || !isValid(extract))
            return ItemStack.EMPTY;
        return extract;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.original.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false;
    }

    public IItemHandler getGUIVariant(){
        return new IItemHandlerFilterInsertion<T>(this.original) {

            @Override
            public boolean isValid(@NotNull ItemStack stack) {
                return IItemHandlerFilterInsertion.this.isValid(stack);
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return this.original.extractItem(slot, amount, simulate);
            }
        };
    }
}

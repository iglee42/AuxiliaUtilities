package fr.iglee42.auxiliautilities.blockentities.items;

import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SingleItemStackHandler extends ItemStackHandler {

    public SingleItemStackHandler() {
        super(1);
    }

    public SingleItemStackHandler(ItemStack stack) {
        super(NonNullList.of(stack));
    }

    public ItemStack getStack() {
        return getStackInSlot(0);
    }
}

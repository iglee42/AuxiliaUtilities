package fr.iglee42.auxiliautilities.utils;

import net.minecraft.world.item.ItemStack;

public interface IItemFilter {

    boolean isItemFilter(ItemStack filterStack);

    boolean matchesFilter(ItemStack stack, ItemStack filterStack);


}

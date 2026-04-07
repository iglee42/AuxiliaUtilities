package fr.iglee42.auxiliautilities.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IFluidFilter {

    boolean isFluidFilter(ItemStack filterStack);

    boolean matchesFilter(FluidStack stack, ItemStack filterStack);


}

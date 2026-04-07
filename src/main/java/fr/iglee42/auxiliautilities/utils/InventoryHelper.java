package fr.iglee42.auxiliautilities.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

public class InventoryHelper {

    public static boolean isStackNotEmpty(@Nullable ItemStack stack){
        return stack != null && !stack.isEmpty();
    }

    public static boolean isStackEmpty(@Nullable ItemStack stack){
        return stack == null || stack.isEmpty();
    }

}

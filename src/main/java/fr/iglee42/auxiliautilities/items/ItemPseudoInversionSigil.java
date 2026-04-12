package fr.iglee42.auxiliautilities.items;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemPseudoInversionSigil extends AUFoilItem{
    public ItemPseudoInversionSigil(Properties props) {
        super(props.durability(256));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.copy();
    }
}

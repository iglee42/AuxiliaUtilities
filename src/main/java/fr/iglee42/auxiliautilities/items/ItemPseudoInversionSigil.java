package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.items.api.AUFoilItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemPseudoInversionSigil extends AUFoilItem {
    public ItemPseudoInversionSigil(Properties props) {
        super(props.durability(256));
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack stack) {
        return stack.copy();
    }
}

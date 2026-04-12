package fr.iglee42.auxiliautilities.items;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemDivisionSigil extends AUFoilItem{
    public ItemDivisionSigil(Properties props) {
        super(props.durability(256));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        stack.setDamageValue(stack.getDamageValue() + 1);
        if (stack.getDamageValue() == stack.getMaxDamage()){
            return new ItemStack(AUItems.UNACTIVATED_DIVISION_SIGIL.get());
        }
        return stack;
    }
}

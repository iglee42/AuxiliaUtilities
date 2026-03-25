package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.utils.Upgrade;
import net.minecraft.world.item.ItemStack;

public class ItemSpeedUpgrade extends ItemUpgrade{
    public ItemSpeedUpgrade(Properties props,int maxStackSize) {
        super(props.stacksTo(maxStackSize), Upgrade.SPEED);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.getMaxStackSize() > 4 || super.isFoil(stack);
    }
}

package fr.iglee42.auxiliautilities.items.api;

import net.minecraft.world.item.ItemStack;

public class AUFoilItem extends AUItem{
    public AUFoilItem(Properties props) {
        super(props);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }
}

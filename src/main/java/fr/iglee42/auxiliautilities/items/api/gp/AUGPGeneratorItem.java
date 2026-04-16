package fr.iglee42.auxiliautilities.items.api.gp;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AUGPGeneratorItem extends AUGPItem{
    public AUGPGeneratorItem(Properties props) {
        super(props);
    }

    @Override
    protected final int getGPConsumption(ItemStack stack, Player player) {
        return 0;
    }
}

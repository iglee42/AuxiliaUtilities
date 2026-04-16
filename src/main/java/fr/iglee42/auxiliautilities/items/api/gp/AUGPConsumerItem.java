package fr.iglee42.auxiliautilities.items.api.gp;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AUGPConsumerItem extends AUGPItem{
    public AUGPConsumerItem(Properties props) {
        super(props);
    }

    @Override
    protected final int getGPGeneration(ItemStack stack, Player player) {
        return 0;
    }
}

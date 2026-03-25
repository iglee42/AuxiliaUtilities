package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.utils.Upgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemUpgrade extends AUItem implements UpgradeProvider{
    private final Upgrade upgrade;

    public ItemUpgrade(Properties props, Upgrade upgrade) {
        super(props);
        this.upgrade = upgrade;
    }

    @Override
    public Upgrade getUpgrade(ItemStack stack) {
        return upgrade;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext p_339594_, List<Component> tooltips, TooltipFlag p_41424_) {
        Upgrade.addTooltip(tooltips, stack,this,stack.getMaxStackSize());
        super.appendHoverText(stack, p_339594_, tooltips, p_41424_);
    }
}

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
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        return Upgrade.getTooltips(stack,this,stack.getMaxStackSize());
    }

    @Override
    public List<Component> getAdvancedTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        return List.of(upgrade.getDescription());
    }
}

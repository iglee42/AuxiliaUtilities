package fr.iglee42.auxiliautilities.items.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class AUItem extends Item implements AUItemBase {
    public AUItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltips, TooltipFlag flag) {
        addTooltips(stack, tooltips, ctx, flag);
        super.appendHoverText(stack, ctx, tooltips, flag);
    }

}

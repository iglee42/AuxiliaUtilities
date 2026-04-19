package fr.iglee42.auxiliautilities.items.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import java.util.function.Consumer;

public class AUItem extends Item implements AUItemBase {
    public AUItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay p_399753_, Consumer<Component> acceptor, TooltipFlag flag) {
        addTooltips(stack, acceptor, ctx, flag);
        super.appendHoverText(stack, ctx, p_399753_, acceptor, flag);
    }
}

package fr.iglee42.auxiliautilities.blocks.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class AUBlock extends Block implements AUBlockBase {
    public AUBlock(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        addTooltips(stack, tooltips, context, flag);
        super.appendHoverText(stack, context, tooltips, flag);
    }
}

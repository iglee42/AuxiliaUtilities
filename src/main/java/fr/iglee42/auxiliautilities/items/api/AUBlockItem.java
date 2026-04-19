package fr.iglee42.auxiliautilities.items.api;

import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import fr.iglee42.auxiliautilities.utils.AUTooltipProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class AUBlockItem extends BlockItem implements AUItemBase {
    public AUBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {
        if (getBlock() instanceof AUBlockBase blockBase)
            blockBase.addToCreativeTab(acceptor);
         else
            acceptor.accept(new ItemStack(this));
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return getBlock() instanceof AUBlockBase base ? base.getItemColor(stack,tintIndex) : AUItemBase.super.getColor(stack, tintIndex);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, TooltipDisplay display, Consumer<Component> acceptor, TooltipFlag flag) {
        if (getBlock() instanceof AUTooltipProvider prov)
            prov.addTooltips(stack,acceptor,ctx,flag);
        super.appendHoverText(stack, ctx, display, acceptor, flag);
    }
}

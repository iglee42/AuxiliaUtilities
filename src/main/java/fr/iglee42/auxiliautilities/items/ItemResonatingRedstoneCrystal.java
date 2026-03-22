package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.ClientGPManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemResonatingRedstoneCrystal extends AUItem{
    public ItemResonatingRedstoneCrystal(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext ctx, List<Component> tooltips, TooltipFlag p_41424_) {
        if (ctx != null && ctx.level() != null && ctx.level().isClientSide){
            tooltips.add(AULang.GP_TOOLTIP.get(ClientGPManager.getTotalConsumption(),ClientGPManager.getTotalGeneration()).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(p_41421_, ctx, tooltips, p_41424_);
    }
}

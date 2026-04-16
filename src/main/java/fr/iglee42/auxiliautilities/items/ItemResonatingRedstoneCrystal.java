package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.ClientGPManager;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemResonatingRedstoneCrystal extends AUItem {
    public ItemResonatingRedstoneCrystal(Properties props) {
        super(props);
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        if (ctx.level() == null || !ctx.level().isClientSide)
            return List.of();
        return List.of(AULang.GP_TOOLTIP.get(ClientGPManager.getTotalConsumption(),ClientGPManager.getTotalGeneration()).withStyle(ChatFormatting.GRAY));
    }
}

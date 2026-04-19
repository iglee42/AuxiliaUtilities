package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.AUKeymappings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public interface AUTooltipProvider {

    @OnlyIn(Dist.CLIENT)
    default void addTooltips(ItemStack stack, Consumer<Component> acceptor, Item.TooltipContext ctx, TooltipFlag flag){
       getTooltips(stack, ctx, flag).forEach(acceptor);
        List<Component> advanced = getAdvancedTooltips(stack, ctx, flag);
        if (!advanced.isEmpty()){
            if (CommonKeysHandler.isKeyPressed(AUKeymappings.SHOW_DESCRIPTION))
                advanced.forEach(acceptor);
            else
                acceptor.accept(AULang.HOLD_DESCRIPTION_TOOLTIP.get(AUKeymappings.SHOW_DESCRIPTION.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        }

        List<Component> storage = getStorageTooltips(stack, ctx, flag);
        if (!storage.isEmpty()){
            if (CommonKeysHandler.isKeyPressed(AUKeymappings.SHOW_DETAILS))
                storage.forEach(acceptor);
            else
                acceptor.accept(AULang.HOLD_DETAILS_TOOLTIP.get(AUKeymappings.SHOW_DETAILS.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        }
    }

    @OnlyIn(Dist.CLIENT)
    default Component getTooltip(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag){
        return Component.empty();
    }

    @OnlyIn(Dist.CLIENT)
    default List<Component> getTooltips(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag){
        if (!Objects.equals(getTooltip(stack, ctx, flag), Component.empty()))
            return List.of(getTooltip(stack, ctx, flag));
        return List.of();
    }

    @OnlyIn(Dist.CLIENT)
    default List<Component> getAdvancedTooltips(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag){
        return AULang.getTooltips(stack);
    }

    @OnlyIn(Dist.CLIENT)
    default List<Component> getStorageTooltips(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag){
        return List.of();
    }

    @OnlyIn(Dist.CLIENT)
    default String formatInt(int i){
        return NumberFormat.getIntegerInstance(Minecraft.getInstance().getLocale()).format(i);
    }

    @OnlyIn(Dist.CLIENT)
    default String formatFloat(float i){
        return NumberFormat.getNumberInstance(Minecraft.getInstance().getLocale()).format(i);
    }

    @OnlyIn(Dist.CLIENT)
    default String formatPercent(int i){
        return NumberFormat.getPercentInstance(Minecraft.getInstance().getLocale()).format(i);
    }

    @OnlyIn(Dist.CLIENT)
    default String formatDuration(int i){
        return AULang.formatDurationSeconds(i,true);
    }

}

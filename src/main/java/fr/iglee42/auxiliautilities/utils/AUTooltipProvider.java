package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.AULang;
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

public interface AUTooltipProvider {

    @OnlyIn(Dist.CLIENT)
    default void addTooltips(ItemStack stack,List<Component> tooltips, Item.TooltipContext ctx, TooltipFlag flag){
        tooltips.addAll(getTooltips(stack, ctx, flag));
        List<Component> advanced = getAdvancedTooltips(stack, ctx, flag);
        if (!advanced.isEmpty()){
            if (flag.hasShiftDown())
                tooltips.addAll(advanced);
            else
                tooltips.add(AULang.HOLD_SHIFT_TOOLTIP.get());
        }

        List<Component> storage = getStorageTooltips(stack, ctx, flag);
        if (!storage.isEmpty()){
            if (flag.hasControlDown())
                tooltips.addAll(storage);
            else
                tooltips.add(AULang.HOLD_CTRL_TOOLTIP.get());
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

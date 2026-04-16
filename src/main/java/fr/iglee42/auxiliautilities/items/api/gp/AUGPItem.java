package fr.iglee42.auxiliautilities.items.api.gp;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.ClientGPManager;
import fr.iglee42.auxiliautilities.gp.GPCapabilities;
import fr.iglee42.auxiliautilities.gp.GPItemHolder;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class AUGPItem extends AUItem {
    public AUGPItem(Properties props) {
        super(props);
    }

    public static GPItemHolder getGPHolder(ItemStack stack,Player player){
        return new AUGPItemHolder(stack,player);
    }

    protected abstract int getGPGeneration(ItemStack stack, Player player);
    protected abstract int getGPConsumption(ItemStack stack, Player player);
    protected void onRemove(ItemStack stack, Player player) {}

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        if (ctx.level() == null || !ctx.level().isClientSide)
            return List.of();
        return List.of(AULang.GP_TOOLTIP.get(ClientGPManager.getTotalConsumption(),ClientGPManager.getTotalGeneration()).withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    protected final UUID getNetworkId(ItemStack stack,Player player){
        GPItemHolder holder = stack.getCapability(GPCapabilities.ITEM,player);
        if (holder == null) return null;
        return holder.getNetworkId();
    }

    public static class AUGPItemHolder extends GPItemHolder {

        public AUGPItemHolder(ItemStack stack, Player player) {
            super(stack, player);
        }


        @Override
        protected int getGPConsumption(ItemStack stack, Player player) {
            return stack.getItem() instanceof AUGPItem item ? item.getGPConsumption(stack,player) : 0;
        }

        @Override
        protected int getGPGeneration(ItemStack stack, Player player) {
            return stack.getItem() instanceof AUGPItem item ? item.getGPGeneration(stack,player) : 0;
        }

        @Override
        protected void onRemoved(ItemStack stack, Player player) {
            if (stack.getItem() instanceof AUGPItem item)
                item.onRemove(stack,player);
        }
    }
}

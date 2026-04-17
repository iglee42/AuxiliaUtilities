package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import fr.iglee42.auxiliautilities.items.api.gp.AUGPConsumerItem;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.network.PlayerMoveVerticallyPacket;
import fr.iglee42.auxiliautilities.network.UpdatePlayerRemainingFlyingTicksPacket;
import fr.iglee42.auxiliautilities.utils.CommonKeysHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemChickenRing extends AUGPConsumerItem {

    public ItemChickenRing(Properties props) {
        super(props.component(AUDataComponents.REMAINING_FLYING_TICKS,ItemFlyingSquidRing.MAX_FLYING_TICKS));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean offhand) {
        tick(stack,entity);
    }

    public void tick(ItemStack stack, Entity entity) {
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;
        boolean enoughPower = GPNetworkManager.INSTANCE.hasEnoughPower(getNetworkId(stack, player));

        if (enoughPower && CommonKeysHandler.isKeyPressed(player,"key.jump")){
            int remainingFlyingTicks = stack.getOrDefault(AUDataComponents.REMAINING_FLYING_TICKS,ItemFlyingSquidRing.MAX_FLYING_TICKS);
            if (remainingFlyingTicks > 0 && player.getDeltaMovement().y() < 0.0) {
                double motion = Math.min(player.getDeltaMovement().y * 0.5, player.getDeltaMovement().y + 0.1);
                player.resetFallDistance();
                if (player instanceof ServerPlayer) {
                    ((ServerPlayer) player).connection.aboveGroundTickCount = 0;
                }
                player.setDeltaMovement(player.getDeltaMovement().x(), motion, player.getDeltaMovement().z());
                PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerMoveVerticallyPacket(motion));
                stack.set(AUDataComponents.REMAINING_FLYING_TICKS, remainingFlyingTicks - 1);
                PacketDistributor.sendToPlayer((ServerPlayer) player,new UpdatePlayerRemainingFlyingTicksPacket(stack.get(AUDataComponents.REMAINING_FLYING_TICKS)));
            }
        } else {
            int remainingFlyingTicks = stack.getOrDefault(AUDataComponents.REMAINING_FLYING_TICKS,ItemFlyingSquidRing.MAX_FLYING_TICKS);
            if (remainingFlyingTicks < ItemFlyingSquidRing.MAX_FLYING_TICKS) {
                stack.set(AUDataComponents.REMAINING_FLYING_TICKS, remainingFlyingTicks + 1);
                PacketDistributor.sendToPlayer((ServerPlayer) player,new UpdatePlayerRemainingFlyingTicksPacket(stack.get(AUDataComponents.REMAINING_FLYING_TICKS)));
            }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(AUDataComponents.REMAINING_FLYING_TICKS,ItemFlyingSquidRing.MAX_FLYING_TICKS) < ItemFlyingSquidRing.MAX_FLYING_TICKS;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int remainingFlyingTicks = stack.getOrDefault(AUDataComponents.REMAINING_FLYING_TICKS,ItemFlyingSquidRing.MAX_FLYING_TICKS);
        float f = Math.max(0.0F, (float)remainingFlyingTicks / ItemFlyingSquidRing.MAX_FLYING_TICKS);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int remainingFlyingTicks = stack.getOrDefault(AUDataComponents.REMAINING_FLYING_TICKS,ItemFlyingSquidRing.MAX_FLYING_TICKS);
        return Math.round((float)remainingFlyingTicks * 13.0F / (float)ItemFlyingSquidRing.MAX_FLYING_TICKS);
    }

    @Override
    protected int getGPConsumption(ItemStack stack, Player player) {
        return AUConfig.CHICKEN_CONSUMPTION.get();
    }

}

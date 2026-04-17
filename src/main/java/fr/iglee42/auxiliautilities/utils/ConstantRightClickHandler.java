package fr.iglee42.auxiliautilities.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class ConstantRightClickHandler {

    private static BlockPos targetPos;
    private static BlockState targetState;

    public static void start(Level level, BlockPos pos) {
        if (!level.isClientSide) return;

        targetPos = pos;
        targetState = level.getBlockState(pos);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {

        if (targetPos == null) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        var level = mc.level;

        if (player == null || level == null) {
            stop();
            return;
        }

        HitResult hit = mc.hitResult;

        if (!(hit instanceof BlockHitResult blockHit)) {
            stop();
            return;
        }

        // conditions pour continuer
        if (!targetPos.equals(blockHit.getBlockPos())
                || level.getBlockState(targetPos) != targetState
                || player.isShiftKeyDown()
                || player.isUsingItem()) {

            stop();
            return;
        }

        // simulate right click
        mc.gameMode.useItemOn(player, InteractionHand.MAIN_HAND, blockHit);
    }

    private static void stop() {
        targetPos = null;
        targetState = null;
    }
}
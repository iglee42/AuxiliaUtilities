package fr.iglee42.auxiliautilities.client;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPBlockEntity;
import fr.iglee42.auxiliautilities.items.ItemFlyingSquidRing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;

public class ClientFlyingRingManager {
    public static int clientRemainingFlyingTicks = ItemFlyingSquidRing.MAX_FLYING_TICKS;

    public static void setClientRemainingFlyingTicks(int clientRemainingFlyingTicks) {
        ClientFlyingRingManager.clientRemainingFlyingTicks = Mth.clamp(clientRemainingFlyingTicks,0,ItemFlyingSquidRing.MAX_FLYING_TICKS);
    }

    public static final LayeredDraw.Layer HUD = (gui, tracker)->{
        if (Minecraft.getInstance().level == null) return;
        if (clientRemainingFlyingTicks == ItemFlyingSquidRing.MAX_FLYING_TICKS) return;
        gui.pose().pushPose();
        int y = gui.guiHeight() * 8/10;
        float progress = (float)clientRemainingFlyingTicks / ItemFlyingSquidRing.MAX_FLYING_TICKS;
        gui.blitSprite(RenderType::guiTextured,ResourceLocation.withDefaultNamespace("hud/jump_bar_background"),gui.guiWidth()/2-91,y,182,5);
        gui.blitSprite(RenderType::guiTextured,ResourceLocation.withDefaultNamespace("hud/jump_bar_progress"),182,5,0,0,gui.guiWidth()/2-91,y, (int) (182 * progress),5);
        gui.pose().popPose();
    };
}

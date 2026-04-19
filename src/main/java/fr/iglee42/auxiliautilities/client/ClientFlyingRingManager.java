package fr.iglee42.auxiliautilities.client;

import fr.iglee42.auxiliautilities.items.ItemFlyingSquidRing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class ClientFlyingRingManager {
    public static int clientRemainingFlyingTicks = ItemFlyingSquidRing.MAX_FLYING_TICKS;

    public static void setClientRemainingFlyingTicks(int clientRemainingFlyingTicks) {
        ClientFlyingRingManager.clientRemainingFlyingTicks = Mth.clamp(clientRemainingFlyingTicks,0,ItemFlyingSquidRing.MAX_FLYING_TICKS);
    }

    public static final GuiLayer HUD = (gui, tracker)->{
        if (Minecraft.getInstance().level == null) return;
        if (clientRemainingFlyingTicks == ItemFlyingSquidRing.MAX_FLYING_TICKS) return;
        gui.pose().pushMatrix();
        int y = gui.guiHeight() * 8/10;
        float progress = (float)clientRemainingFlyingTicks / ItemFlyingSquidRing.MAX_FLYING_TICKS;
        gui.blitSprite(RenderPipelines.GUI_TEXTURED,ResourceLocation.withDefaultNamespace("hud/jump_bar_background"),gui.guiWidth()/2-91,y,182,5);
        gui.blitSprite(RenderPipelines.GUI_TEXTURED,ResourceLocation.withDefaultNamespace("hud/jump_bar_progress"),182,5,0,0,gui.guiWidth()/2-91,y, (int) (182 * progress),5);
        gui.pose().popMatrix();
    };
}

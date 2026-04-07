package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.menu.AUMenuPacket;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetClientNetwork;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetMouseInput;
import fr.iglee42.auxiliautilities.network.AUPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class AUClickWidget extends AUWidgetBase implements AUWidgetMouseInput, AUWidgetClientNetwork {
    @OnlyIn(Dist.CLIENT)
    protected boolean mouseOver;

    @OnlyIn(Dist.CLIENT)
    protected boolean hover;

    public AUClickWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseClicked(double mouseX, double mouseY, int click, boolean isHovered) {
        if (isHovered)
            mouseOver = true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseReleased(double mouseX, double mouseY, int click, boolean isHovered) {
        if (mouseOver) {
            if (isHovered)
                sendClick(click);
            mouseOver = false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseMoved(double mouseX, double mouseY, boolean isHovered) {
        this.hover = isHovered;
    }

    @OnlyIn(Dist.CLIENT)
    protected void sendClick(int click){
        AUMenuPacket pkt = getPacketToSend(click);
        if (pkt == null) return;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.menu.sendInputPacket(this,pkt);
    }

    protected abstract AUMenuPacket getPacketToSend(int click);
}

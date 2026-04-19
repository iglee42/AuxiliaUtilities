package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AUProgressWidget extends AUWidgetBase{

    public static final ResourceLocation TEXTURE = AuxiliaUtilities.id("progress/empty");
    public static final ResourceLocation TEXTURE_FULL = AuxiliaUtilities.id("progress/full");
    public static final ResourceLocation TEXTURE_ERROR = AuxiliaUtilities.id("progress/fail");

    float progress;

    public AUProgressWidget(int x, int y) {
        this(x, y,(byte)0);
    }

    public AUProgressWidget(int x, int y, byte progress) {
        super(x, y,22,17);
        this.progress = progress;
    }

    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        if (progress > 0 && progress != -1){
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,TEXTURE_FULL,getWidth(),getHeight(),0,0,guiLeft + getX(), guiTop + getY(), (int) (this.progress * getWidth()),getHeight());
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,progress != -1 ? TEXTURE : TEXTURE_ERROR,guiLeft +getX(), guiTop + getY(), getWidth(),getHeight());
    }

    public List<Component> getErrorMessages(){
        return List.of();
    }

    @Override
    public @NotNull List<Component> getTooltips() {
        if (progress == -1)
            return getErrorMessages();
        return super.getTooltips();
    }

}

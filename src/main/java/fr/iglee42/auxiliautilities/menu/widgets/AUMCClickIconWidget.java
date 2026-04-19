package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public abstract class AUMCClickIconWidget extends AUMCClickWidget{
    public AUMCClickIconWidget(int x, int y) {
        super(x, y, 18, 18);
    }

    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,getIcon(), guiLeft + getX() + 1, guiTop + getY() + 1, 16, 16);
    }

    public abstract ResourceLocation getIcon();
}

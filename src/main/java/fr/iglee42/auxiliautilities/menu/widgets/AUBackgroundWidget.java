package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class AUBackgroundWidget extends AUWidgetBase{

    private ResourceLocation texture;

    public AUBackgroundWidget(AUWidget other,ResourceLocation texture,int expand) {
        this(other.getX() - expand, other.getY() - expand, other.getWidth() + 2*expand, other.getHeight() + 2*expand, texture);
    }

    public AUBackgroundWidget(int x, int y, int width, int height, ResourceLocation texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        gui.drawBasicBackground(graphics,texture,guiLeft + getX(), guiTop + getY(), getWidth(), getHeight());
    }
}

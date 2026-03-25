package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class AUTextWidget extends AUWidgetBase {

    private final int align;

    public AUTextWidget(int x, int y, int align) {
        super(x, y, 0, 9);
        this.align = align;
    }

    protected abstract Component getMessage();

    @Override
    public int getWidth() {
        return AULang.getTextSize(getMessage());
    }

    @OnlyIn(Dist.CLIENT)
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        int x = getX() + (1 - this.align) * (gui.getXSize() - Minecraft.getInstance().font.width(getMessage())) / 2;
        graphics.drawString(Minecraft.getInstance().font, getMessage(), guiLeft + x, guiTop + getY(), 4210752,false);
    }

}

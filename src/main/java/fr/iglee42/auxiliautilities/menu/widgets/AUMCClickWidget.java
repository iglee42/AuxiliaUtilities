package fr.iglee42.auxiliautilities.menu.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class AUMCClickWidget extends AUClickWidget{

    protected static final WidgetSprites SPRITES = new WidgetSprites(
            ResourceLocation.withDefaultNamespace("widget/button"),
            ResourceLocation.withDefaultNamespace("widget/button_disabled"),
            ResourceLocation.withDefaultNamespace("widget/button_highlighted")
    );


    public boolean visible = true;
    public boolean enabled = true;

    public AUMCClickWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        if (!visible) return;

        ResourceLocation sprite = SPRITES.get(this.enabled, hover);

        int x = guiLeft + getX();
        int y = guiTop + getY();
        int width = getWidth();
        int height = getHeight();
        graphics.blitSprite(RenderType::guiTextured,sprite, x, y, width, height);


    }
}

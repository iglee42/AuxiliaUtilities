package fr.iglee42.auxiliautilities.client.screen;

import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.AUWidgetBase;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidget;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetClientTick;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetKeyInput;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetMouseInput;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AUContainerScreen extends AbstractContainerScreen<AUMenu> {
    public AUContainerScreen(AUMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        int y = 0;
        this.setWidthAndHeight(menu.width, menu.height);
        menu.getWidgets().forEach(widget->widget.addToGui(this));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        menu.getWidgetClientTick().forEach(AUWidgetClientTick::tick);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        drawBasicBackground(graphics, AUMenu.texBackground, leftPos, topPos, imageWidth, imageHeight);
        menu.getWidgets().forEach(widget->widget.renderBackground(graphics, this,leftPos, topPos));
    }

    public void drawBasicBackground(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int w, int h) {
        int w2 = w >> 1;
        int h2 = h >> 1;
        int w3 = w - w2;
        int h3 = h - h2;

        // FUNC TEXTURE X Y UOFFSET VOFFSET UWIDTH VHEIGHT
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,texture, x, y, 0, 0, w2, h2,w,h);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,texture, x + w2, y, 256 - w3, 0, w3, h2,w,h);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,texture, x, y + h2, 0, 256 - h3, w2, h3,w,h);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,texture, x + w2, y + h2, 256 - w3, 256 - h3, w3, h3,w,h);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderForeground(graphics, mouseX, mouseY);
        renderTooltip(graphics,mouseX,mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {}

    protected void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        List<Component> tooltip = null;

        tooltip = renderWindowForeground(guiGraphics, mouseX, mouseY, tooltip, leftPos, topPos);

        if (tooltip != null && !tooltip.isEmpty()) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.renderTooltip(this.font, tooltip
                    .stream().map(t->ClientTooltipComponent.create(t.getVisualOrderText())).toList(),  mouseX, mouseY, DefaultTooltipPositioner.INSTANCE,null);

            guiGraphics.pose().popMatrix();
        }
    }

    private List<Component> renderWindowForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, List<Component> tooltip, int guiX, int guiY) {
        for (AUWidget widget : this.menu.getWidgets()) {

            widget.renderForeground(guiGraphics,  this,this.leftPos, this.topPos);

            if (isInArea(mouseX, mouseY, widget)) {
                List<Component> t = new ArrayList<>(widget.getTooltips());

                if (widget instanceof AUWidgetBase base)
                    if (base.categoryId() != null)
                        t.add(Component.translatable("jei.tooltip.show.recipes"));

                if (tooltip == null)
                    tooltip = new ArrayList<>();

                tooltip.addAll(t);
            }
        }

        return tooltip;
    }

    public boolean isInArea(int mouseX, int mouseY, AUWidget widget) {
        return mouseX >= leftPos + widget.getX() && mouseX < leftPos + widget.getX() + widget.getWidth() && mouseY >= topPos + widget.getY() && mouseY < topPos + widget.getY() + widget.getHeight();
    }

    @Override
    public boolean charTyped(char typed, int modifiers) {
        for (AUWidgetKeyInput widget : this.menu.getWidgetKeyInputs()) {
            if (widget.charTyped(typed, modifiers))
                return true;
        }
        return super.charTyped(typed, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);

        int x = (int) mouseX;
        int y = (int) mouseY;

        for (AUWidgetMouseInput input : this.menu.getWidgetMouseInputs()) {
            input.mouseClicked(x, y, button, isInArea(x, y, input));
        }

        return handled;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseReleased(mouseX, mouseY, button);

        int x = (int) mouseX;
        int y = (int) mouseY;

        for (AUWidgetMouseInput input : this.menu.getWidgetMouseInputs()) {
            input.mouseReleased(x, y, button, isInArea(x, y, input));
        }

        return handled;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        boolean handled = super.mouseDragged(mouseX, mouseY, button, dragX, dragY);

        int x = (int) mouseX;
        int y = (int) mouseY;

        for (AUWidgetMouseInput input : this.menu.getWidgetMouseInputs()) {
            input.mouseDragged(x, y, button, dragX,dragY, isInArea(x, y, input));
        }

        return handled;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        boolean handled = super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);

        int x = (int) mouseX;
        int y = (int) mouseY;

        for (AUWidgetMouseInput input : this.menu.getWidgetMouseInputs()) {
            input.mouseScrolled(x,y,scrollX,scrollY, isInArea(x, y, input));
        }

        return handled;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        int x = (int) mouseX;
        int y = (int) mouseY;

        for (AUWidgetMouseInput input : this.menu.getWidgetMouseInputs()) {
            input.mouseMoved(x,y, isInArea(x, y, input));
        }
        super.mouseMoved(mouseX, mouseY);
    }

    public void setWidthAndHeight(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }
}

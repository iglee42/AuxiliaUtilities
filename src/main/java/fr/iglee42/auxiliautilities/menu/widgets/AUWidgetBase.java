package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidget;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetJEICategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AUWidgetBase implements AUWidget, AUWidgetJEICategory {
    protected int x;

    protected int y;

    protected int width;

    protected int height;

    protected AUMenu menu;

    @OnlyIn(Dist.CLIENT)
    protected AUContainerScreen gui;

    private @Nullable ResourceLocation jeiCategory = null;

    public AUWidgetBase(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @OnlyIn(Dist.CLIENT)
    public void addToGui(AUContainerScreen gui) {
        this.gui = gui;
    }

    public void jeiCategory(@Nullable ResourceLocation category) {
        this.jeiCategory = category;
    }

    @Override
    public ResourceLocation categoryId() {
        return jeiCategory;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void addToContainer(AUMenu menu) {
        this.menu = menu;
    }

    @OnlyIn(Dist.CLIENT)
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {}

    @OnlyIn(Dist.CLIENT)
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {}

    @OnlyIn(Dist.CLIENT)
    public @NotNull List<Component> getTooltips() {
        return List.of();
    }
}
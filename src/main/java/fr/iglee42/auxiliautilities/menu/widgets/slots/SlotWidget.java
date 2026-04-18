package fr.iglee42.auxiliautilities.menu.widgets.slots;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlotWidget extends Slot implements AUWidget {

    public static final ResourceLocation TEXTURE = AuxiliaUtilities.id("slot/normal");

    private int x,y;

    public SlotWidget(Container container, int slot, int x, int y) {
        super(container, slot, x + 1, y + 1);
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return container.canPlaceItem(getContainerSlot(), stack);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public @NotNull List<Component> getTooltips() {
        return List.of();
    }

    @Override
    public void renderBackground(GuiGraphics gui, AUContainerScreen screen, int guiLeft, int guiTop) {
        gui.blitSprite(RenderType::guiTextured,TEXTURE,guiLeft + getX(), guiTop + getY(), getWidth(), getHeight());
    }

    @Override
    public void addToContainer(AUMenu container) {
        container.addInternalSlot(this);
    }
}

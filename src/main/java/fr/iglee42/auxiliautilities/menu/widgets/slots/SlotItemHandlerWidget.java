package fr.iglee42.auxiliautilities.menu.widgets.slots;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlotItemHandlerWidget extends SlotItemHandler implements AUWidget {


    private int x,y;

    public SlotItemHandlerWidget(IItemHandler container, int slot, int x, int y) {
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
        gui.blitSprite(SlotWidget.TEXTURE,guiLeft + getX(), guiTop + getY(), getWidth(), getHeight());
    }

    @Override
    public void addToContainer(AUMenu container) {
        container.addInternalSlot(this);
    }
}

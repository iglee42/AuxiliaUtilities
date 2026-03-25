package fr.iglee42.auxiliautilities.menu.widgets.api;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AUWidget {

    public static final AUWidget EMPTY = new AUWidget() {
        @Override
        public int getX() {
            return 0;
        }

        @Override
        public int getY() {
            return 0;
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public @NotNull List<Component> getTooltips() {
            return List.of();
        }
    };

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    @OnlyIn(Dist.CLIENT)
    default void renderForeground(GuiGraphics gui, AUContainerScreen screen, int guiLeft, int guiTop) {}

    @OnlyIn(Dist.CLIENT)
    default void renderBackground(GuiGraphics gui, AUContainerScreen screen, int guiLeft, int guiTop) {}

    default void addToContainer(AUMenu container) {}

    @NotNull List<Component> getTooltips();

    @OnlyIn(Dist.CLIENT)
    default void addToGui(AUContainerScreen screen) {}

    default void onContainerClosed(AUMenu container, Player playerIn) {}
}

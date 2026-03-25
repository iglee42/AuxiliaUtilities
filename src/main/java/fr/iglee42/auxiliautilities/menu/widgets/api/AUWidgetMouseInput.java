package fr.iglee42.auxiliautilities.menu.widgets.api;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface AUWidgetMouseInput extends AUWidget{

    @OnlyIn(Dist.CLIENT)
    default void mouseMoved(double mouseX, double mouseY, boolean isHovered) {}

    @OnlyIn(Dist.CLIENT)
    default void mouseClicked(double mouseX, double mouseY, int click, boolean isHovered) {}

    @OnlyIn(Dist.CLIENT)
    default void mouseReleased(double mouseX, double mouseY, int click, boolean isHovered) {}

    @OnlyIn(Dist.CLIENT)
    default void mouseDragged(double mouseX, double mouseY, int click, double deltaX, double deltaY, boolean isHovered) {}

    @OnlyIn(Dist.CLIENT)
    default void mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY, boolean isHovered) {}
}

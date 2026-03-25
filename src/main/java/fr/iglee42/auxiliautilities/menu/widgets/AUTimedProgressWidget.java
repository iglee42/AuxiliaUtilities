package fr.iglee42.auxiliautilities.menu.widgets;

import com.google.common.collect.ImmutableList;
import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;

public abstract class AUTimedProgressWidget extends AUProgressWidget{
    public AUTimedProgressWidget(int x, int y) {
        super(x, y);
    }

    protected abstract float getTime();
    protected abstract float getMaxTime();

    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        float time = getTime();
        float maxTime = getMaxTime();
        if (maxTime == -1) this.progress = -1;
        else if (maxTime == 0) this.progress = 0;
        else this.progress = time / maxTime;
        super.renderBackground(graphics, gui, guiLeft, guiTop);
    }

    @Override
    public @NotNull List<Component> getTooltips() {
        if (progress == -1)
            return getErrorMessages();
        if (getMaxTime() == 0)
            return List.of();
        return ImmutableList.of(
                AULang.PROGRESS_TIME_TOOLTIP.get(AULang.formatDurationSeconds((long) getTime(),true),AULang.formatDurationSeconds((long) getMaxTime(),false)),
                Component.literal(NumberFormat.getPercentInstance().format(getTime() / getMaxTime())).withStyle(ChatFormatting.GRAY)
        );
    }
}

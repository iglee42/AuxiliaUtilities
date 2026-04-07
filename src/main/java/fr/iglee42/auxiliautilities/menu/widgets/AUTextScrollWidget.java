package fr.iglee42.auxiliautilities.menu.widgets;

import com.google.common.collect.Lists;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidget;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetAdditionalWidgets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public abstract class AUTextScrollWidget extends AUTextWidget implements AUWidgetAdditionalWidgets {
    AUScrollbarWidget scrollbar;
    List<FormattedCharSequence> strings = new ArrayList<>();
    int numLines;

    public AUTextScrollWidget(int x, int y, int width, int height) {
        super(x, y, width - 14, height);
        this.scrollbar = new AUScrollbarWidget(x+width -14,y,height,0,1){
            @Override
            protected void onChange() {}
        };
        this.scrollbar.hideWhenInvalid = true;
        this.numLines = height / 9;
    }

    @Override
    public int getWidth() {
        return super.getWidth() - 14;
    }

    @Override
    protected Component getMessage() {
        return null;
    }

    protected abstract List<Component> getMessages();

    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {}

    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        updateMessages();
        if (this.strings.isEmpty()) return;
        graphics.setColor(1,1,1,1);
        int scrollValue = this.scrollbar.scrollValue;
        List<FormattedCharSequence> messages = this.strings;
        for (int i = scrollValue; i < Math.min(messages.size(),scrollValue + this.numLines);i++)
            graphics.drawString(Minecraft.getInstance().font,messages.get(i), guiLeft + getX(), guiTop + getY() + (i - scrollValue) * 9,4210752,false);
    }

    private void updateMessages(){
        List<Component> messages = getMessages();
        this.strings = new ArrayList<>();
        if (!messages.isEmpty()) {
            messages.forEach(message -> this.strings.addAll(Minecraft.getInstance().font.split(message, getWidth())));
        }
        scrollbar.setValues(0, Math.max(0, this.strings.size() - this.numLines));
    }

    @Override
    public List<AUWidget> getAdditionalWidgets() {
        return Lists.newArrayList(scrollbar);
    }
}

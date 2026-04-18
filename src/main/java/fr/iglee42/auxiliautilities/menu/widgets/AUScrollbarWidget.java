package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetMouseInput;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class AUScrollbarWidget extends AUWidgetBase implements AUWidgetMouseInput {

    private static final ResourceLocation BACKGROUND = AuxiliaUtilities.id("scroll/bar");
    private static final ResourceLocation ENABLED = AuxiliaUtilities.id("scroll/enabled");
    public static final int BAR_WIDTH = 14;

    public int minValue;

    public int maxValue;

    public int scrollValue;

    public boolean hideWhenInvalid;

    private float drawValue;

    private boolean isScrolling;

    public AUScrollbarWidget(int x, int y, int height,int minValue,int maxValue) {
        super(x, y, 14, height);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.scrollValue = minValue;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseClicked(double mouseX, double mouseY, int click, boolean isHovered) {
        if (minValue == maxValue) return;
        if (isHovered && click == 0){
            this.isScrolling = true;
            scroll((float) (mouseY-this.gui.getGuiTop() - this.y));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setValues(int min, int max){
        this.minValue = min;
        this.maxValue = max;
        if (minValue == maxValue) {
            this.isScrolling = false;
        }
        reScroll();
    }

    @OnlyIn(Dist.CLIENT)
    public void reScroll(){
        this.scrollValue = Mth.clamp(scrollValue, minValue, maxValue);
        float a = (this.scrollValue - this.minValue);
        this.drawValue = Mth.clamp( a * (getHeight() - (float) BAR_WIDTH / 2) / (this.maxValue - this.minValue),0,(getHeight() - 17));
        onChange();
    }

    @OnlyIn(Dist.CLIENT)
    public void reScrollNoUpdate(){
        this.scrollValue = Mth.clamp(scrollValue, minValue, maxValue);
        float a = (this.scrollValue - this.minValue);
        this.drawValue = Mth.clamp( a * (getHeight() - (float) BAR_WIDTH / 2) / (this.maxValue - this.minValue),0,(getHeight() - 17));
    }

    @OnlyIn(Dist.CLIENT)
    public void scroll(float y){
        if (y <= 9.0){
            this.drawValue = 0;
            this.scrollValue = minValue;
        } else if (y >= getHeight() - 8.0) {
            this.drawValue = getHeight() - 17;
            this.scrollValue = maxValue;
        } else {
            this.drawValue = y - 9.0f;
            float a = this.drawValue * (this.maxValue - this.minValue) / (getHeight() - (float) BAR_WIDTH / 2);
            int num = this.minValue + Math.round(a);
            this.scrollValue = Mth.clamp(num, minValue, maxValue);
        }
        onChange();
    }

    @OnlyIn(Dist.CLIENT)
    protected void onChange() {}

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseReleased(double mouseX, double mouseY, int click, boolean isHovered) {
        this.isScrolling = false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseDragged(double mouseX, double mouseY, int click, double deltaX, double deltaY, boolean isHovered) {
        if (isScrolling){
            scroll((float) (mouseY - this.gui.getGuiTop() - this.y));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        if (this.hideWhenInvalid && this.minValue == this.maxValue)
            return;
        if (height == 112){
            graphics.blitSprite(RenderType::guiTextured,BACKGROUND,guiLeft + getX(),guiTop + getY(), BAR_WIDTH,height);
        } else if (height < 112){
            int h = height / 2;
            graphics.blitSprite(RenderType::guiTextured,BACKGROUND,BAR_WIDTH,112,0,0,guiLeft + getX(),guiTop + getY(), BAR_WIDTH,h);
            graphics.blitSprite(RenderType::guiTextured,BACKGROUND,BAR_WIDTH,112,0,112-h-1,guiLeft + getX(),guiTop + getY() + height - h-1, BAR_WIDTH,h + 1);
        } else {
            graphics.blitSprite(RenderType::guiTextured,BACKGROUND,guiLeft + getX(),guiTop + getY(), BAR_WIDTH,16);
            int k = 16;
            for (; k + 80 < height ; k += 80) {
                graphics.blitSprite(RenderType::guiTextured,BACKGROUND,guiLeft + getX(),guiTop + getY() + k, BAR_WIDTH,80);
            }
            graphics.blitSprite(RenderType::guiTextured,BACKGROUND,guiLeft + getX(),guiTop + getY() + k, BAR_WIDTH, height - k);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        if (this.hideWhenInvalid && this.minValue == this.maxValue)
            return;
        graphics.blitSprite(RenderType::guiTextured,ENABLED,guiLeft + getX() + 1,guiTop + getY() + 1 + (int) drawValue, 12, 15);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY, boolean isHovered) {
        if (!allowScroll(mouseX,mouseY))
            return;
        if (deltaY == 0)return;
        if (deltaY > 0)
            deltaY = -1;
        else if (deltaY < 0)
            deltaY = 1;
        setValue((int) (scrollValue + deltaY));
    }

    @OnlyIn(Dist.CLIENT)
    public void setValue(int newValue){
        newValue = Mth.clamp(newValue, minValue, maxValue);
        if (newValue != this.scrollValue) {
            this.scrollValue = newValue;
            reScroll();
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void setValueNoUpdate(int newValue){
        newValue = Mth.clamp(newValue, minValue, maxValue);
        if (newValue != this.scrollValue) {
            this.scrollValue = newValue;
            reScrollNoUpdate();
        }
    }

    public boolean allowScroll(double mouseX, double mouseY){
        return true;
    }
}

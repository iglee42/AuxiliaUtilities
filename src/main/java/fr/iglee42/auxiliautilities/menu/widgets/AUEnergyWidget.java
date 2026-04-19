package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AUEnergyWidget extends AUWidgetBase{

    public static final ResourceLocation TEXTURE = AuxiliaUtilities.id("energy/empty");
    public static final ResourceLocation TEXTURE_FULL = AuxiliaUtilities.id("energy/full");

    private final EnergyStorage storage;

    public AUEnergyWidget(int x, int y, EnergyStorage storage) {
        super(x, y,18,54);
        this.storage = storage;
    }

    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        if (storage.getEnergyStored() > 0){
            float progress = (float) storage.getEnergyStored() / storage.getMaxEnergyStored();

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,TEXTURE_FULL,getWidth(),getHeight(),0, (int) ((1-progress) * getHeight()),guiLeft + getX(), (int) (guiTop + getY() + (1-progress) * getHeight()), getWidth(), (int) (getHeight() * progress));
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        graphics.blitSprite( RenderPipelines.GUI_TEXTURED,TEXTURE ,guiLeft +getX(), guiTop + getY(), getWidth(),getHeight());
    }

    @Override
    public @NotNull List<Component> getTooltips() {
        return List.of(AULang.STORED_ENERGY_TOOLTIP.get(NumberFormat.getInstance(Locale.UK).format(storage.getEnergyStored()), NumberFormat.getInstance(Locale.UK).format(storage.getMaxEnergyStored())));
    }
}

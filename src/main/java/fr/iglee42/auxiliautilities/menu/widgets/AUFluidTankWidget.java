package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.utils.FluidTankRenderer;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AUFluidTankWidget extends AUWidgetBase{

    public static final ResourceLocation TEXTURE = AuxiliaUtilities.id("tank/wide_small");

    private final FluidTank storage;
    private final FluidTankRenderer renderer;


    public AUFluidTankWidget(int x, int y, FluidTank storage) {
        super(x, y,18,33);
        this.storage = storage;
        this.renderer = new FluidTankRenderer(storage.getCapacity(), getWidth(), getHeight());
    }

    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        graphics.pose().pushPose();
        graphics.pose().translate(0,0,100);
        graphics.blitSprite(RenderType::guiTextured,TEXTURE ,guiLeft +getX(), guiTop + getY(), getWidth(),getHeight());
        graphics.pose().popPose();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        if (storage.getFluidAmount() > 0 ){
            renderer.render(graphics, guiLeft + getX(), guiTop + getY(), storage.getFluid());
        }
    }

    @Override
    public @NotNull List<Component> getTooltips() {
        return renderer.getTooltip(storage.getFluid());
    }
}

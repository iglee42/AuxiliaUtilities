package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.AUMenuPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AUMCClickChoiceWidget<T> extends AUMCClickWidget{

    public final ArrayList<Choice<T>> choices = new ArrayList<>();

    int selected = 0;
    int networkState = 0;
    public AUMCClickChoiceWidget(int x, int y) {
        super(x, y, 18, 18);
    }


    public <V extends AUMCClickChoiceWidget<T>> V addChoice(T marker, ItemStack stack, Component tooltip) {
        int ordinal = this.choices.size();
        Choice<T> choice = new Choice<>(ordinal, marker, stack, tooltip);
        this.choices.add(choice);
        return (V)this;
    }

    public <V extends AUMCClickChoiceWidget<T>> V addChoice(T marker, Component displayText, ItemStack stack, Component tooltip) {
        int ordinal = this.choices.size();
        Choice<T> choice = new Choice<>(ordinal, marker, displayText, stack, tooltip);
        this.choices.add(choice);
        this.width = Math.max(this.width, 26 + AULang.getTextSize(displayText));
        return (V)this;
    }

    public <V extends AUMCClickChoiceWidget<T>> V addChoice(T marker, Component displayText, Component tooltip) {
        int ordinal = this.choices.size();
        Choice<T> choice = new Choice<>(ordinal, marker, displayText, tooltip);
        this.choices.add(choice);
        this.width = Math.max(this.width, 8 + AULang.getTextSize(displayText));
        return (V)this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        super.renderBackground(graphics, gui, guiLeft, guiTop);
        Choice<T> choice = choices.get(selected);
        Component displayText = choice.displayText;
        if (displayText == null || Objects.equals(displayText, Component.empty()))
            return;

        Font font = Minecraft.getInstance().font;

        int textWidth = font.width(displayText);

        int x;

        if (!choice.stack.isEmpty()) {
            x = getX() + 18 + (getWidth() - 18 - textWidth) / 2;
        } else {
            x = getX() + (getWidth() - textWidth) / 2;
        }

        int color = 14737632;

        if (!this.enabled) {
            color = 10526880;
        } else if (this.hover) {
            color = 16777120;
        }

        graphics.drawString(
                font,
                displayText,
                guiLeft + x,
                guiTop + getY() + 4,
                color,
                false
        );
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderForeground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
        Choice<T> choice = this.choices.get(this.selected);
        if (choice.stack != ItemStack.EMPTY) {
            graphics.renderItem(choice.stack, guiLeft + x + 1, guiTop + y + 1);
        }
    }

    @Override
    public @NotNull List<Component> getTooltips() {
        Choice<T> choice = this.choices.get(this.selected);
        return (choice != null && choice.tooltip != null && !Objects.equals(choice.tooltip, Component.empty()))? List.of(choice.tooltip) : super.getTooltips();
    }

    @Override
    public void receiveClientPacket(IPayloadContext context, AUMenuPacket packet) {
        if (packet instanceof AUChoicePacket pkt){
            int newState =  pkt.networkState;
            int selection = pkt.selection;
            if (newState > this.networkState) {
                this.networkState = newState;
                this.selected = selection;
                Choice<T> choice = this.choices.get(selection);
                onSelectedServer(choice.marker);
            }
        }
    }

    @Override
    protected AUMenuPacket getPacketToSend(int click) {
        if (click == 1) {
            this.selected--;
            if (this.selected < 0)
                this.selected = this.choices.size() - 1;
        } else if (click == 0) {
            this.selected++;
            if (this.selected >= this.choices.size())
                this.selected = 0;
        } else {
            return null;
        }

        this.networkState++;
        return new AUChoicePacket(this.networkState, this.selected);
    }

    protected abstract void onSelectedServer(T value);

    public abstract T getSelectedValue();

    public static class AUChoicePacket extends AUMenuPacket {

        static {
            AUMenuPacket.register(AUChoicePacket.class, AUChoicePacket::new);
        }
        public int networkState = 0;
        public int selection = 0;

        private AUChoicePacket() {
        }

        public AUChoicePacket(int networkState, int selection) {
            this.networkState = networkState;
            this.selection = selection;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer) {
            buffer.writeVarInt(networkState);
            buffer.writeVarInt(selection);
        }

        @Override
        protected void decode(RegistryFriendlyByteBuf buffer) {
            networkState = buffer.readVarInt();
            selection = buffer.readVarInt();
        }
    }

    public static class Choice<T> {
        final int ordinal;

        final T marker;

        final ItemStack stack;

        final Component tooltip;

        final Component displayText;

        public Choice(int ordinal, T marker, Component displayText, @Nullable Component tooltip) {
            this.ordinal = ordinal;
            this.marker = marker;
            this.displayText = displayText;
            this.tooltip = tooltip;
            this.stack = ItemStack.EMPTY;
        }

        public Choice(int ordinal, T marker, ItemStack stack, Component tooltip) {
            this(ordinal, marker, null, stack, tooltip);
        }

        public Choice(int ordinal, T marker, Component displayText, ItemStack stack, Component tooltip) {
            this.ordinal = ordinal;
            this.marker = marker;
            this.stack = stack;
            this.tooltip = tooltip;
            this.displayText = displayText;
        }
    }
}

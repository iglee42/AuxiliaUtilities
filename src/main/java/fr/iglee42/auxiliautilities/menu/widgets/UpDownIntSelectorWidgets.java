package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.menu.AUMenuPacket;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.stream.Stream;

public abstract class UpDownIntSelectorWidgets implements Iterable<AUWidgetBase> {

    final AUMCClickWidget up;

    final AUMCClickWidget down;

    final AUTextWidget text;

    @OnlyIn(Dist.CLIENT)
    public static int getOffset() {
        if (Screen.hasShiftDown())
            return 10;
        return 1;
    }

    public UpDownIntSelectorWidgets(int x, int y, int width) {
        this.up = new AUMCClickIconWidget(x+width / 2 - 9,y) {
            @Override
            public ResourceLocation getIcon() {
                return AuxiliaUtilities.id("buttons/up");
            }

            @Override
            protected AUMenuPacket getPacketToSend(int click) {
                return new ChangeIntValueMenuPacket(getValue() + getOffset());
            }

            @Override
            public void receiveClientPacket(IPayloadContext context, AUMenuPacket packet) {
                setValue(((ChangeIntValueMenuPacket)packet).value);
            }
        };
        this.text = new AUTextWidget(x + width / 2 - 4,y+18+2,1) {
            @Override
            protected Component getMessage() {
                return Component.literal(NumberFormat.getIntegerInstance(Locale.UK).format(getValue()));
            }
        };
        this.down = new AUMCClickIconWidget(x+width / 2 - 9,y + 4 +18+ 9) {
            @Override
            public ResourceLocation getIcon() {
                return AuxiliaUtilities.id("buttons/down");
            }

            @Override
            protected AUMenuPacket getPacketToSend(int click) {
                return new ChangeIntValueMenuPacket(getValue() - getOffset());
            }

            @Override
            public void receiveClientPacket(IPayloadContext context, AUMenuPacket packet) {
                setValue(((ChangeIntValueMenuPacket)packet).value);
            }
        };
    }

    public abstract int getValue();

    public abstract void setValue(int value);

    @Override
    public @NotNull Iterator<AUWidgetBase> iterator() {
        return Stream.of(up, text, down).iterator();
    }


    public static class ChangeIntValueMenuPacket extends AUMenuPacket{

        private int value;

        static {
            AUMenuPacket.register(ChangeIntValueMenuPacket.class, ChangeIntValueMenuPacket::new);
        }

        public ChangeIntValueMenuPacket() {
            this(0);
        }

        public ChangeIntValueMenuPacket(int value) {
            this.value = value;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer) {
            buffer.writeVarInt(value);
        }

        @Override
        protected void decode(RegistryFriendlyByteBuf buffer) {
            this.value = buffer.readVarInt();
        }
    }
}

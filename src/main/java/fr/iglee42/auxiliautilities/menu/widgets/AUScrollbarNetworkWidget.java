package fr.iglee42.auxiliautilities.menu.widgets;

import fr.iglee42.auxiliautilities.menu.AUMenuPacket;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetClientNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class AUScrollbarNetworkWidget extends AUScrollbarWidget implements AUWidgetClientNetwork {
    public AUScrollbarNetworkWidget(int x, int y, int height, int minValue, int maxValue) {
        super(x, y, height, minValue, maxValue);
    }

    @Override
    public void receiveClientPacket(IPayloadContext context, AUMenuPacket packet) {
        if (packet instanceof AUScrollbarPacket scrollbarPacket) {
            int newValue = Mth.clamp(scrollbarPacket.newValue, minValue, maxValue);
            if (newValue != getValueServer()) {
                setValueServer(newValue);
            }
        }
    }

    @Override
    protected void onChange() {
        menu.sendInputPacket(this,new AUScrollbarPacket(scrollValue));
    }

    public abstract int getValueServer();

    public abstract void setValueServer(int value);


    public static class AUScrollbarPacket extends AUMenuPacket {

        static {
            AUMenuPacket.register(AUScrollbarPacket.class, AUScrollbarPacket::new);
        }
        public int newValue = 0;

        private AUScrollbarPacket() {}

        public AUScrollbarPacket(int newValue) {
            this.newValue = newValue;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer) {
            buffer.writeVarInt(newValue);
        }

        @Override
        protected void decode(RegistryFriendlyByteBuf buffer) {
            newValue = buffer.readVarInt();
        }
    }

}

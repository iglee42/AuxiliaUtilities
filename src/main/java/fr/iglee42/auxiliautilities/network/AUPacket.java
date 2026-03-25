package fr.iglee42.auxiliautilities.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class AUPacket implements CustomPacketPayload {

    private final AUPackets type;

    public AUPacket(AUPackets type) {
        this.type = type;
    }

    protected abstract void handle(IPayloadContext context);


    public AUPackets getType() {
        return type;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type.getType();
    }
}

package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.client.ClientGPManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncGPNetworkPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf,SyncGPNetworkPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncGPNetworkPacket::getTotalGeneration,
            ByteBufCodecs.INT, SyncGPNetworkPacket::getTotalConsumption,
            SyncGPNetworkPacket::new
    );

    private final int totalGeneration;
    private final int totalConsumption;

    public SyncGPNetworkPacket(int totalGeneration, int totalConsumption) {
        super(AUPackets.SYNC_GP_NETWORK);
        this.totalGeneration = totalGeneration;
        this.totalConsumption = totalConsumption;
    }

    public int getTotalGeneration() {
        return totalGeneration;
    }

    public int getTotalConsumption() {
        return totalConsumption;
    }

    @Override
    protected void handle(IPayloadContext context) {
        ClientGPManager.loadFromPacket(this);
    }
}

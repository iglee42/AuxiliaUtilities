package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.client.ClientFlyingRingManager;
import fr.iglee42.auxiliautilities.client.ClientGPManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdatePlayerRemainingFlyingTicksPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdatePlayerRemainingFlyingTicksPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdatePlayerRemainingFlyingTicksPacket::getRemainingFlyingTicks,
            UpdatePlayerRemainingFlyingTicksPacket::new
    );

    private final int remainingFlyingTicks;

    public UpdatePlayerRemainingFlyingTicksPacket(int remainingFlyingTicks) {
        super(AUPackets.UPDATE_PLAYER_REMAINING_FLYING_TICKS);
        this.remainingFlyingTicks = remainingFlyingTicks;
    }

    public int getRemainingFlyingTicks() {
        return remainingFlyingTicks;
    }

    @Override
    protected void handle(IPayloadContext context) {
        ClientFlyingRingManager.setClientRemainingFlyingTicks(remainingFlyingTicks);
    }
}

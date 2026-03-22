package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.client.ClientGPManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SendCurrentBlockGPPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, SendCurrentBlockGPPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SendCurrentBlockGPPacket::getBlockGP,
            SendCurrentBlockGPPacket::new
    );

    private final int blockGP;

    public SendCurrentBlockGPPacket(int blockGP) {
        super(AUPackets.SEND_CURRENT_BLOCK_GP);
        this.blockGP = blockGP;
    }

    public int getBlockGP() {
        return blockGP;
    }

    @Override
    protected void handle(IPayloadContext context) {
        ClientGPManager.setCurrentBlockGP(blockGP);
    }
}

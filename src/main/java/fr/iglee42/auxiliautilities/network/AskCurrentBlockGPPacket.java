package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.blockentities.gp.AUGPBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AskCurrentBlockGPPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, AskCurrentBlockGPPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, AskCurrentBlockGPPacket::getPos,
            AskCurrentBlockGPPacket::new
    );

    private final BlockPos pos;

    public AskCurrentBlockGPPacket(BlockPos pos) {
        super(AUPackets.ASK_CURRENT_BLOCK_GP);
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    protected void handle(IPayloadContext context) {
        if (context.player().level() != null){
            if (context.player().level().getBlockEntity(pos) instanceof AUGPBlockEntity be){
                context.reply(new SendCurrentBlockGPPacket(be.getGPGeneration() == 0 ? -be.getGPConsumption() : be.getGPGeneration()));
            }
        }
    }
}

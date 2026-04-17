package fr.iglee42.auxiliautilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlayerMoveVerticallyPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMoveVerticallyPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, PlayerMoveVerticallyPacket::getVerticalMotion,
            PlayerMoveVerticallyPacket::new
    );

    private final double verticalMotion;

    public PlayerMoveVerticallyPacket(double verticalMotion) {
        super(AUPackets.PLAYER_MOVE_VERTICALLY);
        this.verticalMotion = verticalMotion;
    }

    public double getVerticalMotion() {
        return verticalMotion;
    }

    @Override
    protected void handle(IPayloadContext context) {
        context.enqueueWork(()-> {
            Vec3 motion = Minecraft.getInstance().player.getDeltaMovement();
            Minecraft.getInstance().player.setDeltaMovement(motion.x(), verticalMotion, motion.z());
        });
    }
}

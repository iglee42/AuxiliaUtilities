package fr.iglee42.auxiliautilities.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlaySoundPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, PlaySoundPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, PlaySoundPacket::getSoundId,
            BlockPos.STREAM_CODEC, PlaySoundPacket::getPos,
            PlaySoundPacket::new
    );

    private final ResourceLocation soundId;
    private final BlockPos pos;

    public PlaySoundPacket(ResourceLocation soundId, BlockPos pos) {
        super(AUPackets.PLAY_SOUND);
        this.soundId = soundId;
        this.pos = pos;
    }

    public ResourceLocation getSoundId() {
        return soundId;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    protected void handle(IPayloadContext context) {
        context.enqueueWork(()->{
            var level = Minecraft.getInstance().level;
            if (level != null){
                SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(soundId);
                if (event != null){
                    level.playLocalSound(pos,event, SoundSource.PLAYERS,1,1,false);
                }
            }
        });
    }
}

package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.blockentities.gp.AUGPBlockEntity;
import fr.iglee42.auxiliautilities.utils.CommonKeysHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class UpdateTrackedKeysPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTrackedKeysPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new,ByteBufCodecs.STRING_UTF8,ByteBufCodecs.BOOL), UpdateTrackedKeysPacket::getKeys,
            UpdateTrackedKeysPacket::new
    );

    private final Map<String,Boolean> keys;

    public UpdateTrackedKeysPacket(Map<String,Boolean> keys) {
        super(AUPackets.UPDATE_TRACKED_KEYS);
        this.keys = keys;
    }

    public Map<String, Boolean> getKeys() {
        return keys;
    }

    @Override
    protected void handle(IPayloadContext context) {
        if (context.player() != null){
            getKeys().forEach((name,val)->
                    CommonKeysHandler.setKeyState(context.player(),name,val));
        }
    }
}

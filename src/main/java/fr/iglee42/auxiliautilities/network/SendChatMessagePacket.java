package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.client.AUClient;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class SendChatMessagePacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, SendChatMessagePacket> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.OPTIONAL_STREAM_CODEC, SendChatMessagePacket::getMessage,
            UUIDUtil.STREAM_CODEC,SendChatMessagePacket::getSignature,
            SendChatMessagePacket::new
    );

    private final Optional<Component> message;
    private final UUID signature;

    protected SendChatMessagePacket(Optional<Component> message, UUID signature) {
        super(AUPackets.SEND_CHAT_MESSAGE);
        this.message = message;
        this.signature = signature;
    }

    public SendChatMessagePacket(@Nullable Component message, UUID signature) {
        this(Optional.ofNullable(message), signature);
    }

    public Optional<Component> getMessage() {
        return message;
    }

    public UUID getSignature() {
        return signature;
    }

    @Override
    protected void handle(IPayloadContext context) {
        AUClient.sendPlayerMessage(context.player(),message.orElse(null), AULang.createSignatureFromUUID(signature));
    }
}

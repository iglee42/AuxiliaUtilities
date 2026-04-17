package fr.iglee42.auxiliautilities.network;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.ItemFlatTransferNode;
import fr.iglee42.auxiliautilities.items.ItemPowerManager;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public enum AUPackets {

    // TO CLIENT
    SEND_CHAT_MESSAGE(SendChatMessagePacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    SYNC_GP_NETWORK(SyncGPNetworkPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    SEND_CURRENT_BLOCK_GP(SendCurrentBlockGPPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    SYNC_FLAT_TRANSFER_NODES(SyncFlatTransferNodesPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    POWER_DATA(ItemPowerManager.PowerDataPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    PLAY_SOUND(PlaySoundPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    UPDATE_PLAYER_REMAINING_FLYING_TICKS(UpdatePlayerRemainingFlyingTicksPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),
    PLAYER_MOVE_VERTICALLY(PlayerMoveVerticallyPacket.STREAM_CODEC, PacketFlow.CLIENTBOUND),

    // TO SERVER
    ASK_CURRENT_BLOCK_GP(AskCurrentBlockGPPacket.STREAM_CODEC, PacketFlow.SERVERBOUND),
    UPDATE_TRACKED_KEYS(UpdateTrackedKeysPacket.STREAM_CODEC, PacketFlow.SERVERBOUND),
    LEFT_CLICK_FLAT_TRANSFER_NODE(ItemFlatTransferNode.LeftClickFlatTransferNodePacket.STREAM_CODEC, PacketFlow.SERVERBOUND),
    MENU_INPUT(AUMenu.MenuInputPacket.STREAM_CODEC, PacketFlow.SERVERBOUND),
    SUBMIT_GHOST_ITEM(SubmitGhostItemPacket.STREAM_CODEC, PacketFlow.SERVERBOUND),

    ;


    private final String name;
    private final StreamCodec<RegistryFriendlyByteBuf, ? extends AUPacket> streamCodec;
    @Nullable
    private final PacketFlow flow;

    AUPackets(StreamCodec<RegistryFriendlyByteBuf, ? extends AUPacket> streamCodec, @Nullable PacketFlow flow) {
        this.flow = flow;
        this.name = name().toLowerCase();
        this.streamCodec = streamCodec;
    }

    AUPackets(StreamCodec<RegistryFriendlyByteBuf, ? extends AUPacket> streamCodec) {
        this(streamCodec, null);
    }

    public CustomPacketPayload.Type<? extends AUPacket> getType(){
        return new CustomPacketPayload.Type<>(AuxiliaUtilities.id(name));
    }

    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AuxiliaUtilities.MODID);
        for (AUPackets packetType : values()) {
            register(registrar, packetType);
        }
    }

    private static void register(PayloadRegistrar registrar, AUPackets packetType) {
        registerTyped(registrar, packetType);
    }

    @SuppressWarnings("unchecked")
    private static <T extends AUPacket> void registerTyped(PayloadRegistrar registrar, AUPackets packetType) {
        CustomPacketPayload.Type<T> type = (CustomPacketPayload.Type<T>) packetType.getType();
        StreamCodec<RegistryFriendlyByteBuf, T> codec = (StreamCodec<RegistryFriendlyByteBuf, T>) packetType.streamCodec;

        if (packetType.flow == PacketFlow.CLIENTBOUND) {
            registrar.playToClient(type, codec, AUPackets::handlePacket);
            return;
        }

        if (packetType.flow == PacketFlow.SERVERBOUND) {
            registrar.playToServer(type, codec, AUPackets::handlePacket);
            return;
        }

        registrar.playBidirectional(type, codec, AUPackets::handlePacket);
    }

    private static void handlePacket(AUPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> packet.handle(context));
    }
}

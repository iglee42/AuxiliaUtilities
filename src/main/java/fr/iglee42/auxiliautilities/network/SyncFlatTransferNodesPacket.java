package fr.iglee42.auxiliautilities.network;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public class SyncFlatTransferNodesPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncFlatTransferNodesPacket> STREAM_CODEC = StreamCodec.composite(
            FlatTransferNodeHandler.FlatTransferNode.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncFlatTransferNodesPacket::getNodes,
            SyncFlatTransferNodesPacket::new
    );

    private final List<FlatTransferNodeHandler.FlatTransferNode> nodes;

    public SyncFlatTransferNodesPacket(List<FlatTransferNodeHandler.FlatTransferNode> nodes) {
        super(AUPackets.SYNC_FLAT_TRANSFER_NODES);
        this.nodes = nodes;
    }

    public List<FlatTransferNodeHandler.FlatTransferNode> getNodes() {
        return nodes;
    }

    @Override
    protected void handle(IPayloadContext context) {
        Multimap<BlockPos, FlatTransferNodeHandler.FlatTransferNode> map = HashMultimap.create();
        for (FlatTransferNodeHandler.FlatTransferNode node : nodes) {
            map.put(node.getPos(), node);
        }
        FlatTransferNodeHandler.CLIENT_NODES = map;
    }
}

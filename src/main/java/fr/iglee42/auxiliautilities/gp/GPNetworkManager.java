package fr.iglee42.auxiliautilities.gp;

import fr.iglee42.auxiliautilities.network.SyncGPNetworkPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global access point for GP networks.
 */
public class GPNetworkManager {
    public static final GPNetworkManager INSTANCE = new GPNetworkManager();

    private final Map<UUID, GPNetwork> networks = new ConcurrentHashMap<>();

    private GPNetworkManager() {
    }

    public GPNetwork getNetwork(UUID network) {
        return networks.computeIfAbsent(network, GPNetwork::new);
    }

    public void registerGenerator(GPHolder generator) {
        UUID owner = generator != null ? generator.getNetworkId() : null;
        if (owner == null) return;
        getNetwork(owner).registerGenerator(generator);
    }

    public void unregisterGenerator(GPHolder generator) {
        UUID network = generator != null ? generator.getNetworkId() : null;
        if (network == null) return;
        getNetwork(network).unregisterGenerator(generator);
    }

    public void registerConsumer(GPHolder consumer) {
        UUID network = consumer != null ? consumer.getNetworkId() : null;
        if (network == null) return;
        getNetwork(network).registerConsumer(consumer);
    }

    public void unregisterConsumer(GPHolder consumer) {
        UUID network = consumer != null ? consumer.getNetworkId() : null;
        if (network == null) return;
        getNetwork(network).unregisterConsumer(consumer);
    }

    public boolean hasEnoughPower(UUID network) {
        if (network == null) return false;
        return getNetwork(network).hasEnoughPower();
    }

    protected void syncToPlayer(GPNetwork network){
        if (ServerLifecycleHooks.getCurrentServer() == null)
            throw new UnsupportedOperationException("GP Network Syncing must only be done on server side !");
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerPlayer player;
        if ((player = server.getPlayerList().getPlayer(network.getId())) != null) {
            PacketDistributor.sendToPlayer(player,new SyncGPNetworkPacket(network.getTotalGeneration(),network.getTotalConsumption()));
        }
    }

}


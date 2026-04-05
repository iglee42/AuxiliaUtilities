package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.network.UpdateTrackedKeysPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class CommonKeysHandler {

    private static final List<String> trackedKeys = List.of(
            "key.sprint",
            "key.jump",
            "key.forward",
            "key.back",
            "key.left",
            "key.right"
    );

    private static final Map<Player, Map<String, Boolean>> playerKeyStates = new WeakHashMap<>();

    public static void setKeyState(Player player, String key, boolean pressed) {
        playerKeyStates.computeIfAbsent(player, p -> new WeakHashMap<>()).put(key, pressed);
    }

    public static boolean isKeyPressed(Player player, String key) {
        return playerKeyStates.getOrDefault(player, Map.of()).getOrDefault(key, false);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        playerKeyStates.remove(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        playerKeyStates.remove(event.getEntity());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        var mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player != null) {
            var player = mc.player;
            Map<String, Boolean> newStates = new WeakHashMap<>();
            for (KeyMapping mapping : Arrays.stream(mc.options.keyMappings).filter(km -> trackedKeys.contains(km.getName())).toList()) {
                if (mapping.isDown() != isKeyPressed(player, mapping.getName())) {
                    newStates.put(mapping.getName(), mapping.isDown());
                }
            }
            newStates.forEach((key, pressed) -> setKeyState(player, key, pressed));
            if (!newStates.isEmpty()) {
                PacketDistributor.sendToServer(new UpdateTrackedKeysPacket(newStates));
            }
        }
    }

    public static boolean isHoldingSprint(Player player) {
        return isKeyPressed(player, "key.sprint");
    }

}

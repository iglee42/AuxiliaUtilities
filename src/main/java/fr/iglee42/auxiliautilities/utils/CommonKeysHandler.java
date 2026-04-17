package fr.iglee42.auxiliautilities.utils;

import com.mojang.blaze3d.platform.InputConstants;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.network.UpdateTrackedKeysPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class CommonKeysHandler {

    private static final List<String> trackedKeys = List.of(
            "key.jump"
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
            for (KeyMapping mapping : Arrays.stream(mc.options.keyMappings).filter(km -> trackedKeys.contains(km.getName()) || km.getName().contains(AuxiliaUtilities.MODID)).toList()) {
                if (isKeyPressed(mapping) != isKeyPressed(player, mapping.getName())) {
                    newStates.put(mapping.getName(), mapping.isDown());
                }
            }
            newStates.forEach((key, pressed) -> setKeyState(player, key, pressed));
            if (!newStates.isEmpty()) {
                PacketDistributor.sendToServer(new UpdateTrackedKeysPacket(newStates));
            }
        }
    }

    /*Code from Mekanism for key detection*/

    public static boolean isKeyPressed(KeyMapping keyBinding) {
        if (keyBinding.isDown()) {
            return true;
        }
        if (keyBinding.getKeyConflictContext().isActive() && keyBinding.getKeyModifier().isActive(keyBinding.getKeyConflictContext())) {
            //Manually check in case keyBinding#pressed just never got a chance to be updated
            return isKeyDown(keyBinding);
        }
        //If we failed, due to us being a key modifier as our key, check the old way
        return KeyModifier.isKeyCodeModifier(keyBinding.getKey()) && isKeyDown(keyBinding);
    }

    private static boolean isKeyDown(KeyMapping keyBinding) {
        InputConstants.Key key = keyBinding.getKey();
        int keyCode = key.getValue();
        if (keyCode != InputConstants.UNKNOWN.getValue()) {
            long windowHandle = Minecraft.getInstance().getWindow().getWindow();
            try {
                if (key.getType() == InputConstants.Type.KEYSYM) {
                    return InputConstants.isKeyDown(windowHandle, keyCode);
                } else if (key.getType() == InputConstants.Type.MOUSE) {
                    return GLFW.glfwGetMouseButton(windowHandle, keyCode) == GLFW.GLFW_PRESS;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    /* END OF MEKANISM CODE */

    public static boolean isHoldingModKey(Player player, String modKey) {
        return isKeyPressed(player, getKeyName(modKey));
    }

    public static String getKeyName(String key){
        return "key."+ AuxiliaUtilities.MODID+"."+key;
    }

}

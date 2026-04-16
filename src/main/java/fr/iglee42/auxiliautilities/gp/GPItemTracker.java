package fr.iglee42.auxiliautilities.gp;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class GPItemTracker {

    private static final Map<UUID, Map<Integer, GPItemHolder>> ACTIVE_CONSUMERS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        UUID playerId = player.getUUID();
        Inventory inventory = player.getInventory();
        Map<Integer, GPItemHolder> tracked = ACTIVE_CONSUMERS.computeIfAbsent(playerId, id -> new HashMap<>());
        Set<Integer> seen = new HashSet<>();

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);

            GPItemHolder itemHolder = stack.getCapability(GPCapabilities.ITEM,player);
            if (itemHolder != null) seen.add(slot);
            GPItemHolder oldHolder = tracked.get(slot);
            boolean replaceHolder = oldHolder == null
                    || !ItemStack.isSameItemSameComponents(oldHolder.getStack(), stack)
                    || !Objects.equals(oldHolder,itemHolder);

            if (!replaceHolder) continue;
            if (oldHolder != null) {
                GPNetworkManager.INSTANCE.unregisterConsumer(oldHolder);
                GPNetworkManager.INSTANCE.unregisterGenerator(oldHolder);
            }
            if (itemHolder == null) continue;
            tracked.put(slot, itemHolder);
            if (itemHolder.getGPGeneration() > 0) GPNetworkManager.INSTANCE.registerGenerator(itemHolder);
            if (itemHolder.getGPConsumption() > 0) GPNetworkManager.INSTANCE.registerConsumer(itemHolder);
        }

        Iterator<Map.Entry<Integer, GPItemHolder>> iterator = tracked.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, GPItemHolder> entry = iterator.next();
            if (seen.contains(entry.getKey())) continue;
            GPItemHolder holder = entry.getValue();
            GPNetworkManager.INSTANCE.unregisterGenerator(holder);
            GPNetworkManager.INSTANCE.unregisterConsumer(holder);
            iterator.remove();
        }

        if (tracked.isEmpty()) {
            ACTIVE_CONSUMERS.remove(playerId);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        clear(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        clear(event.getOriginal());
    }

    private static void clear(Player player) {
        Map<Integer, GPItemHolder> tracked = ACTIVE_CONSUMERS.remove(player.getUUID());
        if (tracked == null || tracked.isEmpty()) return;
        tracked.values().forEach(GPNetworkManager.INSTANCE::unregisterConsumer);
        tracked.values().forEach(GPNetworkManager.INSTANCE::unregisterGenerator);
    }

    private GPItemTracker() {
    }
}


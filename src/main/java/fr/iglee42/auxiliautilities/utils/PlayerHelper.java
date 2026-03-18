package fr.iglee42.auxiliautilities.utils;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class PlayerHelper {

    public static boolean isPlayerReal(Player player) {
        return !(player instanceof FakePlayer);
    }
}

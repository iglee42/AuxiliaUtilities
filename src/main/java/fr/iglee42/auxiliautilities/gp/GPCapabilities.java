package fr.iglee42.auxiliautilities.gp;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ItemCapability;

public class GPCapabilities {
    public static final ItemCapability<GPItemHolder, Player> ITEM = ItemCapability.create(AuxiliaUtilities.id("grid_power"), GPItemHolder.class, Player.class);

    private GPCapabilities() {
    }
}

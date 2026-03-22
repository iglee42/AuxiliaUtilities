package fr.iglee42.auxiliautilities.gp;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class GPCapabilities {
    public static final BlockCapability<GPHolder, Void> BLOCK = BlockCapability.createVoid(AuxiliaUtilities.id("grid_power"), GPHolder.class);
    public static final ItemCapability<GPHolder, Void> ITEM = ItemCapability.createVoid(AuxiliaUtilities.id("grid_power"), GPHolder.class);

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

    }

    private GPCapabilities() {
    }
}


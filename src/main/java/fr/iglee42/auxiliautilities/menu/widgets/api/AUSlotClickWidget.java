package fr.iglee42.auxiliautilities.menu.widgets.api;

import fr.iglee42.auxiliautilities.menu.AUMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;

public interface AUSlotClickWidget {

    void onSlotClick(AUMenu menu,int slotId, int button, ClickType clickType, Player player);
}

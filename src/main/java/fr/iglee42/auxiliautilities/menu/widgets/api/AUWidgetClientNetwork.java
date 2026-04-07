package fr.iglee42.auxiliautilities.menu.widgets.api;

import fr.iglee42.auxiliautilities.menu.AUMenuPacket;
import fr.iglee42.auxiliautilities.network.AUPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface AUWidgetClientNetwork extends AUWidget {
  void receiveClientPacket(IPayloadContext context, AUMenuPacket packet);
}
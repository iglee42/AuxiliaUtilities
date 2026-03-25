package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.utils.Upgrade;
import net.minecraft.world.item.ItemStack;

public interface UpgradeProvider {

    Upgrade getUpgrade(ItemStack stack);

}

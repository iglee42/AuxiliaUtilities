package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.UpgradeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public enum Upgrade {
  STACK_SIZE(1, 5.0F),
  SPEED(64, 1.0F),
  MINING(1, 10.0F) {
    public float getPowerUse(int level) {
      return 0.0F;
    }
  };
  
  static final float PENALTY = 0.032786883F;

  private final int maxLevel;

  private final float power;

  Upgrade(int maxLevel, float power) {
    this.maxLevel = maxLevel;
    this.power = power;
  }

  public float getPower() {
    return power;
  }

  public int getMaxLevel() {
    return maxLevel;
  }

  public static void addTooltip(List<Component> tooltip, ItemStack stack, UpgradeProvider item, int stacklimitoverride) {
    Upgrade upgrade = item.getUpgrade(stack);
    if (upgrade != null) {
      tooltip.add(upgrade.getDescription());
      int stackSize = stack.getCount();
      int maxLevel = (stacklimitoverride == -1) ? upgrade.maxLevel : stacklimitoverride;
      tooltip.add(AULang.MAX_UPGRADES_TOOLTIP.get(maxLevel));
      if (upgrade.power > 0.0F)
        if (maxLevel == 1) {
          tooltip.add(AULang.POWER_PENALTY_TOOLTIP.get(upgrade.getPowerUse(1)));
        } else {
          tooltip.add(AULang.POWER_PENALTY_TOOLTIP_LEVEL.get(1,upgrade.getPowerUse(1)));
          if (stackSize > 1 && maxLevel > 1)
            tooltip.add(AULang.POWER_PENALTY_TOOLTIP_LEVEL.get(Math.min(stackSize, maxLevel),upgrade.getPowerUse(Math.min(stackSize, maxLevel))));
        }
    } 
  }
  
  public int getModifierLevel(int level) {
    return level;
  }
  
  public float getPowerUse(int level) {
    if (level == 1)
      return 1.0F; 
    float v = Math.round((100 * level) * (1.0F + level * PENALTY) / 1.0327868F) / 100.0F;
    return v * this.power;
  }

  private Component getDescription(){
    return Component.translatable("upgrade."+ AuxiliaUtilities.MODID +"." + this.name().toLowerCase());
  }
}
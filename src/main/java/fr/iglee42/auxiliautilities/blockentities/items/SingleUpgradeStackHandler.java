package fr.iglee42.auxiliautilities.blockentities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.UpgradeProvider;
import fr.iglee42.auxiliautilities.menu.widgets.api.TransferPriority;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.utils.Upgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class SingleUpgradeStackHandler extends SingleItemStackHandler {

    private final EnumSet<Upgrade> acceptedUpgrades;

    public SingleUpgradeStackHandler(EnumSet<Upgrade> acceptedUpgrades) {
        this.acceptedUpgrades = acceptedUpgrades;
    }

    public SingleUpgradeStackHandler(Upgrade... acceptedUpgrades) {
        this(EnumSet.copyOf(List.of(acceptedUpgrades)));
    }

    public SlotItemHandlerWidget getSpeedSlot(int x,int y){
        return new SlotSpeedUpgradeHandler(x,y);
    }

    public int getLevel(Upgrade upgrade){
        if (!acceptedUpgrades.contains(upgrade)) return 0;
        ItemStack stack = getStack();
        if (stack.isEmpty()) return 0;
        Item it = stack.getItem();
        if (!(it instanceof UpgradeProvider provider)) return 0;
        Upgrade itemUpgrade = provider.getUpgrade(stack);
        if (itemUpgrade != upgrade) return 0;
        return Math.min(stack.getCount(), upgrade.getMaxLevel());
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        if (!(stack.getItem() instanceof UpgradeProvider provider)) return 0;
        Upgrade itemUpgrade = provider.getUpgrade(stack);
        if (itemUpgrade == null) return 0;
        if (!acceptedUpgrades.contains(itemUpgrade)) return 0;
        return itemUpgrade.getMaxLevel();
    }

    private class SlotSpeedUpgradeHandler extends SlotItemHandlerWidget implements TransferPriority {

        public SlotSpeedUpgradeHandler( int x, int y) {
            super(SingleUpgradeStackHandler.this, 0, x, y);
            setBackground(InventoryMenu.BLOCK_ATLAS, AuxiliaUtilities.id("item/upgrade_speed_skeleton"));
        }

        @Override
        public @NotNull List<Component> getTooltips() {
            if (SingleUpgradeStackHandler.this.getStack().isEmpty())
                return List.of(AULang.SPEED_UPGRADES_TOOLTIP.get());
            return super.getTooltips();
        }

        @Override
        public int getPriority() {
            return 1;
        }
    }
}

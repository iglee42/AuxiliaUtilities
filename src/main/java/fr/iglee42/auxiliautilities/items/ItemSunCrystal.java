package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class ItemSunCrystal extends AUItem {
    public static final int MAX_DAMAGE = 250;
    public ItemSunCrystal(Properties props) {
        super(props.durability(MAX_DAMAGE).component(DataComponents.DAMAGE,MAX_DAMAGE));
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {
        super.addToTab(acceptor);
        acceptor.accept(getFullStack());
    }

    public static ItemStack getFullStack() {
        ItemStack stack = new ItemStack(AUItems.SUN_CRYSTAL.get());
        stack.setDamageValue(0);
        return stack;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        int damageValue = stack.getDamageValue();
        if (damageValue > 0) {
            Level level = entity.level();
            if (!level.isBrightOutside() || !level.canSeeSky(entity.blockPosition()) || !level.dimensionType().hasSkyLight()) return super.onEntityItemUpdate(stack,entity);
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 1){
            int damageValue = stack.getDamageValue();
            float ratio = 1-(float) damageValue / MAX_DAMAGE;
            return (((int)(ratio * 255)) << 24) | (0xFF << 16) | (0xFF << 8) | 0xFF;
        }
        return super.getColor(stack, tintIndex);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.getDamageValue() == MAX_DAMAGE)
            return AULang.SUN_CRYSTAL_EMPTY.get();
        return super.getName(stack);
    }
}

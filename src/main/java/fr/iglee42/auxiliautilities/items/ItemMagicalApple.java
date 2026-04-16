package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.mixins.PlayerAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ItemMagicalApple extends AUItem {

    private static final FoodProperties FOOD_PROPS = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.2F)
            .alwaysEdible()
            .build();

    public ItemMagicalApple(Properties props) {
        super(props.food(FOOD_PROPS));
    }


    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player){
            ((PlayerAccessor) player).setEnchantmentSeed(level.random.nextInt());
            AULang.MAGICAL_APPLE_USE.sendToPlayer(player, UUID.randomUUID());
            if (player.containerMenu instanceof EnchantmentMenu)
                player.closeContainer();
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }
}

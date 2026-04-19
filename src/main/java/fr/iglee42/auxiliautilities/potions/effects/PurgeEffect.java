package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jetbrains.annotations.Nullable;

public class PurgeEffect extends AUMobEffect {
    public PurgeEffect() {
        super(MobEffectCategory.HARMFUL, 16738816);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(ServerLevel level, @Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entity, int amplifier, double health) {
        if (entity instanceof Player player){
            FoodData foodData = player.getFoodData();
            CompoundTag tag = new CompoundTag();
            if (entity == source){
                foodData.setFoodLevel( 0);
                foodData.setSaturation(0f);
            } else {
                foodData.setFoodLevel(Math.max(0, (foodData.getFoodLevel() + 1 ) / 2));
                foodData.setSaturation(0f);
            }
        }
    }
}

package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import net.minecraft.nbt.CompoundTag;
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
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entity, int amplifier, double health) {
        if (entity instanceof Player player){
            FoodData foodData = player.getFoodData();
            CompoundTag tag = new CompoundTag();
            foodData.addAdditionalSaveData(tag);
            if (entity == source){
                tag.putInt("foodLevel", 0);
                tag.putFloat("foodSaturationLevel", 0f);
            } else {
                tag.putInt("foodLevel", Math.max(0, (foodData.getFoodLevel() + 1 ) / 2));
                tag.putFloat("foodSaturationLevel", 0f);
            }
            foodData.readAdditionalSaveData(tag);
        }
    }
}

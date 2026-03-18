package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class GreekFireEffect extends AUMobEffect {

    public GreekFireEffect() {
        super(MobEffectCategory.HARMFUL, 16732160);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int p_295167_) {
        return (duration % 20) == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int p_19468_) {
        if (!entity.fireImmune())
            entity.igniteForSeconds(15);
        return super.applyEffectTick(entity, p_19468_);
    }
}

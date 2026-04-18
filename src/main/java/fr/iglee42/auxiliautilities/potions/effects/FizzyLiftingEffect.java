package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FizzyLiftingEffect extends AUMobEffect {
    public FizzyLiftingEffect() {
        super(MobEffectCategory.HARMFUL, 10547296);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level,LivingEntity entity, int amplifier) {
        entity.setOnGround(false);
        entity.fallDistance = 0.0f;
        if (entity.getDeltaMovement().y() < 0.0D){
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1,0.5D,1));
        }
        if (entity.isCrouching()){
            entity.setDeltaMovement(entity.getDeltaMovement().add(0,0.03D,0));
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0,0.06D,0));
        }

        if (entity.isInLiquid())
            entity.setDeltaMovement(entity.getDeltaMovement().add(0,0.2D,0));

        return super.applyEffectTick(level,entity, amplifier);
    }
}

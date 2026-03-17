package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GravityEffect extends AUMobEffect {
    public GravityEffect() {
        super(MobEffectCategory.HARMFUL, 2105376);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int p_19468_) {
        if (entity instanceof Player player){
            if (player.getAbilities().flying){
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
            if (entity.getDeltaMovement().y() > 0.0D){
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1,1D,1));
            }
        }

        double dist = 5.0D;
        Vec3 pos = entity.position();
        Vec3 pos1 = pos.add(0.0D,-dist, 0.0D);
        BlockHitResult result = entity.level().clip(new ClipContext(pos, pos1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (result != null && result.getType() != HitResult.Type.MISS){
            Vec3 hit = result.getLocation();
            dist = hit.distanceTo(pos);
        }
        if (!entity.onGround())
            entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.04D * dist, 0));
        return super.applyEffectTick(entity, p_19468_);
    }
}

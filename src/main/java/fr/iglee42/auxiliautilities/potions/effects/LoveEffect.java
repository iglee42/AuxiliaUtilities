package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LoveEffect extends AUMobEffect {
    public LoveEffect() {
        super(MobEffectCategory.BENEFICIAL, 16761051);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    private void fallInLove(@Nullable Entity indirectSource, Animal animal ){
        Player player = (indirectSource instanceof Player p) ? p : null;
        if (animal.getAge() == 0 && !animal.isInLove()) {
            animal.setInLove(player);
        }
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(ServerLevel level, @Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entity, int amplifier, double health) {
        if (entity.level().isClientSide){
            RandomSource randomSource = entity.level().getRandom();
            double xd = randomSource.nextGaussian() * 0.02d,yd = randomSource.nextGaussian() * 0.02d,zd = randomSource.nextGaussian() * 0.02d;
            entity.level().addParticle(
                    ParticleTypes.HEART,
                    entity.getX() + (randomSource.nextFloat() * entity.getBbWidth() * 2) - entity.getBbWidth(),
                    entity.getY() + 0.5d + (randomSource.nextFloat() * entity.getBbHeight()),
                    entity.getZ() + (randomSource.nextFloat() * entity.getBbWidth() * 2) - entity.getBbWidth(),
                    xd,yd,zd
            );
        } else {
            if (entity instanceof Animal animal)
                fallInLove(indirectSource, animal);

            if (entity instanceof TamableAnimal animal)
                if (indirectSource instanceof Player player && animal.getOwnerUUID() == null)
                    animal.tame(player);
        }
    }
}

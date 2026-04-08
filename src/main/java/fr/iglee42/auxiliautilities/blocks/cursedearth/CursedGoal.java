package fr.iglee42.auxiliautilities.blocks.cursedearth;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class CursedGoal extends Goal {

    private final Mob mob;
    private int timer;

    public CursedGoal(Mob mob, int timer) {
        this.mob = mob;
        this.timer = timer;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void tick() {

        if (mob.level().getGameTime() % 20 != 0)
            return;

        if (timer <= 0) {
            explodeParticles();
            mob.discard();
            return;
        }

        timer--;
        mob.getPersistentData().putInt("CursedEarth", timer);
    }

    private void explodeParticles() {

        RandomSource rand = mob.level().random;
        if (mob.level().isClientSide()) return;

        for (int i = 0; i < 20; i++) {

            ((ServerLevel)mob.level()).sendParticles(
                    ParticleTypes.EXPLOSION,
                    mob.getX() + rand.nextGaussian(),
                    mob.getY() + rand.nextGaussian(),
                    mob.getZ() + rand.nextGaussian(),
                    1,
                    0, 0, 0,0.001F
            );
        }
    }
}
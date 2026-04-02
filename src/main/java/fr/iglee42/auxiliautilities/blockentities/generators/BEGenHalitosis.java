package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BEGenHalitosis extends AUGeneratorBlockEntity{
    public BEGenHalitosis(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.HALITOSIS_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item, T> getItemDataMapType() {
        return (DataMapType<Item, T>) AUGeneratorsDataMaps.HALITOSIS_ITEMS;
    }

    @Override
    protected void burnTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.burnTick(level, pos, state);
        if (level.getDifficulty() == Difficulty.PEACEFUL)
            return;

        RandomSource rand = level.random;

        if (rand.nextInt(1200) == 0) {

            double x = pos.getX() + 0.5D + rand.nextGaussian() * 3.0D;
            double y = pos.getY() + 0.5D + rand.nextGaussian() * 3.0D;
            double z = pos.getZ() + 0.5D + rand.nextGaussian() * 3.0D;

            Endermite endermite = EntityType.ENDERMITE.create(level);
            if (endermite == null) return;

            float yaw = Mth.wrapDegrees(rand.nextFloat() * 360.0F);

            endermite.moveTo(x, y, z, yaw, 0.0F);
            endermite.yHeadRot = yaw;
            endermite.yBodyRot = yaw;

            endermite.finalizeSpawn(
                    (ServerLevel) level,
                    level.getCurrentDifficultyAt(BlockPos.containing(x, y, z)),
                    MobSpawnType.NATURAL,
                    null
            );

            if (endermite.checkSpawnRules(level, MobSpawnType.NATURAL) && endermite.checkSpawnObstruction(level)) {

                level.addFreshEntity(endermite);
                endermite.spawnAnim();

                /*NetworkHandler.sendToAllAround(
                        new PacketParticleSplineCurve(
                                Vec3.atCenterOf(pos),
                                endermite.position(),
                                VecHelper.randUnitVec(rand),
                                VecHelper.randUnitVec(rand),
                                -65281
                        ),
                        level.dimension(),
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        32.0D
                );*/

            } else {
                endermite.discard();
            }
        }
    }


    @Override
    protected int getEnergyStorageCapacity() {
        return 500_000;
    }

}

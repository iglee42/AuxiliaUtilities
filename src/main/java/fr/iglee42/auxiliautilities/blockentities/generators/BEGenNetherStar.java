package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BEGenNetherStar extends AUGeneratorBlockEntity{
    public BEGenNetherStar(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.NETHER_STAR_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item, T> getItemDataMapType() {
        return (DataMapType<Item, T>) AUGeneratorsDataMaps.NETHER_STAR_ITEMS;
    }

    @Override
    protected void burnTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.burnTick(level, pos, state);
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(5))) {
            if (!(entity instanceof Endermite)){
                entity.addEffect(new MobEffectInstance(
                        MobEffects.WITHER,
                        410,
                        (int) Math.floor(Math.sqrt(6)) - 1
                ));
            }
        }
    }

    @Override
    protected int getEnergyStorageCapacity() {
        return 1_000_000;
    }

    @Override
    protected void clientTick(Level level, BlockPos pos, BlockState state) {
        super.clientTick(level, pos, state);
        if (burnTime > 0) {
            for (int i = 0; i < 4; i++) {
                AABB radius = new AABB(pos).inflate(5);
                double x = radius.minX + (radius.maxX - radius.minX) * level.random.nextFloat();
                double y = radius.minY + (radius.maxY - radius.minY) * level.random.nextFloat();
                double z = radius.minZ + (radius.maxZ - radius.minZ) * level.random.nextFloat();
                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.1f, 0.1f, 0.1f), x, y, z, 0, 0.05, 0);
            }
        }
    }
}

package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.potions.AUMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BEGenDeath extends AUGeneratorBlockEntity {
    public BEGenDeath(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.DEATH_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item, T> getItemDataMapType() {
        return (DataMapType<Item, T>) AUGeneratorsDataMaps.DEATH_ITEMS;
    }

    @Override
    protected void burnTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.burnTick(level, pos, state);
        for (Player player : level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(1))) {
            if (!player.hasEffect(AUMobEffects.DOOM)) {
                player.addEffect(new MobEffectInstance(AUMobEffects.DOOM, 1200, 0, true, true, true));
            }
        }
    }

    @Override
    protected void clientTick(Level level, BlockPos pos, BlockState state) {
        super.clientTick(level, pos, state);
        if (burnTime > 0) {
            for (int i = 0; i < 4; i++) {
                AABB radius = new AABB(pos).inflate(1);
                double x = radius.minX + (radius.maxX - radius.minX) * level.random.nextFloat();
                double y = radius.minY + (radius.maxY - radius.minY) * level.random.nextFloat();
                double z = radius.minZ + (radius.maxZ - radius.minZ) * level.random.nextFloat();
                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7f, 0.1f, 0.1f), x, y, z, 0, 0.05, 0);
            }
        }
    }

    public enum DefaultDeathGeneratorItems {
        ROTTEN_FLESH(Items.ROTTEN_FLESH, 20, 20),
        BONE(Items.BONE, 40, 20),
        SPIDER_EYE(Items.SPIDER_EYE, 60, 20),
        GHAST_TEAR(Items.GHAST_TEAR, 80, 20),
        BREEZE_ROD(Items.BREEZE_ROD, 100, 20),
        ECHO_SHARD(Items.ECHO_SHARD, 125, 20),
        WITHER_SKELETON_SKULL(Items.WITHER_SKELETON_SKULL, 150, 20),
        ;

        private final ItemLike item;
        private final int energyPerTick;
        private final int time;

        DefaultDeathGeneratorItems(ItemLike item, int energyPerTick, int time) {
            this.item = item;
            this.energyPerTick = energyPerTick;
            this.time = time * 20;
        }

        public int getEnergyPerTick() {
            return energyPerTick;
        }

        public int getTime() {
            return time;
        }

        public ItemLike getItem() {
            return item;
        }
    }

}

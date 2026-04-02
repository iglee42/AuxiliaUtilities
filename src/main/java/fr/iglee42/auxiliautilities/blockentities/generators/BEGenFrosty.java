package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BEGenFrosty extends AUGeneratorBlockEntity{
    public BEGenFrosty(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.FROSTY_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item, T> getItemDataMapType() {
        return (DataMapType<Item, T>) AUGeneratorsDataMaps.FROSTY_ITEMS;
    }

    @Override
    protected void burnTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.burnTick(level, pos, state);
        BlockPos.MutableBlockPos newPos = new BlockPos.MutableBlockPos();

        int dist = 9;
        RandomSource rand = level.random;

            if (rand.nextInt(1024) == 0) {

                for (int i1 = 0; i1 < 40; i1++) {

                    double d;

                    do {
                        newPos.set(
                                pos.getX() + rand.nextInt(19) - dist,
                                pos.getY() + rand.nextInt(19) - dist,
                                pos.getZ() + rand.nextInt(19) - dist
                        );

                        d = newPos.distSqr(pos);

                    } while (d > 81.0D);

                    BlockState newstate = level.getBlockState(newPos);
                    Block block = newstate.getBlock();

                    if (newstate.isAir()) {

                        if (Blocks.SNOW.defaultBlockState().canSurvive(level, newPos)) {
                            level.setBlock(newPos, Blocks.SNOW.defaultBlockState(), 3);
                            break;
                        }

                    }
                    else if (newstate.getFluidState().isSource() &&
                            (block == Blocks.WATER)) {

                        level.setBlock(newPos, Blocks.ICE.defaultBlockState(), 3);
                        break;

                    }
                    else if (block == Blocks.SNOW) {

                        int value = newstate.getValue(SnowLayerBlock.LAYERS);

                        if (value < 8 && (9.0D - Math.sqrt(d)) * 1.25D > value) {

                            level.setBlock(
                                    newPos,
                                    newstate.setValue(SnowLayerBlock.LAYERS, value + 1),
                                    3
                            );
                            break;
                        }
                    }
                }
            }
    }

    public enum DefaultFrostyGeneratorItems {
        ICE(Items.ICE, 40, 2),
        PACKED_ICE(Items.PACKED_ICE, 40, 18),
        BLUE_ICE(Items.BLUE_ICE, 40, 162),
        SNOWBALL(Items.SNOWBALL, 40, 0.25),
        SNOW_BLOCK(Items.SNOW_BLOCK, 40, 1),
        SNOW(Items.SNOW, 40, 0.15),
        ;

        private final ItemLike item;
        private final int energyPerTick;
        private final int time;

        DefaultFrostyGeneratorItems(ItemLike item, int energyPerTick, double time) {
            this.item = item;
            this.energyPerTick = energyPerTick;
            this.time = (int) (time * 20);
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

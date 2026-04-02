package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BEGenExplosive extends AUGeneratorBlockEntity {
    public BEGenExplosive(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.EXPLOSIVE_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item, T> getItemDataMapType() {
        return (DataMapType<Item, T>) AUGeneratorsDataMaps.EXPLOSIVE_ITEMS;
    }

    @Override
    protected void burnTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.burnTick(level, pos, state);
        RandomSource rand = level.random;
        if (rand.nextInt(41) == 0) {
            for (int i = 0; i < 10; i++) {
                double v = 1.5D;
                double x = pos.getX() + 0.5D + v * rand.nextGaussian();
                double y = pos.getY() + 0.5D + v * rand.nextGaussian();
                double z = pos.getZ() + 0.5D + v * rand.nextGaussian();
                BlockPos otherPos = BlockPos.containing(x, y, z);
                if (i == 9 || !pos.equals(otherPos)) {
                    BlockState otherState = level.getBlockState(otherPos);
                    if (i >= 5 || otherState.getDestroySpeed(level, otherPos) == 0.0F) {
                        level.explode(
                                null,
                                x, y, z,
                                2.0F,
                                Level.ExplosionInteraction.NONE
                        );
                        return;
                    }
                }
            }
        }
    }


    public enum DefaultExplosiveGeneratorItems {
        TNT(Items.TNT, 160, 160),
        GUNPOWDER(Items.GUNPOWDER, 160, 20);

        private final ItemLike item;
        private final int energyPerTick;
        private final int time;

        DefaultExplosiveGeneratorItems(ItemLike item, int energyPerTick, int time) {
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

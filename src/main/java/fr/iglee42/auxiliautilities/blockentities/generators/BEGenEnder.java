package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class BEGenEnder extends AUGeneratorBlockEntity{
    public BEGenEnder(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.ENDER_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item, T> getItemDataMapType() {
        return (DataMapType<Item, T>) AUGeneratorsDataMaps.ENDER_ITEMS;
    }

    public enum DefaultEnderGeneratorItems {
        ENDER_PEARL(Tags.Items.ENDER_PEARLS, 1600, 40),
        ENDER_EYE(Items.ENDER_EYE, 3200, 80);

        private ItemLike item = null;
        private TagKey<Item> tag = null;
        private final int progress;
        private final int energyPerProgress;

        DefaultEnderGeneratorItems(ItemLike item, int progress, int energyPerProgress) {
            this.item = item;
            this.progress = progress;
            this.energyPerProgress = energyPerProgress;
        }

        DefaultEnderGeneratorItems(TagKey<Item> tag,int progress, int energyPerProgress) {
            this.tag = tag;
            this.progress = progress;
            this.energyPerProgress = energyPerProgress;
        }

        public ItemLike getItem() {
            return item;
        }

        public TagKey<Item> getTag() {
            return tag;
        }

        public int getProgress() {
            return progress;
        }

        public int getEnergyPerProgress() {
            return energyPerProgress;
        }

    }
}

package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AUBlocksTagsProvider extends BlockTagsProvider {


    public AUBlocksTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AuxiliaUtilities.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {

        tag(Tags.Blocks.STORAGE_BLOCKS).addTags(AUTags.Blocks.DEMON_BLOCKS,AUTags.Blocks.ENCHANTED_BLOCKS,AUTags.Blocks.EVIL_INFUSED_IRON_BLOCKS);

        tag(AUTags.Blocks.DEMON_BLOCKS).add(AUBlocks.DEMON_BLOCK.get());
        tag(AUTags.Blocks.ENCHANTED_BLOCKS).add(AUBlocks.ENCHANTED_BLOCK.get());
        tag(AUTags.Blocks.EVIL_INFUSED_IRON_BLOCKS).add(AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AUBlocks.DEMON_BLOCK.get(),AUBlocks.ENCHANTED_BLOCK.get(),AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(AUBlocks.DEMON_BLOCK.get(),AUBlocks.ENCHANTED_BLOCK.get(),AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(AUBlocks.DEMON_BLOCK.get(),AUBlocks.ENCHANTED_BLOCK.get(),AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS){
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(set.getBlock().get(),set.getStairs().get(),set.getSlab().get(),set.getWall().get());
            tag(BlockTags.NEEDS_STONE_TOOL).add(set.getBlock().get(),set.getStairs().get(),set.getSlab().get(),set.getWall().get());
            tag(BlockTags.STAIRS).add(set.getStairs().get());
            tag(BlockTags.SLABS).add(set.getSlab().get());
            tag(BlockTags.WALLS).add(set.getWall().get());
        }
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AUBlocks.MANUAL_MILL.get(),AUBlocks.CREATIVE_MILL.get(),AUBlocks.WIND_MILL.get(),AUBlocks.FIRE_MILL.get(),AUBlocks.WATER_MILL.get(),AUBlocks.LUNAR_PANEL.get(),AUBlocks.SOLAR_PANEL.get(),AUBlocks.LAVA_MILL.get(),AUBlocks.DRAGON_EGG_MILL.get(),AUBlocks.RESONATOR.get());
        tag(BlockTags.PLANKS).add(AUBlocks.MAGICAL_PLANKS.get());
        tag(BlockTags.DAMPENS_VIBRATIONS).add(AUBlocks.SOUND_MUFFLER.get());

        tag(BlockTags.CROPS).add(AUBlocks.ENDER_LILLY.get());
        tag(BlockTags.SWORD_EFFICIENT).add(AUBlocks.ENDER_LILLY.get());

    }
}

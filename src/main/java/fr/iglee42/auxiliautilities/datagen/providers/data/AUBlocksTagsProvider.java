package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.CompressedBlockSet;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
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

        tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(AUBlocks.MAGICAL_WOOD.get());

        addCompressedSet(BlockTags.MINEABLE_WITH_PICKAXE,AUBlocks.COMPRESSED_COBBLESTONE);
        addCompressedSet(Tags.Blocks.COBBLESTONES_NORMAL,AUBlocks.COMPRESSED_COBBLESTONE);
        addCompressedSet(BlockTags.MINEABLE_WITH_PICKAXE,AUBlocks.COMPRESSED_BLACKSTONE);
        addCompressedSet(BlockTags.MINEABLE_WITH_PICKAXE,AUBlocks.COMPRESSED_COBBLED_DEEPSLATE);
        addCompressedSet(Tags.Blocks.COBBLESTONES_DEEPSLATE,AUBlocks.COMPRESSED_COBBLED_DEEPSLATE);
        addCompressedSet(BlockTags.MINEABLE_WITH_PICKAXE,AUBlocks.COMPRESSED_END_STONE);
        addCompressedSet(BlockTags.DRAGON_IMMUNE,AUBlocks.COMPRESSED_END_STONE);
        addCompressedSet(Tags.Blocks.END_STONES,AUBlocks.COMPRESSED_END_STONE);
        addCompressedSet(BlockTags.MINEABLE_WITH_PICKAXE,AUBlocks.COMPRESSED_NETHERRACK);
        addCompressedSet(Tags.Blocks.NETHERRACKS,AUBlocks.COMPRESSED_NETHERRACK);
        addCompressedSet(BlockTags.INFINIBURN_OVERWORLD,AUBlocks.COMPRESSED_NETHERRACK);
        addCompressedSet(BlockTags.INFINIBURN_NETHER,AUBlocks.COMPRESSED_NETHERRACK);
        addCompressedSet(BlockTags.INFINIBURN_END,AUBlocks.COMPRESSED_NETHERRACK);
        addCompressedSet(BlockTags.MINEABLE_WITH_SHOVEL,AUBlocks.COMPRESSED_DIRT);
        addCompressedSet(BlockTags.DIRT,AUBlocks.COMPRESSED_DIRT);
        addCompressedSet(BlockTags.MINEABLE_WITH_SHOVEL,AUBlocks.COMPRESSED_GRAVEL);
        addCompressedSet(Tags.Blocks.GRAVELS,AUBlocks.COMPRESSED_GRAVEL);
        addCompressedSet(BlockTags.MINEABLE_WITH_SHOVEL,AUBlocks.COMPRESSED_SAND);
        addCompressedSet(BlockTags.SAND,AUBlocks.COMPRESSED_SAND);

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AUBlocks.ENDER_PORCUPINE.get(),AUBlocks.KLEIN_BOTTLE.get(),AUBlocks.STONE_DRUM.get(),AUBlocks.IRON_DRUM.get(),AUBlocks.REINFORCED_LARGE_DRUM.get(),AUBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(),AUBlocks.CREATIVE_DRUM.get());
    }

    private void addCompressedSet(TagKey<Block> tag,CompressedBlockSet set){
        for (int tier = 1; tier <= set.getMaxTier(); tier++) {
            tag(tag).add(set.getBlock(tier).get());
        }
    }
}

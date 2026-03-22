package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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

    }
}

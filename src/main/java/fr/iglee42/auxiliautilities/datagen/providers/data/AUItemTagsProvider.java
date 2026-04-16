package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.CompressedBlockSet;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.items.ItemSickle;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.theillusivec4.curios.api.CuriosTags;

import java.util.concurrent.CompletableFuture;

public class AUItemTagsProvider extends ItemTagsProvider {
    public AUItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<TagLookup<Block>> blocks) {
        super(output, registries, blocks);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        tag(AUTags.Items.UNSTABLE_INGOTS).add(AUItems.UNSTABLE_INGOT.asItem(),AUItems.STABLE_UNSTABLE_INGOT.asItem());
        tag(AUTags.Items.UNSTABLE_NUGGETS).add(AUItems.STABLE_UNSTABLE_NUGGET.asItem());

        tag(Tags.Items.NUGGETS).addTags(AUTags.Items.UNSTABLE_NUGGETS,AUTags.Items.DEMON_NUGGETS,AUTags.Items.ENCHANTED_NUGGETS,AUTags.Items.EVIL_INFUSED_IRON_NUGGETS);
        tag(Tags.Items.INGOTS).addTags(AUTags.Items.UNSTABLE_INGOTS,AUTags.Items.DEMON_INGOTS,AUTags.Items.ENCHANTED_INGOTS,AUTags.Items.EVIL_INFUSED_IRON_INGOTS);
        tag(Tags.Items.STORAGE_BLOCKS).addTags(AUTags.Items.DEMON_BLOCKS,AUTags.Items.ENCHANTED_BLOCKS,AUTags.Items.EVIL_INFUSED_IRON_BLOCKS);

        tag(ItemTags.DURABILITY_ENCHANTABLE).add(AUItems.GLASS_CUTTER.asItem());
        tag(ItemTags.BREAKS_DECORATED_POTS).add(AUItems.GLASS_CUTTER.asItem());

        tag(ItemTags.SWORDS).add(AUItems.LUX_SABER.asItem());
        tag(Tags.Items.MELEE_WEAPON_TOOLS).add(AUItems.LUX_SABER.asItem());
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(AUItems.LUX_SABER.asItem());
        tag(ItemTags.DURABILITY_ENCHANTABLE).remove(AUItems.LUX_SABER.asItem());
        
        tag(AUTags.Items.DEMON_BLOCKS).add(AUBlocks.DEMON_BLOCK.asItem());
        tag(AUTags.Items.ENCHANTED_BLOCKS).add(AUBlocks.ENCHANTED_BLOCK.asItem());
        tag(AUTags.Items.EVIL_INFUSED_IRON_BLOCKS).add(AUBlocks.EVIL_INFUSED_IRON_BLOCK.asItem());

        tag(AUTags.Items.DEMON_INGOTS).add(AUItems.DEMON_INGOT.asItem());
        tag(AUTags.Items.ENCHANTED_INGOTS).add(AUItems.ENCHANTED_INGOT.asItem());
        tag(AUTags.Items.EVIL_INFUSED_IRON_INGOTS).add(AUItems.EVIL_INFUSED_IRON_INGOT.asItem());

        tag(AUTags.Items.DEMON_NUGGETS).add(AUItems.DEMON_NUGGET.asItem());
        tag(AUTags.Items.ENCHANTED_NUGGETS).add(AUItems.ENCHANTED_NUGGET.asItem());
        tag(AUTags.Items.EVIL_INFUSED_IRON_NUGGETS).add(AUItems.EVIL_INFUSED_IRON_NUGGET.asItem());

        tag(Tags.Items.DYES_MAGENTA).add(AUItems.LUNAR_REACTIVE_DUST.asItem());

        for (Item sickle : AUItems.ITEMS.getEntries().stream().filter(e -> e.get() instanceof ItemSickle).map(DeferredHolder::get).toList()) {
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(sickle);
            tag(ItemTags.MINING_ENCHANTABLE).add(sickle);
            tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(sickle);
            tag(ItemTags.BREAKS_DECORATED_POTS).add(sickle);
            tag(AUTags.Items.SICKLES).add(sickle);
        }

        tag(Tags.Items.TOOLS).addTag(AUTags.Items.SICKLES);

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS){
            tag(ItemTags.STAIRS).add(set.getStairs().asItem());
            tag(ItemTags.SLABS).add(set.getSlab().asItem());
            tag(ItemTags.WALLS).add(set.getWall().asItem());
        }

        tag(AUTags.Items.WOODEN_STAIRS).add(Items.OAK_STAIRS,Items.SPRUCE_STAIRS,Items.BIRCH_STAIRS,Items.JUNGLE_STAIRS,Items.ACACIA_STAIRS,Items.DARK_OAK_STAIRS,Items.MANGROVE_STAIRS,Items.CHERRY_STAIRS,Items.BAMBOO_STAIRS,Items.CRIMSON_STAIRS,Items.WARPED_STAIRS,Items.BAMBOO_MOSAIC_STAIRS);
        tag(ItemTags.PLANKS).add(AUBlocks.MAGICAL_PLANKS.asItem());
        tag(ItemTags.DAMPENS_VIBRATIONS).add(AUBlocks.SOUND_MUFFLER.asItem());
        tag(Tags.Items.CROPS).add(AUBlocks.ENDER_LILLY.asItem(),AUBlocks.RED_ORCHID.asItem());

        addCompressedSet(Tags.Items.COBBLESTONES_NORMAL,AUBlocks.COMPRESSED_COBBLESTONE);
        addCompressedSet(Tags.Items.COBBLESTONES_DEEPSLATE,AUBlocks.COMPRESSED_COBBLED_DEEPSLATE);
        addCompressedSet(Tags.Items.END_STONES,AUBlocks.COMPRESSED_END_STONE);
        addCompressedSet(Tags.Items.NETHERRACKS,AUBlocks.COMPRESSED_NETHERRACK);
        addCompressedSet(ItemTags.DIRT,AUBlocks.COMPRESSED_DIRT);
        addCompressedSet(Tags.Items.GRAVELS,AUBlocks.COMPRESSED_GRAVEL);
        addCompressedSet(ItemTags.SAND,AUBlocks.COMPRESSED_SAND);

        tag(ItemTags.SWORDS).add(AUItems.KIKOKU.asItem());
        tag(Tags.Items.MELEE_WEAPON_TOOLS).add(AUItems.KIKOKU.asItem());
        tag(ItemTags.DURABILITY_ENCHANTABLE).remove(AUItems.KIKOKU.asItem());

        tag(Tags.Items.TOOLS_BOW).add(AUItems.COMPOUND_BOW.asItem());
        tag(ItemTags.DURABILITY_ENCHANTABLE).remove(AUItems.COMPOUND_BOW.asItem());
        tag(ItemTags.BOW_ENCHANTABLE).add(AUItems.COMPOUND_BOW.asItem());
        tag(Tags.Items.RANGED_WEAPON_TOOLS).add(AUItems.COMPOUND_BOW.asItem());

        tag(Tags.Items.GLASS_BLOCKS_COLORLESS).add(AUBlocks.ETHEREAL_GLASS.asItem(),AUBlocks.INEFFABLE_GLASS.asItem(),AUBlocks.REVERSE_ETHEREAL_GLASS.asItem(),AUBlocks.THICKENED_GLASS.asItem(),AUBlocks.THICKENED_GLASS_BORDERED.asItem(),AUBlocks.THICKENED_GLASS_PATTERNED.asItem());
        tag(Tags.Items.GLASS_BLOCKS).add(AUBlocks.ETHEREAL_GLASS.asItem(),AUBlocks.INEFFABLE_GLASS.asItem(),AUBlocks.OBSIDIAN_GLASS.asItem(),AUBlocks.DARK_INEFFABLE_GLASS.asItem(),AUBlocks.GLOWING_GLASS.asItem(),AUBlocks.REDSTONE_GLASS.asItem(),AUBlocks.DARK_GLASS.asItem(),AUBlocks.REVERSE_ETHEREAL_GLASS.asItem(),AUBlocks.THICKENED_GLASS.asItem(),AUBlocks.THICKENED_GLASS_BORDERED.asItem(),AUBlocks.THICKENED_GLASS_PATTERNED.asItem());
        tag(Tags.Items.GLASS_BLOCKS_TINTED).add(AUBlocks.DARK_INEFFABLE_GLASS.asItem(),AUBlocks.DARK_GLASS.asItem());

        tag(AUTags.Items.DIVISION_SIGILS).add(AUItems.DIVISION_SIGIL.asItem(),AUItems.PSEUDO_INVERSION_SIGIL.asItem());

        tag(CuriosTags.RING).add(AUItems.ANGEL_RING.asItem());
    }

    private void addCompressedSet(TagKey<Item> tag, CompressedBlockSet set){
        for (int tier = 1; tier <= set.getMaxTier(); tier++) {
            tag(tag).add(set.getBlock(tier).asItem());
        }
    }
}

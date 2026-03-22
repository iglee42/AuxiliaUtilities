package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

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
    }
}

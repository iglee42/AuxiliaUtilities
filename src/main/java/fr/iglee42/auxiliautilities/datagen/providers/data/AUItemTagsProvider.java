package fr.iglee42.auxiliautilities.datagen.providers.data;

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

        tag(Tags.Items.NUGGETS).addTag(AUTags.Items.UNSTABLE_NUGGETS);
        tag(Tags.Items.INGOTS).addTag(AUTags.Items.UNSTABLE_INGOTS);

        tag(ItemTags.DURABILITY_ENCHANTABLE).add(AUItems.GLASS_CUTTER.asItem());
        tag(ItemTags.BREAKS_DECORATED_POTS).add(AUItems.GLASS_CUTTER.asItem());

        tag(ItemTags.SWORDS).add(AUItems.LUX_SABER.asItem());
        tag(Tags.Items.MELEE_WEAPON_TOOLS).add(AUItems.LUX_SABER.asItem());
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).remove(AUItems.LUX_SABER.asItem());
        tag(ItemTags.DURABILITY_ENCHANTABLE).remove(AUItems.LUX_SABER.asItem());
    }
}

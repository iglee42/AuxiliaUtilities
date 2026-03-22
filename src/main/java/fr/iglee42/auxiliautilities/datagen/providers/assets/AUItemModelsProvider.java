package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.items.AUItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.CompositeModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class AUItemModelsProvider extends ItemModelProvider {
    public AUItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AuxiliaUtilities.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldTool(AUItems.BUILDERS_WAND);
        handheldTool(AUItems.CREATIVE_BUILDERS_WAND);
        handheldTool(AUItems.DESTRUCTION_WAND);
        handheldTool(AUItems.CREATIVE_DESTRUCTION_WAND);
        handheldTool(AUItems.GLASS_CUTTER);
        handheldTool(AUItems.WATERING_CAN);

        getBuilder(AUItems.UNSTABLE_INGOT.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/unstable_ingot"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/unstable_ingot_inner"))
                .renderType("translucent");

        getBuilder(AUItems.STABLE_UNSTABLE_INGOT.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/unstable_ingot"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/unstable_ingot_inner"))
                .renderType("translucent");

        getBuilder(AUItems.STABLE_UNSTABLE_NUGGET.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/unstable_nugget"));

        simpleBlockItem(AUBlocks.ANGEL_BLOCK.get());
        simpleBlockItem(AUBlocks.DEMON_BLOCK.get());
        simpleBlockItem(AUBlocks.ENCHANTED_BLOCK.get());
        simpleBlockItem(AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());

        getBuilder(AUItems.SUN_CRYSTAL.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/sun_crystal"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/sun_crystal_inner"))
                .renderType("translucent");

        makeEnderShards();

        basicItem(AUItems.RESONATING_REDSTONE_CRYSTAL.asItem());
        basicItem(AUItems.REDSTONE_GEAR.asItem());
        basicItem(AUItems.EYE_OF_REDSTONE.asItem());
        basicItem(AUItems.LUNAR_REACTIVE_DUST.asItem());
        basicItem(AUItems.RED_COAL.asItem());
        basicItem(AUItems.MOON_STONE.asItem());
        basicItem(AUItems.DEMON_INGOT.asItem());
        basicItem(AUItems.ENCHANTED_INGOT.asItem());
        basicItem(AUItems.EVIL_INFUSED_IRON_INGOT.asItem());
        basicItem(AUItems.DEMON_NUGGET.asItem());
        basicItem(AUItems.ENCHANTED_NUGGET.asItem());
        basicItem(AUItems.EVIL_INFUSED_IRON_NUGGET.asItem());

        handheldTool(AUItems.WOODEN_SICKLE);
        handheldTool(AUItems.STONE_SICKLE);
        handheldTool(AUItems.IRON_SICKLE);
        handheldTool(AUItems.GOLDEN_SICKLE);
        handheldTool(AUItems.DIAMOND_SICKLE);
        handheldTool(AUItems.NETHERITE_SICKLE);

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS){
            simpleBlockItem(set.getBlock().get());
            simpleBlockItem(set.getStairs().get());
            simpleBlockItem(set.getSlab().get());
            getBuilder(set.getWall().getId().toString())
                    .parent(new ModelFile.ExistingModelFile(mcLoc("block/wall_inventory"),existingFileHelper))
                    .texture("wall", AuxiliaUtilities.id("block/"+set.getName()));
        }

        simpleBlockItem(AUBlocks.CREATIVE_MILL.get());

    }

    private void handheldTool(DeferredItem<? extends Item> item){
        getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/tools/"+item.getId().getPath()));
    }

    private void makeEnderShards(){
        ItemModelBuilder builder = getBuilder(AUItems.ENDER_SHARD.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/ender_shards/ender_shard_1"));

        for (int i = 1; i < 9; i++) {
            builder.override()
                    .predicate(AuxiliaUtilities.id("shards"), i)
                    .model(new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("item/ender_shards/ender_shard_"+i)));

            getBuilder("item/ender_shards/ender_shard_"+i)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/ender_shards/ender_shard_"+i));
        }
    }

}

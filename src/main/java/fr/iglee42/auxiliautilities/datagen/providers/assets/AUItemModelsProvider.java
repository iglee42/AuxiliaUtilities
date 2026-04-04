package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.CompressedBlockSet;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.items.AUItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        simpleBlockItem(AUBlocks.SOLAR_PANEL.get());
        simpleBlockItem(AUBlocks.LUNAR_PANEL.get());
        simpleBlockItem(AUBlocks.WATER_MILL.get());
        simpleBlockItem(AUBlocks.FIRE_MILL.get());
        simpleBlockItem(AUBlocks.LAVA_MILL.get());
        simpleBlockItem(AUBlocks.WIND_MILL.get());
        simpleBlockItem(AUBlocks.DRAGON_EGG_MILL.get());
        simpleBlockItem(AUBlocks.CREATIVE_MILL.get());
        simpleBlockItem(AUBlocks.RESONATOR.get());
        simpleBlockItem(AUBlocks.ENCHANTER.get());
        simpleBlockItem(AUBlocks.FURNACE.get());
        simpleBlockItem(AUBlocks.CRUSHER.get());
        simpleBlockItem(AUBlocks.MAGICAL_WOOD.get());
        simpleBlockItem(AUBlocks.MAGICAL_PLANKS.get());
        simpleBlockItem(AUBlocks.DIAGONAL_WOOD.get());
        simpleBlockItem(AUBlocks.SOUND_MUFFLER.get());
        simpleBlockItem(AUBlocks.MACHINE_BLOCK.get());
        basicItem(AUBlocks.ENDER_LILLY.asItem());

        basicItem(AUItems.UPGRADE_BASE.get());
        basicItem(AUItems.MAGICAL_APPLE.get());
        upgrade(AUItems.SPEED_UPGRADE);
        upgrade(AUItems.ENCHANTED_SPEED_UPGRADE);
        upgrade(AUItems.ULTIMATE_SPEED_UPGRADE);

        for (CompressedBlockSet set : CompressedBlockSet.ALL_SETS) {
            set.getBlocks().values().forEach(block-> simpleBlockItem(block.get()));
        }

        simpleBlockItem(AUBlocks.REDSTONE_CLOCK.get());

        simpleBlockItem(AUBlocks.SURVIVAL_GENERATOR.get());
        simpleBlockItem(AUBlocks.FURNACE_GENERATOR.get());
        simpleBlockItem(AUBlocks.OVERCLOCKED_GENERATOR.get());
        simpleBlockItem(AUBlocks.CULINARY_GENERATOR.get());
        simpleBlockItem(AUBlocks.MAGMATIC_GENERATOR.get());
        simpleBlockItem(AUBlocks.POTION_GENERATOR.get());
        simpleBlockItem(AUBlocks.SLIMEY_GENERATOR.get());
        simpleBlockItem(AUBlocks.DEATH_GENERATOR.get());
        simpleBlockItem(AUBlocks.PINK_GENERATOR.get());
        simpleBlockItem(AUBlocks.EXPLOSIVE_GENERATOR.get());
        simpleBlockItem(AUBlocks.HEATED_REDSTONE_GENERATOR.get());
        simpleBlockItem(AUBlocks.ENDER_GENERATOR.get());
        simpleBlockItem(AUBlocks.DISENCHANTMENT_GENERATOR.get());
        simpleBlockItem(AUBlocks.FROSTY_GENERATOR.get());
        simpleBlockItem(AUBlocks.HALITOSIS_GENERATOR.get());
        simpleBlockItem(AUBlocks.NETHER_STAR_GENERATOR.get());
        simpleBlockItem(AUBlocks.RAINBOW_GENERATOR.get());

        getBuilder(AUItems.RAINBOW_GENERATOR_BOTTOM.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("block/slab"))
                .texture("bottom", AuxiliaUtilities.id("block/rainbow_generator"))
                .texture("side", AuxiliaUtilities.id("block/rainbow_generator_bottom"))
                .texture("top", AuxiliaUtilities.id("block/rainbow_generator_center"));

        getBuilder(AUItems.RAINBOW_GENERATOR_TOP.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("item/slab_top")))
                .texture("bottom", AuxiliaUtilities.id("block/rainbow_generator_center"))
                .texture("side", AuxiliaUtilities.id("block/rainbow_generator_top"))
                .texture("top", AuxiliaUtilities.id("block/rainbow_generator"));
    }


    private void upgrade(DeferredItem<? extends Item> item){
        List<String> name = new ArrayList<>(Arrays.stream(item.getId().getPath().replace("_upgrade","").split("_")).toList());
        Collections.reverse(name);
        getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0",AuxiliaUtilities.id("item/upgrade_" + String.join("_",name)));
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

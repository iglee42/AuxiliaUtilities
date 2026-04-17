package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.CompressedBlockSet;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.client.layers.AngelRingRenderer;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import fr.iglee42.auxiliautilities.items.ItemAngelRing;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
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
        getBuilder(AUBlocks.RED_ORCHID.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("item/generated")))
                .texture("layer0", AuxiliaUtilities.id("block/plants/redorchid_seeds"));

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

        getBuilder(AUBlocks.MISERABLE_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.PATHETIC_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.MEDIOCRE_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.PASSABLE_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.DECENT_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.SOLID_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.GOOD_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.DAMN_GOOD_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.AMAZING_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.INSPIRING_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));
        getBuilder(AUBlocks.PERFECTED_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",mcLoc("block/iron_block"));

        getBuilder(AUBlocks.KLEIN_BOTTLE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")).texture("particle",modLoc("block/klein_lighting"));

        getBuilder(AUItems.FLAT_ITEM_TRANSFER_NODE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", FlatTransferNodeHandler.ITEM_NODE_SPRITE);
        getBuilder(AUItems.FLAT_FLUID_TRANSFER_NODE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0",FlatTransferNodeHandler.FLUID_NODE_SPRITE);

        getBuilder(AUItems.BIOME_MARKER.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/biome_marker"))
                .override()
                .predicate(AuxiliaUtilities.id("has_biome"), 1)
                .model(new ModelFile.UncheckedModelFile(
                        getBuilder(AUItems.BIOME_MARKER.getId() + "_full")
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/biome_marker_active"))
                                .texture("layer1", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/biome_marker_active_0"))
                                .texture("layer2", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/biome_marker_active_1"))
                                .texture("layer3", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/biome_marker_active_2"))
                                .texture("layer4", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/biome_marker_active_3"))
                                .renderType("cutout")
                                .getUncheckedLocation()
                ))
                .end()
                .renderType("cutout");

        getBuilder(AUItems.COMPOUND_BOW.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/tools/compound_bow"))
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .predicate(ResourceLocation.withDefaultNamespace("pull"), 1)
                .model(new ModelFile.UncheckedModelFile(handheldTool("compound_bow_pull_0").getUncheckedLocation()))
                .end()
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .predicate(ResourceLocation.withDefaultNamespace("pull"), 2f)
                .model(new ModelFile.UncheckedModelFile(handheldTool("compound_bow_pull_1").getUncheckedLocation()))
                .end()
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .predicate(ResourceLocation.withDefaultNamespace("pull"), 3f)
                .model(new ModelFile.UncheckedModelFile(handheldTool("compound_bow_pull_2").getUncheckedLocation()))
                .end()
                .override()
                .predicate(ResourceLocation.withDefaultNamespace("pulling"), 1)
                .predicate(ResourceLocation.withDefaultNamespace("pull"), 4f)
                .model(new ModelFile.UncheckedModelFile(handheldTool("compound_bow_pull_charged").getUncheckedLocation()))
                .end();


        simpleBlockItem(AUBlocks.STONE_DRUM.get());
        simpleBlockItem(AUBlocks.COPPER_DRUM.get());
        simpleBlockItem(AUBlocks.IRON_DRUM.get());
        simpleBlockItem(AUBlocks.REINFORCED_LARGE_DRUM.get());
        simpleBlockItem(AUBlocks.NETHERITE_DRUM.get());
        simpleBlockItem(AUBlocks.DEMONICALLY_GARGANTUAN_DRUM.get());
        simpleBlockItem(AUBlocks.CREATIVE_DRUM.get());

        getBuilder(AUItems.ITEM_FILTER.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AuxiliaUtilities.id("item/filter_items"));
        getBuilder(AUItems.FLUID_FILTER.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AuxiliaUtilities.id("item/filter_fluids"));

        simpleBlockItem(AUBlocks.ENDER_PORCUPINE.get());
        basicItem(AUItems.POWER_MANAGER.get());
        basicItem(AUItems.DROP_OF_EVIL.get());
        basicItem(AUItems.FLUID_DROPLET.get());
        basicItem(AUItems.ENERGY_DROPLET.get());

        simpleBlockItem(AUBlocks.DARK_GLASS.get());
        simpleBlockItem(AUBlocks.DARK_INEFFABLE_GLASS.get());
        simpleBlockItem(AUBlocks.INEFFABLE_GLASS.get());
        simpleBlockItem(AUBlocks.ETHEREAL_GLASS.get());
        simpleBlockItem(AUBlocks.REVERSE_ETHEREAL_GLASS.get());
        simpleBlockItem(AUBlocks.REDSTONE_GLASS.get());
        simpleBlockItem(AUBlocks.GLOWING_GLASS.get());
        simpleBlockItem(AUBlocks.OBSIDIAN_GLASS.get());
        simpleBlockItem(AUBlocks.THICKENED_GLASS.get());
        simpleBlockItem(AUBlocks.THICKENED_GLASS_BORDERED.get());
        simpleBlockItem(AUBlocks.THICKENED_GLASS_PATTERNED.get());
        simpleBlockItem(AUBlocks.SANDY_GLASS.get());

        simpleBlockItem(AUBlocks.CURSED_EARTH.get());

        getBuilder(AUItems.UNACTIVATED_DIVISION_SIGIL.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AuxiliaUtilities.id("item/division_sigil"));
        basicItem(AUItems.DIVISION_SIGIL.asItem());
        basicItem(AUItems.PSEUDO_INVERSION_SIGIL.asItem());

        simpleBlockItem(AUBlocks.TERRAFORMER.get());
        simpleBlockItem(AUBlocks.COOLER.get());
        simpleBlockItem(AUBlocks.HEATER.get());
        simpleBlockItem(AUBlocks.HUMIDIFIER.get());
        simpleBlockItem(AUBlocks.DEHUMIDIFIER.get());
        simpleBlockItem(AUBlocks.MAGIC_INFUSER.get());
        simpleBlockItem(AUBlocks.MAGIC_ABSORBER.get());
        simpleBlockItem(AUBlocks.DESHOSTILIFIER.get());
        simpleBlockItem(AUBlocks.ANTENNA.get());
        simpleBlockItem(AUBlocks.CLIMOGRAPH_BLOCK.get());

        ItemModelBuilder angelRingBuilder = getBuilder(AUItems.ANGEL_RING.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AuxiliaUtilities.id("item/tools/angel_ring"));
        for (int i = 1; i < ItemAngelRing.AngelRingWings.values().length; i++){
            ItemAngelRing.AngelRingWings wing = ItemAngelRing.AngelRingWings.values()[i];
            angelRingBuilder = angelRingBuilder.override()
                    .predicate(AuxiliaUtilities.id("wings"),i)
                    .model( getBuilder(AUItems.ANGEL_RING.getRegisteredName()+"_"+wing.name().toLowerCase())
                            .parent(new ModelFile.UncheckedModelFile("item/generated"))
                            .texture("layer0", AuxiliaUtilities.id("item/tools/angel_ring_"+wing.name().toLowerCase())))
                    .end();

            getBuilder(AuxiliaUtilities.id(wing.getSerializedName()+"_wing").toString())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", AuxiliaUtilities.id("item/wings/"+wing.name().toLowerCase()));
        }

        getBuilder(AUItems.FLYING_SQUID_RING.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AuxiliaUtilities.id("item/tools/flying_squid_ring"));

        getBuilder(AUItems.CHICKEN_RING.getRegisteredName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", AuxiliaUtilities.id("item/tools/chicken_ring"));

        handheldTool(AUItems.GOLDEN_LASSO)
                .override()
                .predicate(AuxiliaUtilities.id("has_entity"),1)
                .model(getBuilder(AuxiliaUtilities.id("golden_lasso_full").toString())
                        .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                        .texture("layer0",AuxiliaUtilities.id("item/tools/golden_lasso"))
                        .texture("layer1",AuxiliaUtilities.id("item/tools/lasso_internal_1"))
                        .texture("layer2",AuxiliaUtilities.id("item/tools/lasso_internal_2"))
                );
        handheldTool(AUItems.CURSED_LASSO)
                .override()
                .predicate(AuxiliaUtilities.id("has_entity"),1)
                .model(getBuilder(AuxiliaUtilities.id("cursed_lasso_full").toString())
                        .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                        .texture("layer0",AuxiliaUtilities.id("item/tools/cursed_lasso"))
                        .texture("layer1",AuxiliaUtilities.id("item/tools/lasso_internal_1"))
                        .texture("layer2",AuxiliaUtilities.id("item/tools/lasso_internal_2"))
                );
    }


    private void upgrade(DeferredItem<? extends Item> item){
        List<String> name = new ArrayList<>(Arrays.stream(item.getId().getPath().replace("_upgrade","").split("_")).toList());
        Collections.reverse(name);
        getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0",AuxiliaUtilities.id("item/upgrade_" + String.join("_",name)));
    }

    private ItemModelBuilder handheldTool(DeferredItem<? extends Item> item){
       return getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/tools/"+item.getId().getPath()));
    }

    private ModelBuilder<?> handheldTool(String texture){
        return getBuilder(AuxiliaUtilities.id(texture).toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/tools/"+texture));
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

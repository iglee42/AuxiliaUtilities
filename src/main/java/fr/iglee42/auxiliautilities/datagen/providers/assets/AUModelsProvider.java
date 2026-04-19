package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.*;
import fr.iglee42.auxiliautilities.client.models.properties.AngelRingWingsProp;
import fr.iglee42.auxiliautilities.client.models.properties.BiomeMarkerHasBiomeProp;
import fr.iglee42.auxiliautilities.client.models.properties.LassoHasEntityProp;
import fr.iglee42.auxiliautilities.client.models.properties.LuxSaberHasEnergyProp;
import fr.iglee42.auxiliautilities.client.models.tints.AUItemTintSource;
import fr.iglee42.auxiliautilities.datagen.builders.Quad2DLoaderBuilder;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import fr.iglee42.auxiliautilities.items.ItemAngelRing;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
import net.minecraft.client.renderer.item.properties.numeric.Count;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.*;

public class AUModelsProvider extends ModelProvider {

    private static final String MACHINE_SIDE = "machines/side";
    private static final String MACHINE_BOTTOM = "machines/bottom";

    private static final TextureSlot BASE = TextureSlot.create("base");

    public AUModelsProvider(PackOutput output) {
        super(output, AuxiliaUtilities.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createTrivialCube(AUBlocks.ANGEL_BLOCK.get());
        createBlockWithTexture(blockModels,AUBlocks.DEMON_BLOCK.get(),"demon_metal_block");
        createBlockWithTexture(blockModels,AUBlocks.ENCHANTED_BLOCK.get(),"enchanted_metal_block");
        blockModels.createTrivialCube(AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());
        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS) {
            //blockModels.createTrivialCube(set.getBlock().get());
            blockModels.family(set.getBlock().get())
                    .stairs(set.getStairs().get())
                    .slab(set.getSlab().get())
                    .wall(set.getWall().get());
        }

        createBlockWithModel(blockModels,AUBlocks.MANUAL_MILL.get(), "manual_mill_base");
        createBlockWithModel(blockModels,AUBlocks.SOLAR_PANEL);
        createBlockWithModel(blockModels,AUBlocks.LUNAR_PANEL);
        createBlockWithModel(blockModels,AUBlocks.FIRE_MILL);
        createBlockWithModel(blockModels,AUBlocks.WATER_MILL);
        createBlockWithModel(blockModels,AUBlocks.WIND_MILL);
        createBlockWithModel(blockModels,AUBlocks.LAVA_MILL);
        createBlockWithModel(blockModels,AUBlocks.DRAGON_EGG_MILL);
        createSimpleColumnBlock(blockModels,AUBlocks.CREATIVE_MILL.get(), "creative_mill_side","creative_mill_end");

        blockModels.createTrivialCube(AUBlocks.MAGICAL_WOOD.get());
        blockModels.createTrivialCube(AUBlocks.MAGICAL_PLANKS.get());
        blockModels.createTrivialCube(AUBlocks.DIAGONAL_WOOD.get());
        createBlockWithModel(blockModels,AUBlocks.RESONATOR);
        createBlockWithModel(blockModels,AUBlocks.ENDER_PORCUPINE);
        blockModels.createTrivialCube(AUBlocks.SOUND_MUFFLER.get());

        createPlant(blockModels,AUBlocks.ENDER_LILLY,BlockEnderLilly.AGE);
        createRedOrchid(blockModels);

        createOrientableMachine(blockModels,AUBlocks.FURNACE,MACHINE_SIDE,"machines/furnace_front",MACHINE_BOTTOM);
        createOrientableMachine(blockModels,AUBlocks.CRUSHER,MACHINE_SIDE,"machines/crusher_front",MACHINE_BOTTOM);
        createCubeTopBottomBlock(blockModels,AUBlocks.ENCHANTER.get(), "machines/enchanter_side","machines/enchanter_top",MACHINE_BOTTOM);
        createSimpleColumnBlock(blockModels,AUBlocks.MACHINE_BLOCK.get(), MACHINE_SIDE,MACHINE_BOTTOM);

        CompressedBlockSet.ALL_SETS.forEach(set->{
            for (int i = 0; i < set.getMaxTier(); i++) {
                int finalI = i;
                TexturedModel.Provider compressedProv = TexturedModel.CUBE.updateTexture(mapping->mapping.put(TextureSlot.ALL,AuxiliaUtilities.id("block/compressed/"+set.getName()+"/"+ finalI)));
                blockModels.createTrivialBlock(set.getBlock(finalI).get(),compressedProv);
            }
        });

        createRedstoneClock(blockModels);

        createGenerator(blockModels,AUBlocks.SURVIVAL_GENERATOR);
        createGenerator(blockModels,AUBlocks.FURNACE_GENERATOR);
        createGenerator(blockModels,AUBlocks.OVERCLOCKED_GENERATOR);
        createGenerator(blockModels,AUBlocks.CULINARY_GENERATOR);
        createGenerator(blockModels,AUBlocks.MAGMATIC_GENERATOR);
        createGenerator(blockModels,AUBlocks.POTION_GENERATOR);
        createGenerator(blockModels,AUBlocks.SLIMEY_GENERATOR);
        createGenerator(blockModels,AUBlocks.DEATH_GENERATOR);
        createGenerator(blockModels,AUBlocks.PINK_GENERATOR);
        createGenerator(blockModels,AUBlocks.EXPLOSIVE_GENERATOR);
        createGenerator(blockModels,AUBlocks.HEATED_REDSTONE_GENERATOR);
        createGenerator(blockModels,AUBlocks.ENDER_GENERATOR);
        createGenerator(blockModels,AUBlocks.DISENCHANTMENT_GENERATOR);
        createGenerator(blockModels,AUBlocks.FROSTY_GENERATOR);
        createGenerator(blockModels,AUBlocks.HALITOSIS_GENERATOR);
        createGenerator(blockModels,AUBlocks.NETHER_STAR_GENERATOR);
        blockModels.createTrivialCube(AUBlocks.RAINBOW_GENERATOR.get());

        createOpiniumCore(blockModels,AUBlocks.MISERABLE_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.PATHETIC_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.MEDIOCRE_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.PASSABLE_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.DECENT_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.SOLID_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.GOOD_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.DAMN_GOOD_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.AMAZING_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.INSPIRING_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.PERFECTED_OPINIUM_CORE);
        createOpiniumCore(blockModels,AUBlocks.KLEIN_BOTTLE);

        createDrum(blockModels,AUBlocks.STONE_DRUM);
        createDrum(blockModels,AUBlocks.COPPER_DRUM);
        createDrum(blockModels,AUBlocks.IRON_DRUM);
        createDrum(blockModels,AUBlocks.REINFORCED_LARGE_DRUM);
        createDrum(blockModels,AUBlocks.NETHERITE_DRUM);
        createDrum(blockModels,AUBlocks.CREATIVE_DRUM);
        createBlockWithModel(blockModels,AUBlocks.DEMONICALLY_GARGANTUAN_DRUM);

        createGlass(blockModels,AUBlocks.ETHEREAL_GLASS,"ethereal");
        createGlass(blockModels,AUBlocks.REVERSE_ETHEREAL_GLASS,"reverse_ethereal");
        createGlass(blockModels,AUBlocks.DARK_GLASS,"dark");
        createGlass(blockModels,AUBlocks.DARK_INEFFABLE_GLASS,"dark_ineffable");
        createGlass(blockModels,AUBlocks.INEFFABLE_GLASS,"ineffable");
        createGlass(blockModels,AUBlocks.OBSIDIAN_GLASS,"obsidian");
        createGlass(blockModels,AUBlocks.REDSTONE_GLASS,"redstone");
        createGlass(blockModels,AUBlocks.GLOWING_GLASS,"glowing");
        createGlass(blockModels,AUBlocks.THICKENED_GLASS,"thickened");
        createGlass(blockModels,AUBlocks.THICKENED_GLASS_BORDERED.get(),"thickened_glass_bordered");
        createGlass(blockModels,AUBlocks.THICKENED_GLASS_PATTERNED.get(),"thickened_glass_patterned");
        blockModels.createTrivialCube(AUBlocks.SANDY_GLASS.get());

        createSpike(blockModels,AUBlocks.WOODEN_SPIKE);
        createSpike(blockModels,AUBlocks.STONE_SPIKE);
        createSpike(blockModels,AUBlocks.COPPER_SPIKE);
        createSpike(blockModels,AUBlocks.IRON_SPIKE);
        createSpike(blockModels,AUBlocks.GOLDEN_SPIKE);
        createSpike(blockModels,AUBlocks.DIAMOND_SPIKE);
        createSpike(blockModels,AUBlocks.NETHERITE_SPIKE);
        createSpike(blockModels,AUBlocks.CREATIVE_SPIKE);


        TexturedModel.Provider cursedEarthProv = TexturedModel.createDefault(
                b->
                        new TextureMapping()
                                .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/cursed_earth_side"))
                                .put(TextureSlot.TOP,AuxiliaUtilities.id("block/cursed_earth"))
                                .put(TextureSlot.BOTTOM,ResourceLocation.withDefaultNamespace("block/dirt"))
                ,
                ModelTemplates.CUBE_BOTTOM_TOP
        );
        blockModels.createTrivialBlock(AUBlocks.CURSED_EARTH.get(), cursedEarthProv);

        createSimpleColumnBlock(blockModels,AUBlocks.CLIMOGRAPH_BLOCK.get(), "terraformer/terraformer_side","block/terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.TERRAFORMER,"terraformer/terraformer_side","terraformer/controller","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.COOLER,"terraformer/terraformer_side","terraformer/cooler","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.HEATER,"terraformer/terraformer_side","terraformer/heater","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.HUMIDIFIER,"terraformer/terraformer_side","terraformer/humidifier","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.DEHUMIDIFIER,"terraformer/terraformer_side","terraformer/dehumidifier","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.MAGIC_INFUSER,"terraformer/terraformer_side","terraformer/magic_infuser","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.MAGIC_ABSORBER,"terraformer/terraformer_side","terraformer/magic_absorber","terraformer/terraformer_base");
        createSimpleHorizontal(blockModels,AUBlocks.DESHOSTILIFIER,"terraformer/terraformer_side","terraformer/deshostilifier","terraformer/terraformer_base");
        createBlockWithModel(blockModels,AUBlocks.ANTENNA);


        // ITEMS

        handheldTool(itemModels,AUItems.BUILDERS_WAND);
        handheldTool(itemModels,AUItems.DESTRUCTION_WAND);
        ResourceLocation creativeBuilderModel = ModelTemplates.FLAT_HANDHELD_ITEM.create(AUItems.CREATIVE_BUILDERS_WAND.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/creative_builders_wand")),itemModels.modelOutput);
        ResourceLocation creativeBuilderOverlayModel = ModelTemplates.create(TextureSlot.TEXTURE).extend().customLoader(
                Quad2DLoaderBuilder::new,$->{}
        ).build().create(AuxiliaUtilities.id("item/creative_builders_wand_skeleton"),new TextureMapping().put(TextureSlot.TEXTURE,AuxiliaUtilities.id("item/tools/creative_builders_wand_skeleton")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.CREATIVE_BUILDERS_WAND.asItem(),new CompositeModel.Unbaked(
                List.of(new BlockModelWrapper.Unbaked(creativeBuilderModel,Collections.emptyList()),new BlockModelWrapper.Unbaked(creativeBuilderOverlayModel,Collections.emptyList()))
        ));

        ResourceLocation creativeDestructionModel = ModelTemplates.FLAT_HANDHELD_ITEM.create(AUItems.CREATIVE_DESTRUCTION_WAND.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/creative_destruction_wand")),itemModels.modelOutput);
        ResourceLocation creativeDestructionOverlayModel = ModelTemplates.create(TextureSlot.TEXTURE).extend().customLoader(
                Quad2DLoaderBuilder::new,$->{}
        ).build().create(AuxiliaUtilities.id("item/creative_destruction_wand_skeleton"),new TextureMapping().put(TextureSlot.TEXTURE,AuxiliaUtilities.id("item/tools/creative_destruction_wand_skeleton")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.CREATIVE_DESTRUCTION_WAND.asItem(),new CompositeModel.Unbaked(
                List.of(new BlockModelWrapper.Unbaked(creativeDestructionModel,Collections.emptyList()),new BlockModelWrapper.Unbaked(creativeDestructionOverlayModel,Collections.emptyList()))
        ));
        handheldTool(itemModels,AUItems.GLASS_CUTTER);
        handheldTool(itemModels,AUItems.WATERING_CAN);

        itemModels.itemModelOutput.accept(AUItems.UNSTABLE_INGOT.asItem(), new BlockModelWrapper.Unbaked(
                itemModels.generateLayeredItem(AUItems.UNSTABLE_INGOT.asItem(), AuxiliaUtilities.id("item/unstable_ingot"),AuxiliaUtilities.id("item/unstable_ingot_inner")),
                List.of(new AUItemTintSource(0))
        ));
        itemModels.itemModelOutput.accept(AUItems.STABLE_UNSTABLE_INGOT.asItem(), new BlockModelWrapper.Unbaked(
                itemModels.generateLayeredItem(AUItems.UNSTABLE_INGOT.asItem(), AuxiliaUtilities.id("item/unstable_ingot"),AuxiliaUtilities.id("item/unstable_ingot_inner")),
                Collections.emptyList()
        ));
        itemModels.generateFlatItem(AUItems.STABLE_UNSTABLE_NUGGET.asItem(), ModelTemplates.FLAT_ITEM);

        itemModels.itemModelOutput.accept(AUItems.SUN_CRYSTAL.asItem(), new CompositeModel.Unbaked(
                List.of(
                        new BlockModelWrapper.Unbaked(
                                itemModels.createFlatItemModel(AUItems.SUN_CRYSTAL.asItem(), ModelTemplates.FLAT_ITEM),
                                Collections.emptyList()
                        ),
                        new BlockModelWrapper.Unbaked(
                                itemModels.createFlatItemModel(AUItems.SUN_CRYSTAL.asItem(), "_inner",ModelTemplates.FLAT_ITEM),
                                List.of(new AUItemTintSource(1))
                        )
                )
        ));

        makeEnderShards(itemModels);
        itemModels.generateFlatItem(AUItems.RESONATING_REDSTONE_CRYSTAL.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.REDSTONE_GEAR.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.EYE_OF_REDSTONE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.LUNAR_REACTIVE_DUST.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.RED_COAL.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.MOON_STONE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.DEMON_INGOT.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.ENCHANTED_INGOT.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.EVIL_INFUSED_IRON_INGOT.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.DEMON_NUGGET.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.ENCHANTED_NUGGET.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.EVIL_INFUSED_IRON_NUGGET.asItem(), ModelTemplates.FLAT_ITEM);

        handheldTool(itemModels,AUItems.WOODEN_SICKLE);
        handheldTool(itemModels,AUItems.STONE_SICKLE);
        handheldTool(itemModels,AUItems.IRON_SICKLE);
        handheldTool(itemModels,AUItems.GOLDEN_SICKLE);
        handheldTool(itemModels,AUItems.DIAMOND_SICKLE);
        handheldTool(itemModels,AUItems.NETHERITE_SICKLE);

        itemModels.generateFlatItem(AUItems.UPGRADE_BASE.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.MAGICAL_APPLE.asItem(), ModelTemplates.FLAT_ITEM);
        upgrade(itemModels,AUItems.SPEED_UPGRADE);
        upgrade(itemModels,AUItems.ENCHANTED_SPEED_UPGRADE);
        upgrade(itemModels,AUItems.ULTIMATE_SPEED_UPGRADE);

        ResourceLocation rgbottom = ModelTemplates.SLAB_BOTTOM.create(AUItems.RAINBOW_GENERATOR_BOTTOM.asItem(),new TextureMapping()
                .put(TextureSlot.BOTTOM,AuxiliaUtilities.id("block/rainbow_generator"))
                .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/rainbow_generator_bottom"))
                .put(TextureSlot.TOP,AuxiliaUtilities.id("block/rainbow_generator_center"))
        ,itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.RAINBOW_GENERATOR_BOTTOM.asItem(), new BlockModelWrapper.Unbaked(rgbottom,Collections.emptyList()));
        ResourceLocation rgtop = ModelTemplates.createItem(AuxiliaUtilities.MODID + ":slab_top",TextureSlot.BOTTOM,TextureSlot.SIDE,TextureSlot.TOP).create(AUItems.RAINBOW_GENERATOR_BOTTOM.asItem(),new TextureMapping()
                        .put(TextureSlot.BOTTOM,AuxiliaUtilities.id("block/rainbow_generator"))
                        .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/rainbow_generator_bottom"))
                        .put(TextureSlot.TOP,AuxiliaUtilities.id("block/rainbow_generator_center"))
                ,itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.RAINBOW_GENERATOR_TOP.asItem(), new BlockModelWrapper.Unbaked(rgtop,Collections.emptyList()));

        ExtendedModelTemplate flatTransferNode  =  ModelTemplates.createItem(AuxiliaUtilities.MODID + ":flat_transfer_node", TextureSlot.TEXTURE,TextureSlot.PARTICLE).extend().customLoader(Quad2DLoaderBuilder::new,$->{}).build();
        ResourceLocation ftnItem = flatTransferNode.create(AUItems.FLAT_ITEM_TRANSFER_NODE.asItem(),new TextureMapping()
                .put(TextureSlot.TEXTURE, FlatTransferNodeHandler.ITEM_NODE_SPRITE)
                .copySlot(TextureSlot.TEXTURE,TextureSlot.PARTICLE), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.FLAT_ITEM_TRANSFER_NODE.asItem(), new BlockModelWrapper.Unbaked(ftnItem,Collections.emptyList()));

        ResourceLocation ftnFluid = flatTransferNode.create(AUItems.FLAT_FLUID_TRANSFER_NODE.asItem(),new TextureMapping()
                .put(TextureSlot.TEXTURE, FlatTransferNodeHandler.FLUID_NODE_SPRITE)
                .copySlot(TextureSlot.TEXTURE,TextureSlot.PARTICLE), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.FLAT_FLUID_TRANSFER_NODE.asItem(), new BlockModelWrapper.Unbaked(ftnFluid,Collections.emptyList()));

        ResourceLocation biomeMarkerEmpty = itemModels.createFlatItemModel(AUItems.BIOME_MARKER.asItem(),ModelTemplates.FLAT_ITEM);
        ResourceLocation biomeMarkerFullBase = itemModels.createFlatItemModel(AUItems.BIOME_MARKER.asItem(),"_active",ModelTemplates.FLAT_ITEM);
        ResourceLocation biomeMarkerFull1 = itemModels.createFlatItemModel(AUItems.BIOME_MARKER.asItem(),"_active_1",ModelTemplates.FLAT_ITEM);
        ResourceLocation biomeMarkerFull2 = itemModels.createFlatItemModel(AUItems.BIOME_MARKER.asItem(),"_active_2",ModelTemplates.FLAT_ITEM);
        ResourceLocation biomeMarkerFull3 = itemModels.createFlatItemModel(AUItems.BIOME_MARKER.asItem(),"_active_3",ModelTemplates.FLAT_ITEM);
        ResourceLocation biomeMarkerFull4 = itemModels.createFlatItemModel(AUItems.BIOME_MARKER.asItem(),"_active_4",ModelTemplates.FLAT_ITEM);

        itemModels.itemModelOutput.accept(AUItems.BIOME_MARKER.asItem(),
                new ConditionalItemModel.Unbaked(
                        new BiomeMarkerHasBiomeProp(),
                        new BlockModelWrapper.Unbaked(
                                biomeMarkerEmpty,
                                Collections.emptyList()
                        ),
                        new CompositeModel.Unbaked(
                                List.of(
                                        new BlockModelWrapper.Unbaked(biomeMarkerFullBase,Collections.emptyList()),
                                        new BlockModelWrapper.Unbaked(biomeMarkerFull1,List.of(new AUItemTintSource(1))),
                                        new BlockModelWrapper.Unbaked(biomeMarkerFull2,List.of(new AUItemTintSource(2))),
                                        new BlockModelWrapper.Unbaked(biomeMarkerFull3,List.of(new AUItemTintSource(3))),
                                        new BlockModelWrapper.Unbaked(biomeMarkerFull4,List.of(new AUItemTintSource(4)))
                                        )
                        )
                ));



        itemModels.itemModelOutput.accept(AUItems.COMPOUND_BOW.asItem(),
                new ConditionalItemModel.Unbaked(
                        new IsUsingItem(),
                        handheldTool(itemModels,"compound_bow"),
                        new RangeSelectItemModel.Unbaked(
                            new UseDuration(false),
                                0.05F,
                                List.of(
                                        new RangeSelectItemModel.Entry(
                                                0.4f,
                                                handheldTool(itemModels,"compound_bow_pull_1")
                                        ),
                                        new RangeSelectItemModel.Entry(
                                                0.82f,
                                                handheldTool(itemModels,"compound_bow_pull_2")
                                        ),
                                        new RangeSelectItemModel.Entry(
                                                1f,
                                                handheldTool(itemModels,"compound_bow_pull_charged")
                                        )
                                ),
                                Optional.of(handheldTool(itemModels,"compound_bow_pull_0"))

                        )
                ));

        ResourceLocation itemFilterModel = ModelTemplates.FLAT_ITEM.create(AUItems.ITEM_FILTER.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/filter_items")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.ITEM_FILTER.asItem(),new BlockModelWrapper.Unbaked(itemFilterModel, Collections.emptyList()));
        ResourceLocation fluidFilterModel = ModelTemplates.FLAT_ITEM.create(AUItems.FLUID_FILTER.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/filter_fluids")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.FLUID_FILTER.asItem(),new BlockModelWrapper.Unbaked(fluidFilterModel, Collections.emptyList()));

        itemModels.generateFlatItem(AUItems.POWER_MANAGER.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.DROP_OF_EVIL.asItem(), ModelTemplates.FLAT_ITEM);
        ResourceLocation fluidDroplet = itemModels.createFlatItemModel(AUItems.FLUID_DROPLET.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(AUItems.FLUID_DROPLET.asItem(),new BlockModelWrapper.Unbaked(fluidDroplet,List.of(new AUItemTintSource(0))));
        itemModels.generateFlatItem(AUItems.ENERGY_DROPLET.asItem(), ModelTemplates.FLAT_ITEM);

        ResourceLocation divisionSigilModel = ModelTemplates.FLAT_ITEM.create(AUItems.UNACTIVATED_DIVISION_SIGIL.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/division_sigil")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.UNACTIVATED_DIVISION_SIGIL.asItem(),new BlockModelWrapper.Unbaked(divisionSigilModel, Collections.emptyList()));
        itemModels.generateFlatItem(AUItems.DIVISION_SIGIL.asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(AUItems.PSEUDO_INVERSION_SIGIL.asItem(), ModelTemplates.FLAT_ITEM);

        List<SelectItemModel.SwitchCase<ItemAngelRing.AngelRingWings>> cases = new ArrayList<>();
        for (int i = 1; i < ItemAngelRing.AngelRingWings.values().length; i++) {
            ItemAngelRing.AngelRingWings wings = ItemAngelRing.AngelRingWings.values()[i];
            cases.add(new SelectItemModel.SwitchCase<>(
                    List.of(wings),
                    new BlockModelWrapper.Unbaked(ModelTemplates.FLAT_ITEM.create(AuxiliaUtilities.id(AUItems.ANGEL_RING.getId().getPath()+"_"+wings.name().toLowerCase()),new TextureMapping()
                            .put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/angel_ring_"+wings.name().toLowerCase())), itemModels.modelOutput),Collections.emptyList())
            ));
        }

        itemModels.itemModelOutput.accept(AUItems.ANGEL_RING.asItem(),
                new SelectItemModel.Unbaked(
                        new SelectItemModel.UnbakedSwitch<>(
                                new AngelRingWingsProp(),
                                List.copyOf(cases)
                        ),
                        Optional.of(
                                new BlockModelWrapper.Unbaked(ModelTemplates.FLAT_ITEM.create(AUItems.ANGEL_RING.asItem(),new TextureMapping()
                                        .put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/angel_ring")), itemModels.modelOutput),Collections.emptyList())
                        )
                ));

        itemModels.itemModelOutput.accept(AUItems.FLYING_SQUID_RING.asItem(),
                new BlockModelWrapper.Unbaked(ModelTemplates.FLAT_ITEM.create(AUItems.FLYING_SQUID_RING.asItem(),new TextureMapping()
                        .put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/flying_squid_ring")), itemModels.modelOutput),Collections.emptyList()));
        itemModels.itemModelOutput.accept(AUItems.CHICKEN_RING.asItem(),
                new BlockModelWrapper.Unbaked(ModelTemplates.FLAT_ITEM.create(AUItems.CHICKEN_RING.asItem(),new TextureMapping()
                        .put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/chicken_ring")), itemModels.modelOutput),Collections.emptyList()));

        ModelTemplate handheldThreeItems = ModelTemplates.createItem("handheld",TextureSlot.LAYER0,TextureSlot.LAYER1,TextureSlot.LAYER2);
        ResourceLocation goldenLassoFull = handheldThreeItems.create(AUItems.GOLDEN_LASSO.getId().withSuffix("_full"),TextureMapping.layered(AuxiliaUtilities.id("item/tools/golden_lasso"),AuxiliaUtilities.id("item/tools/lasso_internal_1"),AuxiliaUtilities.id("item/tools/lasso_internal_2")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.GOLDEN_LASSO.asItem(),
                new ConditionalItemModel.Unbaked(
                        new LassoHasEntityProp(),
                        handheldTool(itemModels,"golden_lasso"),
                        new BlockModelWrapper.Unbaked(
                                goldenLassoFull,
                                Collections.emptyList()
                        )
                ));
        ResourceLocation cursedLassoFull = handheldThreeItems.create(AUItems.CURSED_LASSO.getId().withSuffix("_full"),TextureMapping.layered(AuxiliaUtilities.id("item/tools/cursed_lasso"),AuxiliaUtilities.id("item/tools/lasso_internal_1"),AuxiliaUtilities.id("item/tools/lasso_internal_2")),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(AUItems.CURSED_LASSO.asItem(),
                new ConditionalItemModel.Unbaked(
                        new LassoHasEntityProp(),
                        handheldTool(itemModels,"cursed_lasso"),
                        new BlockModelWrapper.Unbaked(
                                cursedLassoFull,
                                Collections.emptyList()
                        )
                ));

        itemModels.itemModelOutput.accept(AUItems.LUX_SABER.asItem(),
                new ConditionalItemModel.Unbaked(
                        new LuxSaberHasEnergyProp(),
                        new BlockModelWrapper.Unbaked(
                                AuxiliaUtilities.id("item/lux_saber_active"),
                                List.of()
                        ),
                        new BlockModelWrapper.Unbaked(
                                AuxiliaUtilities.id("item/lux_saber"),
                                List.of()
                        )
                ));


        super.registerModels(blockModels, itemModels);
    }

    private void createRedstoneClock(BlockModelGenerators blockModels) {
        ResourceLocation on = TexturedModel.CUBE.create(AUBlocks.REDSTONE_CLOCK.get(), blockModels.modelOutput);
        ResourceLocation off = blockModels.createSuffixedVariant(AUBlocks.REDSTONE_CLOCK.get(), "_off", ModelTemplates.CUBE_ALL, TextureMapping::cube);
        blockModels.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(Blocks.REDSTONE_LAMP)
                                .with(PropertyDispatch.initial(BlockRedstoneClock.POWER_STATE)
                                        .select(BlockRedstoneClock.PowerState.DISABLED,BlockModelGenerators.plainVariant(off))
                                        .select(BlockRedstoneClock.PowerState.ENABLED_POWERED,BlockModelGenerators.plainVariant(on))
                                        .select(BlockRedstoneClock.PowerState.ENABLED_NOT_POWERED,BlockModelGenerators.plainVariant(on))
                                )
                );

    }

    private void createBlockWithTexture(BlockModelGenerators blockModels, Block block, String texture){
        TexturedModel.Provider CUBE_ALL_PROVIDER = TexturedModel.createDefault(
                b-> TextureMapping.cube(AuxiliaUtilities.id("block/"+texture)),
                ModelTemplates.CUBE_ALL
        );
        blockModels.createTrivialBlock(block,CUBE_ALL_PROVIDER);
    }

    private void createBlockWithModel(BlockModelGenerators blockModels, Block block, String model){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(
                block,
                BlockModelGenerators.plainVariant(AuxiliaUtilities.id("block/"+model))
        ));
    }

    private void createBlockWithModel(BlockModelGenerators blockModels, DeferredBlock<?> block){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(
                block.get(),
                BlockModelGenerators.plainVariant(AuxiliaUtilities.id("block/"+block.getId().getPath()))
        ));
    }

    private void createSimpleColumnBlock(BlockModelGenerators blockModels, Block block, String side,String end){
        TexturedModel.Provider COLUMN_PROVIDER = TexturedModel.createDefault(
                b->
                        TextureMapping.column(AuxiliaUtilities.id("block/"+side),AuxiliaUtilities.id("block/"+end)),
                ModelTemplates.CUBE_COLUMN
        );
        blockModels.createTrivialBlock(block,COLUMN_PROVIDER);
    }

    private void createCubeTopBottomBlock(BlockModelGenerators blockModels, Block block, String side, String top, String bottom){
        TexturedModel.Provider PROVIDER = TexturedModel.createDefault(
                b->
                        new TextureMapping()
                                .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/"+side))
                                .put(TextureSlot.TOP,AuxiliaUtilities.id("block/"+top))
                                .put(TextureSlot.BOTTOM,AuxiliaUtilities.id("block/"+bottom))
                ,
                ModelTemplates.CUBE_BOTTOM_TOP
        );
        blockModels.createTrivialBlock(block,PROVIDER);
    }

    private void createPlant(BlockModelGenerators blockModels, DeferredBlock<?> block, Property<Integer> prop) {
            Int2ObjectMap<ResourceLocation> int2objectmap = new Int2ObjectOpenHashMap<>();
            PropertyDispatch<MultiVariant> propertydispatch = PropertyDispatch.initial(prop)
                    .generate(
                            i -> {
                                ResourceLocation resourcelocation = int2objectmap.computeIfAbsent(
                                        i, p_387534_ -> blockModels.createSuffixedVariant(block.get(),
                                                "_stage" + i,
                                                ModelTemplates.CROSS,
                                                $->TextureMapping.cross(AuxiliaUtilities.id("block/plants/"+block.getId().getPath()+"_stage_"+i)))
                                );
                                return BlockModelGenerators.plainVariant(resourcelocation);
                            }
                    );
            blockModels.registerSimpleFlatItemModel(block.asItem());
            blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get()).with(propertydispatch));
    }

    private void createRedOrchid(BlockModelGenerators blockModels) {
        ModelTemplate template= ModelTemplates.create(AuxiliaUtilities.id("red_orchid").toString(),TextureSlot.CROSS,BASE);
        Int2ObjectMap<ResourceLocation> int2objectmap = new Int2ObjectOpenHashMap<>();
        PropertyDispatch<MultiVariant> propertydispatch = PropertyDispatch.initial(BlockRedOrchid.AGE)
                .generate(
                        i -> {
                            ResourceLocation resourcelocation = int2objectmap.computeIfAbsent(
                                    i, p_387534_ -> blockModels.createSuffixedVariant(AUBlocks.RED_ORCHID.get(),
                                            "_stage" + i,
                                            template,
                                            $->new TextureMapping()
                                                    .put(TextureSlot.CROSS,AuxiliaUtilities.id("block/plants/redorchid_"+i))
                                                    .put(BASE,AuxiliaUtilities.id("block/plants/redorchid_base_"+i))
                                    )
                            );
                            return BlockModelGenerators.plainVariant(resourcelocation);
                        }
                );
        blockModels.registerSimpleFlatItemModel(AUBlocks.RED_ORCHID.asItem());
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(AUBlocks.RED_ORCHID.get()).with(propertydispatch));
    }

    private void createOrientableMachine(BlockModelGenerators blockModels,DeferredBlock<?> block,String side,String front,String top){
        Function<Boolean,TexturedModel.Provider> modelFunc = lit -> TexturedModel.createDefault(
                b->new TextureMapping()
                        .put(TextureSlot.FRONT,AuxiliaUtilities.id("block/"+front+((boolean) lit ? "_on":"")))
                        .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/"+side))
                        .put(TextureSlot.TOP,AuxiliaUtilities.id("block/"+top)),
                ModelTemplates.CUBE_ORIENTABLE
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get())
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT,
                        BlockModelGenerators.plainVariant(modelFunc.apply(true).create(block.get(), blockModels.modelOutput)),
                        BlockModelGenerators.plainVariant(modelFunc.apply(false).create(block.get(), blockModels.modelOutput))))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

    private void createSimpleHorizontal(BlockModelGenerators blockModels, DeferredBlock<?> block,String side,String front,String end){
        TexturedModel.Provider prov = TexturedModel.createDefault(
                b->new TextureMapping()
                        .put(TextureSlot.FRONT,AuxiliaUtilities.id("block/"+front))
                        .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/"+side))
                        .put(TextureSlot.TOP,AuxiliaUtilities.id("block/"+end)),
                ModelTemplates.CUBE_ORIENTABLE
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get(), BlockModelGenerators.plainVariant(prov.create(block.get(),blockModels.modelOutput)))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
        );
    }

    private void createSpike(BlockModelGenerators blockModels,DeferredBlock<?> block){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block.get(),
                        BlockModelGenerators.plainVariant(AuxiliaUtilities.id("block/"+block.getId().getPath())))
                .with(BlockModelGenerators.ROTATION_FACING)
        );
    }

    private void createGenerator(BlockModelGenerators blockModels,DeferredBlock<?> generator){
        String id = generator.getId().getPath();
        createOrientableMachine(blockModels,generator,"machines/"+id+"/side","machines/"+id+"/front","machines/"+id+"/top");
    }

    private void createOpiniumCore(BlockModelGenerators blockModels,DeferredBlock<?> block){
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block.get(),BlockModelGenerators.plainVariant(AuxiliaUtilities.id("block/opinium_core")))
        );
    }

    private void createDrum(BlockModelGenerators blockModels,DeferredBlock<?> drum){
        ModelTemplate template = ModelTemplates.create(AuxiliaUtilities.id("drum").toString(),TextureSlot.SIDE,TextureSlot.TOP);
        String id = drum.getId().getPath().replace("_drum","");
        TexturedModel.Provider provider = TexturedModel.createDefault(b->
                new TextureMapping()
                        .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/drum/"+id))
                        .put(TextureSlot.TOP,AuxiliaUtilities.id("block/drum/top_"+id)),
                template
        );
        blockModels.createTrivialBlock(drum.get(),provider);
    }

    private void createGlass(BlockModelGenerators blockModels,DeferredBlock<?> block,String glass){
        TexturedModel.Provider provider = TexturedModel.createDefault(b->
                        new TextureMapping()
                                .put(TextureSlot.ALL,AuxiliaUtilities.id("block/glass/"+glass+"_glass")),
                ExtendedModelTemplateBuilder.of(ModelTemplates.CUBE_ALL).renderType("translucent").build()
        );
        blockModels.createTrivialBlock(block.get(),provider);
    }

    private void createGlass(BlockModelGenerators blockModels,Block block,String texture){
        TexturedModel.Provider provider = TexturedModel.createDefault(b->
                        new TextureMapping()
                                .put(TextureSlot.ALL,AuxiliaUtilities.id("block/glass/"+texture)),
                ExtendedModelTemplateBuilder.of(ModelTemplates.CUBE_ALL).renderType("translucent").build()
        );
        blockModels.createTrivialBlock(block,provider);
    }

    private void upgrade(ItemModelGenerators itemModels,DeferredItem<? extends Item> item, ItemTintSource... tints){
        List<String> name = new ArrayList<>(Arrays.stream(item.getId().getPath().replace("_upgrade","").split("_")).toList());
        Collections.reverse(name);
        ResourceLocation model = ModelTemplates.FLAT_ITEM.create(item.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/upgrade_" + String.join("_",name))),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item.asItem(),new BlockModelWrapper.Unbaked(
                model,
                List.of(tints)
        ));
    }

    private void handheldTool(ItemModelGenerators itemModels, DeferredItem<? extends Item> item, ItemTintSource... tints){
        ResourceLocation model = ModelTemplates.FLAT_HANDHELD_ITEM.create(item.asItem(),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/"+item.getId().getPath())),itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item.asItem(),new BlockModelWrapper.Unbaked(
                model,
                List.of(tints)
        ));
    }

    private BlockModelWrapper.Unbaked handheldTool(ItemModelGenerators itemModels, String texture, ItemTintSource... tints){
        ResourceLocation model = ModelTemplates.FLAT_HANDHELD_ITEM.create(AuxiliaUtilities.id(texture),new TextureMapping().put(TextureSlot.LAYER0,AuxiliaUtilities.id("item/tools/"+texture)),itemModels.modelOutput);
        return new BlockModelWrapper.Unbaked(model, List.of(tints));
    }

    private void makeEnderShards(ItemModelGenerators itemModels){
        List<RangeSelectItemModel.Entry> entries = new ArrayList<>();
        for (int i = 1; i < 9; i++){
            entries.add(
                    new RangeSelectItemModel.Entry(
                            i,
                            ItemModelUtils.plainModel(itemModels.createFlatItemModel(AUItems.ENDER_SHARD.asItem(), "s/ender_shard_"+i,ModelTemplates.FLAT_ITEM))
                    )
            );
        }

        itemModels.itemModelOutput.accept(AUItems.ENDER_SHARD.asItem(), new RangeSelectItemModel.Unbaked(
                new Count(false),
                1,
                entries,
                Optional.of(
                        new BlockModelWrapper.Unbaked(
                                AuxiliaUtilities.id("item/ender_shards/ender_shard_1"),
                                Collections.emptyList()
                        )
                )
        ));
    }
}

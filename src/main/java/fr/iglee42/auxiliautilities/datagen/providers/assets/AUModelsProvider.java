package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.*;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;

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

        super.registerModels(blockModels, itemModels);
    }

    private void createRedstoneClock(BlockModelGenerators blockModels) {
        ResourceLocation on = TexturedModel.CUBE.create(AUBlocks.REDSTONE_CLOCK.get(), blockModels.modelOutput);
        ResourceLocation off = blockModels.createSuffixedVariant(AUBlocks.REDSTONE_CLOCK.get(), "_off", ModelTemplates.CUBE_ALL, TextureMapping::cube);
        blockModels.blockStateOutput
                .accept(
                        MultiVariantGenerator.multiVariant(Blocks.REDSTONE_LAMP)
                                .with(PropertyDispatch.property(BlockRedstoneClock.POWER_STATE)
                                        .select(BlockRedstoneClock.PowerState.DISABLED,Variant.variant().with(VariantProperties.MODEL,off))
                                        .select(BlockRedstoneClock.PowerState.ENABLED_POWERED,Variant.variant().with(VariantProperties.MODEL,on))
                                        .select(BlockRedstoneClock.PowerState.ENABLED_NOT_POWERED,Variant.variant().with(VariantProperties.MODEL,on))
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
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(
                block,
                Variant.variant().with(VariantProperties.MODEL,AuxiliaUtilities.id("block/"+model))
        ));
    }

    private void createBlockWithModel(BlockModelGenerators blockModels, DeferredBlock<?> block){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(
                block.get(),
                Variant.variant().with(VariantProperties.MODEL,AuxiliaUtilities.id("block/"+block.getId().getPath()))
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
            PropertyDispatch propertydispatch = PropertyDispatch.property(prop)
                    .generate(
                            i -> {
                                ResourceLocation resourcelocation = int2objectmap.computeIfAbsent(
                                        i, p_387534_ -> blockModels.createSuffixedVariant(block.get(),
                                                "_stage" + i,
                                                ModelTemplates.CROSS,
                                                $->TextureMapping.cross(AuxiliaUtilities.id("block/plants/"+block.getId().getPath()+"_stage_"+i)))
                                );
                                return Variant.variant().with(VariantProperties.MODEL, resourcelocation);
                            }
                    );
            blockModels.registerSimpleFlatItemModel(block.asItem());
            blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.get()).with(propertydispatch));
    }

    private void createRedOrchid(BlockModelGenerators blockModels) {
        ModelTemplate template= ModelTemplates.create(AuxiliaUtilities.id("block/red_orchid").toString(),TextureSlot.CROSS,BASE);
        Int2ObjectMap<ResourceLocation> int2objectmap = new Int2ObjectOpenHashMap<>();
        PropertyDispatch propertydispatch = PropertyDispatch.property(BlockRedOrchid.AGE)
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
                            return Variant.variant().with(VariantProperties.MODEL, resourcelocation);
                        }
                );
        blockModels.registerSimpleFlatItemModel(AUBlocks.RED_ORCHID.asItem());
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(AUBlocks.RED_ORCHID.get()).with(propertydispatch));
    }

    private void createOrientableMachine(BlockModelGenerators blockModels,DeferredBlock<?> block,String side,String front,String top){
        Function<Boolean,TexturedModel.Provider> modelFunc = lit -> TexturedModel.createDefault(
                b->new TextureMapping()
                        .put(TextureSlot.FRONT,AuxiliaUtilities.id("block/"+front+((boolean) lit ? "_on":"")))
                        .put(TextureSlot.SIDE,AuxiliaUtilities.id("block/"+side))
                        .put(TextureSlot.TOP,AuxiliaUtilities.id("block/"+top)),
                ModelTemplates.CUBE_ORIENTABLE
        );
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.get())
                .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.LIT,
                        modelFunc.apply(true).create(block.get(), blockModels.modelOutput),
                        modelFunc.apply(false).create(block.get(), blockModels.modelOutput)))
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
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
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.get(), Variant.variant().with(VariantProperties.MODEL,prov.create(block.get(),blockModels.modelOutput)))
                .with(BlockModelGenerators.createHorizontalFacingDispatch())
        );
    }

    private void createSpike(BlockModelGenerators blockModels,DeferredBlock<?> block){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block.get(),
                        Variant.variant().with(VariantProperties.MODEL,AuxiliaUtilities.id("block/"+block.getId().getPath())))
                .with(BlockModelGenerators.createFacingDispatch())
        );
    }

    private void createGenerator(BlockModelGenerators blockModels,DeferredBlock<?> generator){
        String id = generator.getId().getPath();
        createOrientableMachine(blockModels,generator,"machines/"+id+"/side","machines/"+id+"/front","machines/"+id+"/top");
    }

    private void createOpiniumCore(BlockModelGenerators blockModels,DeferredBlock<?> block){
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(block.get(),AuxiliaUtilities.id("block/opinium_core"))
        );
    }

    private void createDrum(BlockModelGenerators blockModels,DeferredBlock<?> drum){
        ModelTemplate template = ModelTemplates.create(AuxiliaUtilities.id("block/drum").toString(),TextureSlot.SIDE,TextureSlot.TOP);
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
}

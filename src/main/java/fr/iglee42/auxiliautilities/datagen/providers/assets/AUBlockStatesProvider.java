package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.EmptyModel;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class AUBlockStatesProvider extends BlockStateProvider {

    private static final ResourceLocation MACHINE_SIDE = AuxiliaUtilities.id("block/machines/side");
    private static final ResourceLocation MACHINE_BOTTOM = AuxiliaUtilities.id("block/machines/bottom");

    public AUBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AuxiliaUtilities.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(AUBlocks.ANGEL_BLOCK.get());
        simpleBlock(AUBlocks.DEMON_BLOCK.get(),models().cubeAll(AUBlocks.DEMON_BLOCK.getId().getPath(), modLoc("block/demon_metal_block")));
        simpleBlock(AUBlocks.ENCHANTED_BLOCK.get(),models().cubeAll(AUBlocks.ENCHANTED_BLOCK.getId().getPath(), modLoc("block/enchanted_metal_block")));
        simpleBlock(AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS){
            simpleBlock(set.getBlock().get());
            stairsBlock(set.getStairs().get(), blockTexture(set.getBlock().get()));
            slabBlock(set.getSlab().get(), blockTexture(set.getBlock().get()),blockTexture(set.getBlock().get()));
            wallBlock(set.getWall().get(), blockTexture(set.getBlock().get()));
        }

        simpleBlock(AUBlocks.MANUAL_MILL.get(),new ConfiguredModel(new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/manual_mill_base"))));
        ResourceLocation creativeMillModel = models().cubeColumn(AUBlocks.CREATIVE_MILL.getId().getPath(), modLoc("block/creative_mill_side"), modLoc("block/creative_mill")).getUncheckedLocation();
        simpleBlock(AUBlocks.SOLAR_PANEL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/solar_panel")));
        simpleBlock(AUBlocks.LUNAR_PANEL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/lunar_panel")));
        simpleBlock(AUBlocks.FIRE_MILL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/fire_mill")));
        simpleBlock(AUBlocks.WATER_MILL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/water_mill")));
        simpleBlock(AUBlocks.WIND_MILL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/wind_mill")));
        simpleBlock(AUBlocks.LAVA_MILL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/lava_mill")));
        simpleBlock(AUBlocks.DRAGON_EGG_MILL.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/dragon_egg_mill")));
        getVariantBuilder(AUBlocks.CREATIVE_MILL.get()).forAllStates($->new ConfiguredModel[]{ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(creativeMillModel)).buildLast()});


        simpleBlock(AUBlocks.MAGICAL_WOOD.get());
        simpleBlock(AUBlocks.MAGICAL_PLANKS.get());
        simpleBlock(AUBlocks.DIAGONAL_WOOD.get());
        simpleBlock(AUBlocks.RESONATOR.get(),new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("block/resonator")));

        simpleBlock(AUBlocks.SOUND_MUFFLER.get());

        getVariantBuilder(AUBlocks.ENDER_LILLY.get()).
                forAllStates(state->{
                    int age = state.getValue(CropBlock.AGE);
                    ResourceLocation id = models().cross(AUBlocks.ENDER_LILLY.getRegisteredName() + "_stage_" +age, modLoc("block/plants/ender_lilly_stage_" + age)).renderType("cutout").getUncheckedLocation();
                    return new ConfiguredModel[]{ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(id)).buildLast()};
                });

        horizontalBlock(AUBlocks.FURNACE.get(), state->{
            boolean lit = state.getValue(BlockFurnace.LIT);
            ResourceLocation model = models().orientable(AUBlocks.FURNACE.getRegisteredName() + (lit ? "_on" : ""), MACHINE_SIDE, modLoc("block/machines/furnace_front" + (lit?"_on":"")), MACHINE_BOTTOM).getUncheckedLocation();
            return new ModelFile.UncheckedModelFile(model);
        });

        horizontalBlock(AUBlocks.CRUSHER.get(), state->{
            boolean lit = state.getValue(BlockCrusher.LIT);
            ResourceLocation model = models().orientable(AUBlocks.CRUSHER.getRegisteredName() + (lit ? "_on" : ""), MACHINE_SIDE, modLoc("block/machines/crusher_front" + (lit?"_on":"")), MACHINE_BOTTOM).getUncheckedLocation();
            return new ModelFile.UncheckedModelFile(model);
        });


        simpleBlock(AUBlocks.ENCHANTER.get(),new ModelFile.UncheckedModelFile(models().cubeBottomTop(AUBlocks.ENCHANTER.getRegisteredName(), modLoc("block/machines/enchanter_side"),MACHINE_BOTTOM,modLoc("block/machines/enchanter_top")).getUncheckedLocation()));
        simpleBlock(AUBlocks.MACHINE_BLOCK.get(),new ModelFile.UncheckedModelFile(models().cubeColumn(AUBlocks.MACHINE_BLOCK.getRegisteredName(), MACHINE_SIDE,MACHINE_BOTTOM).getUncheckedLocation()));

        CompressedBlockSet.ALL_SETS.forEach(set-> {
            for (int tier = 1; tier <= set.getMaxTier(); tier++) {
                simpleBlock(set.getBlock(tier).get(), models().cubeAll(set.getBlock(tier).getRegisteredName(), modLoc("block/compressed/" + set.getName() + "/" + tier)));
            }
        });

        ResourceLocation redstoneClockModel = models().cubeAll(AUBlocks.REDSTONE_CLOCK.getRegisteredName(), modLoc("block/redstone_clock")).getUncheckedLocation();
        ResourceLocation redstoneClockModelOff = models().cubeAll(AUBlocks.REDSTONE_CLOCK.getRegisteredName() + "_off", modLoc("block/redstone_clock_off")).getUncheckedLocation();
        getVariantBuilder(AUBlocks.REDSTONE_CLOCK.get())
                .forAllStates(state->{
                    boolean off = state.getValue(BlockRedstoneClock.POWER_STATE) == BlockRedstoneClock.PowerState.DISABLED;
                    return new ConfiguredModel[]{ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(off ? redstoneClockModelOff : redstoneClockModel)).buildLast()};
                });

        generator(AUBlocks.SURVIVAL_GENERATOR);
        generator(AUBlocks.FURNACE_GENERATOR);
        generator(AUBlocks.OVERCLOCKED_GENERATOR);
        generator(AUBlocks.CULINARY_GENERATOR);
        generator(AUBlocks.MAGMATIC_GENERATOR);
        generator(AUBlocks.POTION_GENERATOR);
        generator(AUBlocks.SLIMEY_GENERATOR);
        generator(AUBlocks.DEATH_GENERATOR);
        generator(AUBlocks.PINK_GENERATOR);
        generator(AUBlocks.EXPLOSIVE_GENERATOR);
        generator(AUBlocks.HEATED_REDSTONE_GENERATOR);
        generator(AUBlocks.ENDER_GENERATOR);
        generator(AUBlocks.DISENCHANTMENT_GENERATOR);
        generator(AUBlocks.FROSTY_GENERATOR);
        generator(AUBlocks.HALITOSIS_GENERATOR);
        generator(AUBlocks.NETHER_STAR_GENERATOR);

        simpleBlock(AUBlocks.RAINBOW_GENERATOR.get());

        simpleBlock(AUBlocks.MISERABLE_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.MISERABLE_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.PATHETIC_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.PATHETIC_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.MEDIOCRE_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.MEDIOCRE_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.PASSABLE_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.PASSABLE_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.DECENT_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.DECENT_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.SOLID_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.SOLID_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.GOOD_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.GOOD_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.DAMN_GOOD_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.DAMN_GOOD_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.AMAZING_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.AMAZING_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.INSPIRING_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.INSPIRING_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
        simpleBlock(AUBlocks.PERFECTED_OPINIUM_CORE.get(),models().getBuilder(AUBlocks.PERFECTED_OPINIUM_CORE.getRegisteredName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")));
    }

    private void generator(DeferredBlock<BlockGenerator> generator){
        horizontalBlock(generator.get(), state->{
            boolean lit = state.getValue(BlockGenerator.LIT);
            String id = generator.getId().getPath();
            ResourceLocation model = models().orientable(generator.getRegisteredName() + (lit ? "_on" : ""), modLoc("block/machines/"+id+"/side"), modLoc("block/machines/"+id+"/front" + (lit?"_on":"")), modLoc("block/machines/"+id+"/top")).getUncheckedLocation();
            return new ModelFile.UncheckedModelFile(model);
        });
    }
}

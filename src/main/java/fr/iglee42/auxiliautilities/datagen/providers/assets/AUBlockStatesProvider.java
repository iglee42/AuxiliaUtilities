package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockEnchanter;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
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
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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

        simpleBlock(AUBlocks.ENCHANTER.get(),new ModelFile.UncheckedModelFile(models().cubeBottomTop(AUBlocks.ENCHANTER.getRegisteredName(), modLoc("block/machines/enchanter_side"),MACHINE_BOTTOM,modLoc("block/machines/enchanter_top")).getUncheckedLocation()));
    }
}

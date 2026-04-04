package fr.iglee42.auxiliautilities.blockentities;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.generators.*;
import fr.iglee42.auxiliautilities.blockentities.gp.consumers.BEResonator;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.*;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AUBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AuxiliaUtilities.MODID);

    private static final List<DeferredHolder<BlockEntityType<?>,?>> GENERATORS = new ArrayList<>();

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEManualMill>> MANUAL_MILL = register("manual_mill",BEManualMill::new, AUBlocks.MANUAL_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BESolarPanel>> SOLAR_PANEL = register("solar_panel", BESolarPanel::new, AUBlocks.SOLAR_PANEL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BELunarPanel>> LUNAR_PANEL = register("lunar_panel", BELunarPanel::new, AUBlocks.LUNAR_PANEL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEFireMill>> FIRE_MILL = register("fire_mill",BEFireMill::new, AUBlocks.FIRE_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEWaterMill>> WATER_MILL = register("water_mill",BEWaterMill::new, AUBlocks.WATER_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEWindMill>> WIND_MILL = register("wind_mill",BEWindMill::new, AUBlocks.WIND_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BELavaMill>> LAVA_MILL = register("lava_mill",BELavaMill::new, AUBlocks.LAVA_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEDragonEggMill>> DRAGON_EGG_MILL = register("dragon_egg_mill",BEDragonEggMill::new, AUBlocks.DRAGON_EGG_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BECreativeMill>> CREATIVE_MILL = register("creative_mill",BECreativeMill::new, AUBlocks.CREATIVE_MILL);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEResonator>> RESONATOR = register("resonator",BEResonator::new, AUBlocks.RESONATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEEnchanter>> ENCHANTER = register("enchanter",BEEnchanter::new, AUBlocks.ENCHANTER);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEFurnace>> FURNACE = register("furnace", BEFurnace::new, AUBlocks.FURNACE);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BECrusher>> CRUSHER = register("crusher", BECrusher::new, AUBlocks.CRUSHER);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenSurvival>> SURVIVAL_GENERATOR = registerGenerator("survival_generator", BEGenSurvival::new, AUBlocks.SURVIVAL_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenFurnace>> FURNACE_GENERATOR = registerGenerator("furnace_generator", BEGenFurnace::new, AUBlocks.FURNACE_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenOverclocked>> OVERCLOCKED_GENERATOR = registerGenerator("overclocked_generator", BEGenOverclocked::new, AUBlocks.OVERCLOCKED_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenCulinary>> CULINARY_GENERATOR = registerGenerator("culinary_generator", BEGenCulinary::new, AUBlocks.CULINARY_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenPotion>> POTION_GENERATOR = registerGenerator("potion_generator", BEGenPotion::new, AUBlocks.POTION_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenMagmatic>> MAGMATIC_GENERATOR = registerGenerator("magmatic_generator", BEGenMagmatic::new, AUBlocks.MAGMATIC_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenSlimey>> SLIMEY_GENERATOR = registerGenerator("slimey_generator", BEGenSlimey::new, AUBlocks.SLIMEY_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenPink>> PINK_GENERATOR = registerGenerator("pink_generator", BEGenPink::new, AUBlocks.PINK_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenDeath>> DEATH_GENERATOR = registerGenerator("death_generator", BEGenDeath::new, AUBlocks.DEATH_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenExplosive>> EXPLOSIVE_GENERATOR = registerGenerator("explosive_generator", BEGenExplosive::new, AUBlocks.EXPLOSIVE_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenHeatedRedstone>> HEATED_REDSTONE_GENERATOR = registerGenerator("heated_redstone_generator", BEGenHeatedRedstone::new, AUBlocks.HEATED_REDSTONE_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenEnder>> ENDER_GENERATOR = registerGenerator("ender_generator", BEGenEnder::new, AUBlocks.ENDER_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenDisenchantment>> DISENCHANTMENT_GENERATOR = registerGenerator("disenchantment_generator", BEGenDisenchantment::new, AUBlocks.DISENCHANTMENT_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenFrosty>> FROSTY_GENERATOR = registerGenerator("frosty_generator", BEGenFrosty::new, AUBlocks.FROSTY_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenHalitosis>> HALITOSIS_GENERATOR = registerGenerator("halitosis_generator", BEGenHalitosis::new, AUBlocks.HALITOSIS_GENERATOR);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEGenNetherStar>> NETHER_STAR_GENERATOR = registerGenerator("nether_star_generator", BEGenNetherStar::new, AUBlocks.NETHER_STAR_GENERATOR);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BERainbowGenerator>> RAINBOW_GENERATOR = register("rainbow_generator", BERainbowGenerator::new, AUBlocks.RAINBOW_GENERATOR);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEOpiniumCore>> OPINIUM_CORE = register("opinium_core", BEOpiniumCore::new, AUBlocks.MISERABLE_OPINIUM_CORE,AUBlocks.PATHETIC_OPINIUM_CORE,AUBlocks.MEDIOCRE_OPINIUM_CORE,AUBlocks.PASSABLE_OPINIUM_CORE,AUBlocks.DECENT_OPINIUM_CORE,AUBlocks.SOLID_OPINIUM_CORE,AUBlocks.GOOD_OPINIUM_CORE,AUBlocks.DAMN_GOOD_OPINIUM_CORE,AUBlocks.AMAZING_OPINIUM_CORE,AUBlocks.INSPIRING_OPINIUM_CORE,AUBlocks.PERFECTED_OPINIUM_CORE);

    private static <T extends AUBlockEntity> DeferredHolder<BlockEntityType<?>,BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, DeferredHolder<Block, ? extends Block>... block){
        Supplier<List<Block>> blocks = ()->Arrays.stream(block).map(holder -> (Block) holder.get()).toList();
        return BLOCK_ENTITY_TYPES.register(name,()->BlockEntityType.Builder.of(supplier, blocks.get().toArray(new Block[]{})).build(null));
    }

    private static <T extends AUGeneratorBlockEntity> DeferredHolder<BlockEntityType<?>,BlockEntityType<T>> registerGenerator(String name, BlockEntityType.BlockEntitySupplier<T> supplier, DeferredHolder<Block, ? extends Block> block){
        var holder =  BLOCK_ENTITY_TYPES.register(name,()->BlockEntityType.Builder.of(supplier,block.get()).build(null));
        GENERATORS.addLast( holder);
        return holder;
    }

    public static List<DeferredHolder<BlockEntityType<?>, ?>> getGenerators() {
        return Collections.unmodifiableList(GENERATORS);
    }
}

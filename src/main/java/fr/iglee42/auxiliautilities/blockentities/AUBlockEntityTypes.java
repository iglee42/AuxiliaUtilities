package fr.iglee42.auxiliautilities.blockentities;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.gp.consumers.BEResonator;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.*;
import fr.iglee42.auxiliautilities.blockentities.BEEnchanter;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AUBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AuxiliaUtilities.MODID);

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

    private static <T extends AUBlockEntity> DeferredHolder<BlockEntityType<?>,BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, DeferredHolder<Block, ? extends Block> block){
        return BLOCK_ENTITY_TYPES.register(name,()->BlockEntityType.Builder.of(supplier,block.get()).build(null));
    }
}

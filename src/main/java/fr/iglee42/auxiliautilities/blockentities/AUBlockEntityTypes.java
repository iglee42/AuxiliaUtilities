package fr.iglee42.auxiliautilities.blockentities;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.gp.consumers.BEResonator;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.BECreativeMill;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.BEManualMill;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AUBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AuxiliaUtilities.MODID);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEManualMill>> MANUAL_MILL = register("manual_mill",BEManualMill::new, AUBlocks.MANUAL_MILL);
    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BECreativeMill>> CREATIVE_MILL = register("creative_mill",BECreativeMill::new, AUBlocks.CREATIVE_MILL);

    public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<BEResonator>> RESONATOR = register("resonator",BEResonator::new, AUBlocks.RESONATOR);

    private static <T extends AUBlockEntity> DeferredHolder<BlockEntityType<?>,BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, DeferredHolder<Block, ? extends Block> block){
        return BLOCK_ENTITY_TYPES.register(name,()->BlockEntityType.Builder.of(supplier,block.get()).build(null));
    }
}

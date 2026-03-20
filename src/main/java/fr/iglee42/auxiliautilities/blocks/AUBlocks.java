package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.items.ItemAngelBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class AUBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AuxiliaUtilities.MODID);

    public static final DeferredBlock<Block> ANGEL_BLOCK = createBlockWithCustomItem("angel_block",()-> new BlockAngel(BlockBehaviour.Properties.of().instabreak()),block->new ItemAngelBlock(block,new Item.Properties()));


    private static <T extends Block> DeferredBlock<T> createBlockWithoutItem(String name, Supplier<T> supplier){
        return BLOCKS.register(name, supplier);
    }

    private static <T extends Block> DeferredBlock<T> createBlock(String name, Supplier<T> supplier){
        DeferredBlock<T> block = createBlockWithoutItem(name, supplier);
        AUItems.ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties()));
        return block;
    }

    private static <T extends Block> DeferredBlock<T> createBlockWithCustomItem(String name, Supplier<T> supplier, Function<T, ? extends Item> itemFactory){
        DeferredBlock<T> block = createBlockWithoutItem(name, supplier);
        AUItems.ITEMS.register(name,()->itemFactory.apply(block.get()));
        return block;
    }
}

package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.gp.consumers.BlockResonator;
import fr.iglee42.auxiliautilities.blocks.gp.generators.BlockCreativeMill;
import fr.iglee42.auxiliautilities.blocks.gp.generators.BlockManualMill;
import fr.iglee42.auxiliautilities.items.AUBlockItem;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.items.ItemAngelBlock;
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

    public static final DeferredBlock<Block> DEMON_BLOCK = createBlock("demon_block",()-> new Block(BlockBehaviour.Properties.of().instabreak()));
    public static final DeferredBlock<Block> ENCHANTED_BLOCK = createBlock("enchanted_block",()-> new Block(BlockBehaviour.Properties.of().instabreak()));
    public static final DeferredBlock<Block> EVIL_INFUSED_IRON_BLOCK = createBlock("evil_infused_iron_block",()-> new Block(BlockBehaviour.Properties.of().instabreak()));

    //public static final DecorativeBlockSet BORDER_STONE = new DecorativeBlockSet("border_stone");
    //public static final DecorativeBlockSet CROSSED_STONE = new DecorativeBlockSet("crossed_stone");
    //public static final DecorativeBlockSet POLISHED_STONE = new DecorativeBlockSet("polished_stone");
    public static final DecorativeBlockSet STONEBURNT = new DecorativeBlockSet("stoneburnt");
    public static final DecorativeBlockSet QUARTZBURNT = new DecorativeBlockSet("quartzburnt");
    public static final DecorativeBlockSet RAINBOW_STONE = new DecorativeBlockSet("rainbow_stone");

    public static final DeferredBlock<BlockManualMill> MANUAL_MILL = createBlock("manual_mill",()->new BlockManualMill(BlockBehaviour.Properties.of().noOcclusion()));
    public static final DeferredBlock<BlockCreativeMill> CREATIVE_MILL = createBlock("creative_mill",()->new BlockCreativeMill(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<BlockResonator> RESONATOR = createBlock("resonator",()->new BlockResonator(BlockBehaviour.Properties.of().noOcclusion()));

    protected static <T extends Block> DeferredBlock<T> createBlockWithoutItem(String name, Supplier<T> supplier){
        return BLOCKS.register(name, supplier);
    }

    protected static <T extends Block> DeferredBlock<T> createBlock(String name, Supplier<T> supplier){
        DeferredBlock<T> block = createBlockWithoutItem(name, supplier);
        AUItems.ITEMS.register(name,()->new AUBlockItem(block.get(),new Item.Properties()));
        return block;
    }

    protected static <T extends Block> DeferredBlock<T> createBlockWithCustomItem(String name, Supplier<T> supplier, Function<T, ? extends Item> itemFactory){
        DeferredBlock<T> block = createBlockWithoutItem(name, supplier);
        AUItems.ITEMS.register(name,()->itemFactory.apply(block.get()));
        return block;
    }
}

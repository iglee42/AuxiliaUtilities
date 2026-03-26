package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.gp.consumers.BlockResonator;
import fr.iglee42.auxiliautilities.blocks.gp.generators.*;
import fr.iglee42.auxiliautilities.items.AUBlockItem;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.items.ItemAngelBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class AUBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AuxiliaUtilities.MODID);

    public static final DeferredBlock<Block> ANGEL_BLOCK = createBlockWithCustomItem("angel_block",()-> new BlockAngel(BlockBehaviour.Properties.of().instabreak()), block->new ItemAngelBlock(block,new Item.Properties()));

    public static final DeferredBlock<Block> DEMON_BLOCK = createBlock("demon_block",()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(5,6)));
    public static final DeferredBlock<Block> ENCHANTED_BLOCK = createBlock("enchanted_block",()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(5,6)));
    public static final DeferredBlock<Block> EVIL_INFUSED_IRON_BLOCK = createBlock("evil_infused_iron_block",()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(5,6)));

    public static final DecorativeBlockSet BORDER_STONE = new DecorativeBlockSet("border_stone");
    public static final DecorativeBlockSet CROSSED_STONE = new DecorativeBlockSet("crossed_stone");
    public static final DecorativeBlockSet POLISHED_STONE = new DecorativeBlockSet("polished_stone");
    public static final DecorativeBlockSet STONEBURNT = new DecorativeBlockSet("stoneburnt");
    public static final DecorativeBlockSet QUARTZBURNT = new DecorativeBlockSet("quartzburnt");
    public static final DecorativeBlockSet RAINBOW_STONE = new DecorativeBlockSet("rainbow_stone");

    public static final DeferredBlock<BlockMagicalWood> MAGICAL_WOOD = createBlock("magical_wood",()->new BlockMagicalWood(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredBlock<Block> MAGICAL_PLANKS = createBlock("magical_planks",()->new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<Block> DIAGONAL_WOOD = createBlock("diagonal_wood",()->new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final DeferredBlock<BlockManualMill> MANUAL_MILL = createBlock("manual_mill",()->new BlockManualMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockSolarPanel> SOLAR_PANEL = createBlock("solar_panel",()->new BlockSolarPanel(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockLunarPanel> LUNAR_PANEL = createBlock("lunar_panel",()->new BlockLunarPanel(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockFireMill> FIRE_MILL = createBlock("fire_mill",()->new BlockFireMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockWaterMill> WATER_MILL = createBlock("water_mill",()->new BlockWaterMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockWindMill> WIND_MILL = createBlock("wind_mill",()->new BlockWindMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockLavaMill> LAVA_MILL = createBlock("lava_mill",()->new BlockLavaMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockDragonEggMill> DRAGON_EGG_MILL = createBlock("dragon_egg_mill",()->new BlockDragonEggMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<BlockCreativeMill> CREATIVE_MILL = createBlock("creative_mill",()->new BlockCreativeMill(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));

    public static final DeferredBlock<BlockSoundMuffler> SOUND_MUFFLER = createBlock("sound_muffler",()->new BlockSoundMuffler(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final DeferredBlock<BlockResonator> RESONATOR = createBlock("resonator",()->new BlockResonator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final DeferredBlock<BlockEnderLilly> ENDER_LILLY = createBlock("ender_lilly",()->new BlockEnderLilly(BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES)));

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

package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.generators.BEGenHeatedRedstone;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.glass.AUBlockDarkGlass;
import fr.iglee42.auxiliautilities.blocks.glass.AUBlockGlass;
import fr.iglee42.auxiliautilities.blocks.glass.AUBlockRedstoneGlass;
import fr.iglee42.auxiliautilities.blocks.gp.consumers.BlockEnderPorcupine;
import fr.iglee42.auxiliautilities.blocks.gp.consumers.BlockResonator;
import fr.iglee42.auxiliautilities.blocks.gp.generators.*;
import fr.iglee42.auxiliautilities.items.AUBlockItem;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.items.ItemAngelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static final DeferredBlock<Block> MACHINE_BLOCK = createBlock("machine_block",()->new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<BlockSoundMuffler> SOUND_MUFFLER = createBlock("sound_muffler",()->new BlockSoundMuffler(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).noOcclusion()));
    public static final DeferredBlock<BlockResonator> RESONATOR = createBlock("resonator",()->new BlockResonator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredBlock<BlockEnchanter> ENCHANTER = createBlock("enchanter",()->new BlockEnchanter(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredBlock<BlockFurnace> FURNACE = createBlock("furnace", () -> new BlockFurnace(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredBlock<BlockCrusher> CRUSHER = createBlock("crusher", () -> new BlockCrusher(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final DeferredBlock<BlockEnderPorcupine> ENDER_PORCUPINE = createBlock("ender_porcupine",()->new BlockEnderPorcupine(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final DeferredBlock<BlockEnderLilly> ENDER_LILLY = createBlock("ender_lilly",()->new BlockEnderLilly(BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES)));
    public static final DeferredBlock<BlockRedOrchid> RED_ORCHID = createBlock("red_orchid",()->new BlockRedOrchid(BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES)));

    public static final CompressedBlockSet COMPRESSED_COBBLESTONE = new CompressedBlockSet(Blocks.COBBLESTONE,8);
    public static final CompressedBlockSet COMPRESSED_DIRT = new CompressedBlockSet(Blocks.DIRT,4);
    public static final CompressedBlockSet COMPRESSED_COBBLED_DEEPSLATE = new CompressedBlockSet(Blocks.COBBLED_DEEPSLATE,8);
    public static final CompressedBlockSet COMPRESSED_SAND = new CompressedBlockSet(Blocks.SAND,2);
    public static final CompressedBlockSet COMPRESSED_GRAVEL = new CompressedBlockSet(Blocks.GRAVEL,2);
    public static final CompressedBlockSet COMPRESSED_NETHERRACK = new CompressedBlockSet(Blocks.NETHERRACK,6);
    public static final CompressedBlockSet COMPRESSED_BLACKSTONE = new CompressedBlockSet(Blocks.BLACKSTONE,8);
    public static final CompressedBlockSet COMPRESSED_END_STONE = new CompressedBlockSet(Blocks.END_STONE,6);

    public static final DeferredBlock<BlockRedstoneClock> REDSTONE_CLOCK = createBlock("redstone_clock",()->new BlockRedstoneClock(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).noOcclusion()));

    public static final DeferredBlock<BlockGenerator> SURVIVAL_GENERATOR = createBlock("survival_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.SURVIVAL_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> FURNACE_GENERATOR = createBlock("furnace_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.FURNACE_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> OVERCLOCKED_GENERATOR = createBlock("overclocked_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.OVERCLOCKED_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> CULINARY_GENERATOR = createBlock("culinary_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.CULINARY_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> POTION_GENERATOR = createBlock("potion_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.POTION_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> MAGMATIC_GENERATOR = createBlock("magmatic_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.MAGMATIC_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> SLIMEY_GENERATOR = createBlock("slimey_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.SLIMEY_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> PINK_GENERATOR = createBlock("pink_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.PINK_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> DEATH_GENERATOR = createBlock("death_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.DEATH_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> EXPLOSIVE_GENERATOR = createBlock("explosive_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.EXPLOSIVE_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> HEATED_REDSTONE_GENERATOR = createBlock("heated_redstone_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> ENDER_GENERATOR = createBlock("ender_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.ENDER_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> DISENCHANTMENT_GENERATOR = createBlock("disenchantment_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.DISENCHANTMENT_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> FROSTY_GENERATOR = createBlock("frosty_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.FROSTY_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> HALITOSIS_GENERATOR = createBlock("halitosis_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.HALITOSIS_GENERATOR.get()));
    public static final DeferredBlock<BlockGenerator> NETHER_STAR_GENERATOR = createBlock("nether_star_generator",()->new BlockGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.NETHER_STAR_GENERATOR.get()));

    public static final DeferredBlock<BlockRainbowGenerator> RAINBOW_GENERATOR = createBlockWithCustomItem("rainbow_generator", () -> new BlockRainbowGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()),block->new AUBlockItem(block,new Item.Properties()){
        @Override
        public Component getName(ItemStack p_41458_) {
            return getBlock().getName();
        }
    });

    public static final Map<BlockOpiniumCore.Tier,DeferredBlock<BlockOpiniumCore>> OPINIUM_CORE = new HashMap<>();
    public static final DeferredBlock<BlockOpiniumCore> MISERABLE_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.MISERABLE);
    public static final DeferredBlock<BlockOpiniumCore> PATHETIC_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.PATHETIC);
    public static final DeferredBlock<BlockOpiniumCore> MEDIOCRE_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.MEDIOCRE);
    public static final DeferredBlock<BlockOpiniumCore> PASSABLE_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.PASSABLE);
    public static final DeferredBlock<BlockOpiniumCore> DECENT_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.DECENT);
    public static final DeferredBlock<BlockOpiniumCore> SOLID_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.SOLID);
    public static final DeferredBlock<BlockOpiniumCore> GOOD_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.GOOD);
    public static final DeferredBlock<BlockOpiniumCore> DAMN_GOOD_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.DAMN_GOOD);
    public static final DeferredBlock<BlockOpiniumCore> AMAZING_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.AMAZING);
    public static final DeferredBlock<BlockOpiniumCore> INSPIRING_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.INSPIRING);
    public static final DeferredBlock<BlockOpiniumCore> PERFECTED_OPINIUM_CORE = createOpiniumCore(BlockOpiniumCore.Tier.PERFECTED);

    public static final DeferredBlock<BlockKleinBottle> KLEIN_BOTTLE = createBlock("klein_bottle",()->new BlockKleinBottle(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));

    public static final DeferredBlock<BlockDrum> STONE_DRUM = createBlock("stone_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion(), ()->AUBlockEntityTypes.STONE_DRUM.get()));
    public static final DeferredBlock<BlockDrum> COPPER_DRUM = createBlock("copper_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.COPPER_DRUM.get()));
    public static final DeferredBlock<BlockDrum> IRON_DRUM = createBlock("iron_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.IRON_DRUM.get()));
    public static final DeferredBlock<BlockDrum> REINFORCED_LARGE_DRUM = createBlock("reinforced_large_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.REINFORCED_LARGE_DRUM.get()));
    public static final DeferredBlock<BlockDrum> NETHERITE_DRUM = createBlock("netherite_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.NETHERITE_DRUM.get()));
    public static final DeferredBlock<BlockDrum> DEMONICALLY_GARGANTUAN_DRUM = createBlock("demonically_gargantuan_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion(), ()->AUBlockEntityTypes.DEMONICALLY_GARGANTUAN_DRUM.get()));
    public static final DeferredBlock<BlockDrum> CREATIVE_DRUM = createBlock("creative_drum",()->new BlockDrum(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion(), ()->AUBlockEntityTypes.CREATIVE_DRUM.get()));

    public static final DeferredBlock<AUBlock> SANDY_GLASS = createBlock("sandy_glass",()->new AUBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<AUBlockGlass> THICKENED_GLASS = createBlock("thickened_glass",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    public static final DeferredBlock<AUBlockGlass> THICKENED_GLASS_BORDERED = createBlock("thickened_glass_bordered",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    public static final DeferredBlock<AUBlockGlass> THICKENED_GLASS_PATTERNED = createBlock("thickened_glass_patterned",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    public static final DeferredBlock<AUBlockDarkGlass> DARK_GLASS = createBlock("dark_glass",()->new AUBlockDarkGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    public static final DeferredBlock<AUBlockGlass> GLOWING_GLASS = createBlock("glowing_glass",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel($->15).noOcclusion()));
    public static final DeferredBlock<AUBlockRedstoneGlass> REDSTONE_GLASS = createBlock("redstone_glass",()->new AUBlockRedstoneGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));
    public static final DeferredBlock<AUBlockGlass> ETHEREAL_GLASS = createBlock("ethereal_glass",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()){
        @Override
        protected boolean blockEntity(Entity entity) {
            return !(entity instanceof Player);
        }
    });
    public static final DeferredBlock<AUBlockGlass> REVERSE_ETHEREAL_GLASS = createBlock("reverse_ethereal_glass",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()){
        @Override
        protected boolean blockEntity(Entity entity) {
            return entity instanceof Player;
        }
    });
    public static final DeferredBlock<AUBlockGlass> INEFFABLE_GLASS = createBlock("ineffable_glass",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()){
        @Override
        protected boolean blockEntity(Entity entity) {
            return !(entity instanceof Player);
        }
    });
    public static final DeferredBlock<AUBlockGlass> DARK_INEFFABLE_GLASS = createBlock("dark_ineffable_glass",()->new AUBlockDarkGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()){
        @Override
        protected boolean blockEntity(Entity entity) {
            return !(entity instanceof Player);
        }
    });
    public static final DeferredBlock<AUBlockGlass> OBSIDIAN_GLASS = createBlock("obsidian_glass",()->new AUBlockGlass(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).strength(0.3F,1200.0F).noOcclusion()));

    public static final DeferredBlock<BlockSpike> WOODEN_SPIKE = createBlock("wooden_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).noOcclusion(), BlockSpike.SpikeType.WOOD));
    public static final DeferredBlock<BlockSpike> STONE_SPIKE = createBlock("stone_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion(), BlockSpike.SpikeType.STONE));
    public static final DeferredBlock<BlockSpike> COPPER_SPIKE = createBlock("copper_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion(), BlockSpike.SpikeType.COPPER));
    public static final DeferredBlock<BlockSpike> IRON_SPIKE = createBlock("iron_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), BlockSpike.SpikeType.IRON));
    public static final DeferredBlock<BlockSpike> GOLDEN_SPIKE = createBlock("golden_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion(), BlockSpike.SpikeType.GOLDEN));
    public static final DeferredBlock<BlockSpike> DIAMOND_SPIKE = createBlock("diamond_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion(), BlockSpike.SpikeType.DIAMOND));
    public static final DeferredBlock<BlockSpike> NETHERITE_SPIKE = createBlock("netherite_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion(), BlockSpike.SpikeType.NETHERITE));
    public static final DeferredBlock<BlockSpike> CREATIVE_SPIKE = createBlock("creative_spike",()->new BlockSpike(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion(), BlockSpike.SpikeType.CREATIVE));

    private static  DeferredBlock<BlockOpiniumCore> createOpiniumCore(BlockOpiniumCore.Tier tier){
        DeferredBlock<BlockOpiniumCore> block = createBlock(tier.name().toLowerCase() + "_opinium_core", () -> new BlockOpiniumCore(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), tier));
        OPINIUM_CORE.put(tier,block);
        return block;
    }
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

    public static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_) {
        return false;
    }

}

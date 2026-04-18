package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.cursedearth.BlockCursedEarth;
import fr.iglee42.auxiliautilities.blocks.glass.AUBlockDarkGlass;
import fr.iglee42.auxiliautilities.blocks.glass.AUBlockGlass;
import fr.iglee42.auxiliautilities.blocks.glass.AUBlockRedstoneGlass;
import fr.iglee42.auxiliautilities.blocks.gp.consumers.BlockEnderPorcupine;
import fr.iglee42.auxiliautilities.blocks.gp.consumers.BlockResonator;
import fr.iglee42.auxiliautilities.blocks.gp.generators.*;
import fr.iglee42.auxiliautilities.items.api.AUBlockItem;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.items.ItemAngelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class AUBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AuxiliaUtilities.MODID);

    public static final DeferredBlock<Block> ANGEL_BLOCK = createBlockWithCustomItem("angel_block",BlockAngel::new,BlockBehaviour.Properties.of().instabreak(), (block,p)->new ItemAngelBlock(block,p));

    public static final DeferredBlock<Block> DEMON_BLOCK = createBlock("demon_block",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(5,6));
    public static final DeferredBlock<Block> ENCHANTED_BLOCK = createBlock("enchanted_block",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(5,6));
    public static final DeferredBlock<Block> EVIL_INFUSED_IRON_BLOCK = createBlock("evil_infused_iron_block",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).strength(5,6));

    public static final DecorativeBlockSet BORDER_STONE = new DecorativeBlockSet("border_stone");
    public static final DecorativeBlockSet CROSSED_STONE = new DecorativeBlockSet("crossed_stone");
    public static final DecorativeBlockSet POLISHED_STONE = new DecorativeBlockSet("polished_stone");
    public static final DecorativeBlockSet STONEBURNT = new DecorativeBlockSet("stoneburnt");
    public static final DecorativeBlockSet QUARTZBURNT = new DecorativeBlockSet("quartzburnt");
    public static final DecorativeBlockSet RAINBOW_STONE = new DecorativeBlockSet("rainbow_stone");

    public static final DeferredBlock<BlockMagicalWood> MAGICAL_WOOD = createBlock("magical_wood",BlockMagicalWood::new,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD));
    public static final DeferredBlock<Block> MAGICAL_PLANKS = createBlock("magical_planks",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));
    public static final DeferredBlock<Block> DIAGONAL_WOOD = createBlock("diagonal_wood",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS));

    public static final DeferredBlock<BlockManualMill> MANUAL_MILL = createBlock("manual_mill",BlockManualMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockSolarPanel> SOLAR_PANEL = createBlock("solar_panel",BlockSolarPanel::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockLunarPanel> LUNAR_PANEL = createBlock("lunar_panel",BlockLunarPanel::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockFireMill> FIRE_MILL = createBlock("fire_mill",BlockFireMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockWaterMill> WATER_MILL = createBlock("water_mill",BlockWaterMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockWindMill> WIND_MILL = createBlock("wind_mill",BlockWindMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockLavaMill> LAVA_MILL = createBlock("lava_mill",BlockLavaMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockDragonEggMill> DRAGON_EGG_MILL = createBlock("dragon_egg_mill",BlockDragonEggMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());
    public static final DeferredBlock<BlockCreativeMill> CREATIVE_MILL = createBlock("creative_mill",BlockCreativeMill::new,BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion());

    public static final DeferredBlock<Block> MACHINE_BLOCK = createBlock("machine_block",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final DeferredBlock<BlockSoundMuffler> SOUND_MUFFLER = createBlock("sound_muffler",BlockSoundMuffler::new,BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).noOcclusion());
    public static final DeferredBlock<BlockResonator> RESONATOR = createBlock("resonator",BlockResonator::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockEnchanter> ENCHANTER = createBlock("enchanter",BlockEnchanter::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockFurnace> FURNACE = createBlock("furnace", BlockFurnace::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockCrusher> CRUSHER = createBlock("crusher", BlockCrusher::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());

    public static final DeferredBlock<BlockEnderPorcupine> ENDER_PORCUPINE = createBlock("ender_porcupine",BlockEnderPorcupine::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());

    public static final DeferredBlock<BlockEnderLilly> ENDER_LILLY = createBlock("ender_lilly",BlockEnderLilly::new,BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES));
    public static final DeferredBlock<BlockRedOrchid> RED_ORCHID = createBlock("red_orchid",BlockRedOrchid::new,BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES));

    public static final CompressedBlockSet COMPRESSED_COBBLESTONE = new CompressedBlockSet(Blocks.COBBLESTONE,8);
    public static final CompressedBlockSet COMPRESSED_DIRT = new CompressedBlockSet(Blocks.DIRT,4);
    public static final CompressedBlockSet COMPRESSED_COBBLED_DEEPSLATE = new CompressedBlockSet(Blocks.COBBLED_DEEPSLATE,8);
    public static final CompressedBlockSet COMPRESSED_SAND = new CompressedBlockSet(Blocks.SAND,2);
    public static final CompressedBlockSet COMPRESSED_GRAVEL = new CompressedBlockSet(Blocks.GRAVEL,2);
    public static final CompressedBlockSet COMPRESSED_NETHERRACK = new CompressedBlockSet(Blocks.NETHERRACK,6);
    public static final CompressedBlockSet COMPRESSED_BLACKSTONE = new CompressedBlockSet(Blocks.BLACKSTONE,8);
    public static final CompressedBlockSet COMPRESSED_END_STONE = new CompressedBlockSet(Blocks.END_STONE,6);

    public static final DeferredBlock<BlockRedstoneClock> REDSTONE_CLOCK = createBlock("redstone_clock",BlockRedstoneClock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).noOcclusion());

    public static final DeferredBlock<BlockGenerator> SURVIVAL_GENERATOR = createBlock("survival_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.SURVIVAL_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> FURNACE_GENERATOR = createBlock("furnace_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.FURNACE_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> OVERCLOCKED_GENERATOR = createBlock("overclocked_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.OVERCLOCKED_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> CULINARY_GENERATOR = createBlock("culinary_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.CULINARY_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> POTION_GENERATOR = createBlock("potion_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.POTION_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> MAGMATIC_GENERATOR = createBlock("magmatic_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.MAGMATIC_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> SLIMEY_GENERATOR = createBlock("slimey_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.SLIMEY_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> PINK_GENERATOR = createBlock("pink_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.PINK_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> DEATH_GENERATOR = createBlock("death_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.DEATH_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> EXPLOSIVE_GENERATOR = createBlock("explosive_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.EXPLOSIVE_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> HEATED_REDSTONE_GENERATOR = createBlock("heated_redstone_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.HEATED_REDSTONE_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> ENDER_GENERATOR = createBlock("ender_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.ENDER_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> DISENCHANTMENT_GENERATOR = createBlock("disenchantment_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.DISENCHANTMENT_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> FROSTY_GENERATOR = createBlock("frosty_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.FROSTY_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> HALITOSIS_GENERATOR = createBlock("halitosis_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.HALITOSIS_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockGenerator> NETHER_STAR_GENERATOR = createBlock("nether_star_generator",p->new BlockGenerator(p, ()->AUBlockEntityTypes.NETHER_STAR_GENERATOR.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());

    public static final DeferredBlock<BlockRainbowGenerator> RAINBOW_GENERATOR = createBlockWithCustomItem("rainbow_generator", BlockRainbowGenerator::new,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(),(block,p)->new AUBlockItem(block,p){
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

    public static final DeferredBlock<BlockKleinBottle> KLEIN_BOTTLE = createBlock("klein_bottle", BlockKleinBottle::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());

    public static final DeferredBlock<BlockDrum> STONE_DRUM = createBlock("stone_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.STONE_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion());
    public static final DeferredBlock<BlockDrum> COPPER_DRUM = createBlock("copper_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.COPPER_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockDrum> IRON_DRUM = createBlock("iron_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.IRON_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockDrum> REINFORCED_LARGE_DRUM = createBlock("reinforced_large_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.REINFORCED_LARGE_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockDrum> NETHERITE_DRUM = createBlock("netherite_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.NETHERITE_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockDrum> DEMONICALLY_GARGANTUAN_DRUM = createBlock("demonically_gargantuan_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.DEMONICALLY_GARGANTUAN_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockDrum> CREATIVE_DRUM = createBlock("creative_drum",p->new BlockDrum(p, ()->AUBlockEntityTypes.CREATIVE_DRUM.get()),BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion());

    public static final DeferredBlock<AUBlock> SANDY_GLASS = createBlock("sandy_glass",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS));
    public static final DeferredBlock<AUBlockGlass> THICKENED_GLASS = createBlock("thickened_glass",AUBlockGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> THICKENED_GLASS_BORDERED = createBlock("thickened_glass_bordered",AUBlockGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> THICKENED_GLASS_PATTERNED = createBlock("thickened_glass_patterned",AUBlockGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockDarkGlass> DARK_GLASS = createBlock("dark_glass",AUBlockDarkGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> GLOWING_GLASS = createBlock("glowing_glass",AUBlockGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel($->15).noOcclusion());
    public static final DeferredBlock<AUBlockRedstoneGlass> REDSTONE_GLASS = createBlock("redstone_glass",AUBlockRedstoneGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> ETHEREAL_GLASS = createBlock("ethereal_glass",p->new AUBlockGlass(p){
        @Override
        protected boolean blockEntity(Entity entity) {
            return !(entity instanceof Player);
        }
    },BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> REVERSE_ETHEREAL_GLASS = createBlock("reverse_ethereal_glass",p->new AUBlockGlass(p){
        @Override
        protected boolean blockEntity(Entity entity) {
            return entity instanceof Player;
        }
    },BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> INEFFABLE_GLASS = createBlock("ineffable_glass",p->new AUBlockGlass(p){
        @Override
        protected boolean blockEntity(Entity entity) {
            return !(entity instanceof Player);
        }
    },BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> DARK_INEFFABLE_GLASS = createBlock("dark_ineffable_glass",p->new AUBlockDarkGlass(p){
        @Override
        protected boolean blockEntity(Entity entity) {
            return !(entity instanceof Player);
        }
    },BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion());
    public static final DeferredBlock<AUBlockGlass> OBSIDIAN_GLASS = createBlock("obsidian_glass",AUBlockGlass::new,BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).strength(0.3F,1200.0F).noOcclusion());

    public static final DeferredBlock<BlockSpike> WOODEN_SPIKE = createBlock("wooden_spike",p->new BlockSpike(p, BlockSpike.SpikeType.WOOD),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).noOcclusion());
    public static final DeferredBlock<BlockSpike> STONE_SPIKE = createBlock("stone_spike",p->new BlockSpike(p, BlockSpike.SpikeType.STONE),BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion());
    public static final DeferredBlock<BlockSpike> COPPER_SPIKE = createBlock("copper_spike",p->new BlockSpike(p, BlockSpike.SpikeType.COPPER),BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockSpike> IRON_SPIKE = createBlock("iron_spike",p->new BlockSpike(p, BlockSpike.SpikeType.IRON),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockSpike> GOLDEN_SPIKE = createBlock("golden_spike",p->new BlockSpike(p, BlockSpike.SpikeType.GOLDEN),BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockSpike> DIAMOND_SPIKE = createBlock("diamond_spike",p->new BlockSpike(p, BlockSpike.SpikeType.DIAMOND),BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockSpike> NETHERITE_SPIKE = createBlock("netherite_spike",p->new BlockSpike(p, BlockSpike.SpikeType.NETHERITE),BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion());
    public static final DeferredBlock<BlockSpike> CREATIVE_SPIKE = createBlock("creative_spike",p->new BlockSpike(p, BlockSpike.SpikeType.CREATIVE),BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK).noOcclusion());

    public static final DeferredBlock<BlockCursedEarth> CURSED_EARTH = createBlock("cursed_earth",BlockCursedEarth::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.6F)
            .randomTicks()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.NORMAL));

    public static final DeferredBlock<AUBlock> CLIMOGRAPH_BLOCK = createBlock("climograph_block", AUBlock::new,BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()));
    public static final DeferredBlock<BlockTerraformer> TERRAFORMER = createBlock("terraformer", BlockTerraformer::new,BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()));
    public static final DeferredBlock<BlockTerraformerExtension> COOLER = createBlock("cooler",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.COOLER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<BlockTerraformerExtension> HEATER = createBlock("heater",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.HEATER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<BlockTerraformerExtension> HUMIDIFIER = createBlock("humidifier",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.HUMIDIFIER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<BlockTerraformerExtension> DEHUMIDIFIER = createBlock("dehumidifier",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.DEHUMIDIFIER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<BlockTerraformerExtension> MAGIC_INFUSER = createBlock("magic_infuser",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.MAGIC_INFUSER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<BlockTerraformerExtension> MAGIC_ABSORBER = createBlock("magic_absorber",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.MAGIC_ABSORBER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<BlockTerraformerExtension> DESHOSTILIFIER = createBlock("deshostilifier",p->new BlockTerraformerExtension(p, AUBlockEntityTypes.DESHOSTILIFIER),BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());
    public static final DeferredBlock<AUBlock> ANTENNA = createBlock("antenna",AUBlock::new,BlockBehaviour.Properties.ofFullCopy(MACHINE_BLOCK.get()).noOcclusion());

    private static  DeferredBlock<BlockOpiniumCore> createOpiniumCore(BlockOpiniumCore.Tier tier){
        DeferredBlock<BlockOpiniumCore> block = createBlock(tier.name().toLowerCase() + "_opinium_core", p -> new BlockOpiniumCore(p, tier),BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion());
        OPINIUM_CORE.put(tier,block);
        return block;
    }
    protected static <T extends Block> DeferredBlock<T> createBlockWithoutItem(String name, Function<BlockBehaviour.Properties,T> func, BlockBehaviour.Properties props){
        return BLOCKS.registerBlock(name, func,props);
    }

    protected static <T extends Block> DeferredBlock<T> createBlock(String name, Function<BlockBehaviour.Properties,T> func, BlockBehaviour.Properties props){
        DeferredBlock<T> block = createBlockWithoutItem(name, func,props);
        AUItems.createItem(name,p->new AUBlockItem(block.get(),p),new Item.Properties().useBlockDescriptionPrefix());
        return block;
    }

    protected static <T extends Block> DeferredBlock<T> createBlockWithCustomItem(String name,  Function<BlockBehaviour.Properties,T> func, BlockBehaviour.Properties props, BiFunction<T, Item.Properties, ? extends Item> itemFactory){
        DeferredBlock<T> block = createBlockWithoutItem(name, func,props);
        AUItems.createItem(name,p->itemFactory.apply(block.get(),p),new Item.Properties().useBlockDescriptionPrefix());
        return block;
    }

    public static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_) {
        return false;
    }

}

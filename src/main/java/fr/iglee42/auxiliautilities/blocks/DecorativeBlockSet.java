package fr.iglee42.auxiliautilities.blocks;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;

public class DecorativeBlockSet {

    public static final List<DecorativeBlockSet> ALL_SETS = new ArrayList<>();

    private final String name;
    private final DeferredBlock<Block> block;
    private final DeferredBlock<StairBlock> stairs;
    private final DeferredBlock<SlabBlock> slab;
    private final DeferredBlock<WallBlock> wall;

    public DecorativeBlockSet(String name) {
        this.name = name;
        block = AUBlocks.createBlock(name,()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
        stairs = AUBlocks.createBlock(name+"_stairs",()-> new StairBlock(block.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS)));
        slab = AUBlocks.createBlock(name+"_slab",()-> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB)));
        wall = AUBlocks.createBlock(name+"_wall",()-> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL)));
        ALL_SETS.add(this);
    }

    public DeferredBlock<Block> getBlock() {
        return block;
    }

    public DeferredBlock<StairBlock> getStairs() {
        return stairs;
    }

    public DeferredBlock<SlabBlock> getSlab() {
        return slab;
    }

    public DeferredBlock<WallBlock> getWall() {
        return wall;
    }

    public String getName() {
        return name;
    }
}

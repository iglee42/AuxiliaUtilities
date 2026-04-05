package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.mixins.BlockPropertiesAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.*;

public class CompressedBlockSet {

    public static final List<CompressedBlockSet> ALL_SETS = new ArrayList<>();

    private final Block block;
    private final String name;
    private final int maxTier;
    private final Map<Integer,DeferredBlock<AUBlock>> blocks;

    public CompressedBlockSet(Block block, int maxTier) {
        this.block = block;
        this.name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        this.maxTier = maxTier;
        this.blocks = new HashMap<>();
        for (int tier = 1; tier <= maxTier; tier++) {
            BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(block);
            float destroyTime = (float) (((BlockPropertiesAccessor)props).getDestroyTime() * Math.pow(2.25D,tier + 1));
            float explosionResistance = (float) (((BlockPropertiesAccessor)props).getExplosionResistance() * Math.pow(1.5D,tier + 1));
            int finalTier = tier;
            blocks.put(finalTier,AUBlocks.createBlock("compressed_"+name+"_"+tier,()->new AUBlock(props.strength(destroyTime,explosionResistance)){
                @Override
                public List<Component> getStorageTooltips(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag) {
                    return List.of(AULang.COMPRESSED_BLOCKS.get(formatInt((int) Math.pow(9,finalTier))));
                }
            }));
        }
        ALL_SETS.add(this);
    }

    public Map<Integer, DeferredBlock<AUBlock>> getBlocks() {
        return blocks;
    }

    public DeferredBlock<AUBlock> getBlock(int tier) {
        return blocks.get(tier);
    }

    public String getName() {
        return name;
    }

    public int getMaxTier() {
        return maxTier;
    }

    public Block getBaseBlock() {
        return block;
    }
}

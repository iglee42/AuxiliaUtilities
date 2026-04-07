package fr.iglee42.auxiliautilities.blocks.api;

import fr.iglee42.auxiliautilities.utils.AUTooltipProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public interface AUBlockBase extends AUTooltipProvider {

    default Block self(){
        return (Block) this;
    }


    default void addToCreativeTab(Consumer<ItemStack> acceptor){
        acceptor.accept(new ItemStack(self().asItem()));
    }

    default int getColor( BlockState state, BlockAndTintGetter getter, BlockPos pos, int tintIndex){
        return 0xFFFFFFFF;
    }

    default int getItemColor(ItemStack stack, int tintIndex){
        return 0xFFFFFFFF;
    }

}

package fr.iglee42.auxiliautilities.blocks.api;

import fr.iglee42.auxiliautilities.utils.AUTooltipProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public interface AUBlockBase extends AUTooltipProvider {

    default Block self(){
        return (Block) this;
    }


    default void addToCreativeTab(Consumer<ItemStack> acceptor){
        acceptor.accept(new ItemStack(self().asItem()));
    }

}

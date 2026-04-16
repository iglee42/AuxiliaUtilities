package fr.iglee42.auxiliautilities.items.api;

import fr.iglee42.auxiliautilities.utils.AUTooltipProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface AUItemBase extends AUTooltipProvider {

    default Item self(){
        return (Item) this;
    }

    default void addToTab(Consumer<ItemStack> acceptor){
        acceptor.accept(new ItemStack(self()));
    }

    default int getColor(ItemStack stack, int tintIndex){
        return 0xFFFFFFFF;
    }

}

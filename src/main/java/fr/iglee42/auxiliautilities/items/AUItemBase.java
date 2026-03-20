package fr.iglee42.auxiliautilities.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public interface AUItemBase {

    private Item self(){
        return (Item) this;
    }

    default void addToTab(Consumer<ItemStack> acceptor){
        acceptor.accept(new ItemStack(self()));
    }

}

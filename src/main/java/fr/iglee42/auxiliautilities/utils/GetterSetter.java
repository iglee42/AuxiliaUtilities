package fr.iglee42.auxiliautilities.utils;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface GetterSetter<T> extends Consumer<T>, Supplier<T> {
  public static class ContainerSlot implements GetterSetter<ItemStack> {
    final Slot slot;
    
    public ContainerSlot(Slot slot) {
      this.slot = slot;
    }
    
    public void accept(ItemStack stack) {
      this.slot.set(stack);
    }
    
    public ItemStack get() {
      return this.slot.hasItem() ? this.slot.getItem() : ItemStack.EMPTY;
    }
  }
  
  public static class InvSlot implements GetterSetter<ItemStack> {
    final Container inventory;
    
    final int slot;
    
    public InvSlot(Container inventory, int slot) {
      this.inventory = inventory;
      this.slot = slot;
    }
    
    public void accept(ItemStack stack) {
      this.inventory.setItem(this.slot, stack);
    }
    
    public ItemStack get() {
      return this.inventory.getItem(this.slot);
    }
  }
  
  public static class PlayerHand implements GetterSetter<ItemStack> {
    final Inventory inventoryPlayer;
    
    public PlayerHand(Inventory inventoryPlayer) {
      this.inventoryPlayer = inventoryPlayer;
    }
    
    public void accept(ItemStack stack) {
      this.inventoryPlayer.addAndPickItem(stack);
    }
    
    public ItemStack get() {
      return this.inventoryPlayer.getSelectedItem();
    }
  }
}

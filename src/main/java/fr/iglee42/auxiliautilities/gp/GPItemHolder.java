package fr.iglee42.auxiliautilities.gp;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable GP holder wrapper for an item stack tracked in a player inventory slot.
 */
public abstract class GPItemHolder implements GPHolder {

    private final ItemStack stack;
    private final Player player;
    private final ItemStack originalStack;

    public GPItemHolder(ItemStack stack, Player player) {
        this.stack = stack;
        this.player = player;
        this.originalStack = stack.copy();
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public int getGPGeneration() {
        return stack.isEmpty() ? 0 : getGPGeneration(stack, player);
    }

    @Override
    public int getGPConsumption() {
        return stack.isEmpty() ? 0 : getGPConsumption(stack, player);
    }

    @Override
    public UUID getNetworkId() {
        return player.getUUID();
    }

    public final void onRemoved(){
        onRemoved(originalStack,player);
    }

    @Override
    public String name() {
        return stack.getItemName().toString();
    }

    protected int getGPConsumption(ItemStack stack, Player player) {
        return 0;
    }

    protected int getGPGeneration(ItemStack stack, Player player) {
        return 0;
    }

    protected void onRemoved(ItemStack stack, Player player) {
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GPItemHolder other)) return false;

        return stack == other.stack && player == other.player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(System.identityHashCode(stack), player);
    }
}


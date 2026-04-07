package fr.iglee42.auxiliautilities.network;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotGhostWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public class SubmitGhostItemPacket extends AUPacket{

    public static final StreamCodec<RegistryFriendlyByteBuf, SubmitGhostItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,SubmitGhostItemPacket::getStack,
            ByteBufCodecs.INT, SubmitGhostItemPacket::getSlotIndex,
            SubmitGhostItemPacket::new
    );
    private final ItemStack stack;
    private final int slotIndex;


    public SubmitGhostItemPacket(ItemStack stack,int slotIndex) {
        super(AUPackets.SUBMIT_GHOST_ITEM);
        this.stack = stack;
        this.slotIndex = slotIndex;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    @Override
    protected void handle(IPayloadContext context) {
        if (context.player().containerMenu instanceof AUMenu menu){
            if (slotIndex >= 0 && slotIndex < menu.playerSlotsStart){
                if (menu.getSlot(slotIndex) instanceof SlotGhostWidget ghostSlot){
                    ghostSlot.set(stack);
                }
            }
        }
    }
}

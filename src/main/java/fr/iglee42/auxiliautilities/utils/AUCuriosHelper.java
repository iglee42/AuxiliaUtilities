package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.gp.GPCapabilities;
import fr.iglee42.auxiliautilities.gp.GPItemHolder;
import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import fr.iglee42.auxiliautilities.items.ItemAngelRing;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class AUCuriosHelper {

    public AUCuriosHelper(IEventBus bus){
        bus.addListener(this::registerCapabilities);
    }

    public static ItemStack getItemFromCuriosForCosmetic(Item item, Player player){
        return CuriosApi.getCuriosInventory(player).map(inv->inv.findFirstCurio(item).map(slot->{
            if (!slot.slotContext().visible()) return ItemStack.EMPTY;
            return slot.stack();
        }).orElse(ItemStack.EMPTY)).orElse(ItemStack.EMPTY);
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerItem(CuriosCapability.ITEM,
                (stack,ctx)->new AUGPCurio() {
                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }

                    @Override
                    public void curioTick(SlotContext slotContext) {
                        if (getStack().getItem() instanceof ItemAngelRing it){
                            it.tick(getStack(), slotContext.entity());
                        }
                    }

                    @Override
                    public boolean canEquipFromUse(SlotContext slotContext) {
                        return true;
                    }
                }, AUItems.ANGEL_RING);

    }

    private abstract static class AUGPCurio implements ICurio{

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack) {
            if (!(slotContext.entity() instanceof Player player)) return;
            GPItemHolder holder;
            if ((holder=getStack().getCapability(GPCapabilities.ITEM,player)) != null){
                if (holder.getGPGeneration() > 0) GPNetworkManager.INSTANCE.registerGenerator(holder);
                if (holder.getGPConsumption() > 0) GPNetworkManager.INSTANCE.registerConsumer(holder);
            }
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack) {
            if (!(slotContext.entity() instanceof Player player)) return;
            GPItemHolder prevHolder;
            if ((prevHolder=getStack().getCapability(GPCapabilities.ITEM,player)) != null){
                GPNetworkManager.INSTANCE.unregisterGenerator(prevHolder);
                GPNetworkManager.INSTANCE.unregisterConsumer(prevHolder);
            }
        }
    }
}

package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.function.Consumer;

public class ItemEnergyDroplet extends AUItem{
    public ItemEnergyDroplet(Properties props) {
        super(props.component(AUDataComponents.STORED_ENERGY,0));
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {}


    public static ItemStack createWithEnergy(int energy){
        ItemStack stack = new ItemStack(AUItems.ENERGY_DROPLET.get());
        stack.set(AUDataComponents.STORED_ENERGY, energy);
        return stack;
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        if (stack.has(AUDataComponents.STORED_ENERGY)){
            int energy = stack.getOrDefault(AUDataComponents.STORED_ENERGY,0);
            if (energy > 0)
                return List.of(AULang.STORED_ENERGY_TOOLTIP_ITEM.get(formatInt(energy)));
        }
        return super.getStorageTooltips(stack, ctx, flag);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (ctx.getLevel().isClientSide) return super.useOn(ctx);
        IEnergyStorage storage = ctx.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK,ctx.getClickedPos(),ctx.getClickedFace());
        if (storage != null){
            int energy = ctx.getItemInHand().getOrDefault(AUDataComponents.STORED_ENERGY,0);
            if (energy > 0){
                int extracted = storage.receiveEnergy(energy, false);
                if (extracted > 0){
                    if (extracted < energy)
                        ctx.getItemInHand().set(AUDataComponents.STORED_ENERGY, energy - extracted);
                    else
                        ctx.getItemInHand().shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useOn(ctx);
    }

    protected static class EnergyTank implements IEnergyStorage {

        private final ItemStack stack;

        protected EnergyTank(ItemStack stack){
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            int extracted = Math.min(toExtract, getEnergy());
            if (!simulate){
                if (extracted < getEnergy())
                    stack.set(AUDataComponents.STORED_ENERGY, getEnergy() - extracted);
                else
                    stack.shrink(1);
            }
            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return getEnergy();
        }

        @Override
        public int getMaxEnergyStored() {
            return getEnergy();
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }

        private int getEnergy(){
            return stack.getOrDefault(AUDataComponents.STORED_ENERGY,0);
        }

    }
}

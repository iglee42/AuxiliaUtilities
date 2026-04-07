package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.utils.FluidColorHelper;
import fr.iglee42.auxiliautilities.utils.StoredFluidStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.function.Consumer;

public class ItemFluidDroplet extends AUItem{
    public ItemFluidDroplet(Properties props) {
        super(props.component(AUDataComponents.STORED_FLUID, StoredFluidStack.EMPTY));
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {}

    public static ItemStack createWithFluid(FluidStack fluidStack){
        ItemStack stack = new ItemStack(AUItems.FLUID_DROPLET.get());
        stack.set(AUDataComponents.STORED_FLUID, new StoredFluidStack(fluidStack));
        return stack;
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 0 || stack.getOrDefault(AUDataComponents.STORED_FLUID,StoredFluidStack.EMPTY).stack().isEmpty()) return 0xFFFFFFFF;
        return FluidColorHelper.getColor(stack.getOrDefault(AUDataComponents.STORED_FLUID,StoredFluidStack.EMPTY).stack());
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        if (stack.has(AUDataComponents.STORED_FLUID)){
            StoredFluidStack fluid = stack.get(AUDataComponents.STORED_FLUID);
            if (fluid != null && !fluid.stack().isEmpty())
                return List.of(AULang.STORED_FLUID_TOOLTIP_ITEM.get(formatInt(fluid.stack().getAmount()),fluid.stack().getHoverName().getString()));
        }
        return super.getStorageTooltips(stack, ctx, flag);
    }

    protected static class FluidTank implements IFluidHandlerItem {

        private final ItemStack stack;

        protected FluidTank(ItemStack stack){
            this.stack = stack;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return getFluid().getAmount();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(getFluid(),resource)) return FluidStack.EMPTY;
            FluidStack fluidInTank = getFluid().copy();
            int drained = Math.min(fluidInTank.getAmount(), resource.getAmount());
            int newAmount = fluidInTank.getAmount() - drained;
            if (action.execute()){
                if (newAmount <= 0){
                    stack.shrink(1);
                } else {
                    stack.set(AUDataComponents.STORED_FLUID, new StoredFluidStack(new FluidStack(fluidInTank.getFluid(), newAmount)));
                }
            }
            return new FluidStack(fluidInTank.getFluid(), drained);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (maxDrain <= 0) return FluidStack.EMPTY;
            FluidStack fluidInTank = getFluid().copy();
            int drained = Math.min(fluidInTank.getAmount(), maxDrain);
            int newAmount = fluidInTank.getAmount() - drained;
            if (action.execute()){
                if (newAmount <= 0){
                    stack.shrink(1);
                } else {
                    stack.set(AUDataComponents.STORED_FLUID, new StoredFluidStack(new FluidStack(fluidInTank.getFluid(), newAmount)));
                }
            }
            return new FluidStack(fluidInTank.getFluid(), drained);
        }

        private FluidStack getFluid(){
            if (stack == null || !stack.has(AUDataComponents.STORED_FLUID)) return FluidStack.EMPTY;
            return getContainer().get(AUDataComponents.STORED_FLUID).stack();
        }

        @Override
        public ItemStack getContainer() {
            return stack;
        }
    }
}

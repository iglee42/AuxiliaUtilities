package fr.iglee42.auxiliautilities.blockentities.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.menu.widgets.api.TransferPriority;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.utils.IFluidFilter;
import fr.iglee42.auxiliautilities.utils.IItemFilter;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SingleFilterStackHandler extends SingleItemStackHandler{

    public abstract Item getExpectedItem();

    public SlotItemHandlerWidget getSlot(int x,int y){
        return new SlotFilterWidget(this,x,y);
    }

    public boolean hasFilter(){
        return InventoryHelper.isStackNotEmpty(getStack());
    }

    protected abstract boolean isValid(ItemStack stack);

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return isValid(stack);
    }

    @Override
    protected int getStackLimit(int slot, ItemStack stack) {
        if (!isValid(stack)) return 0;
        return super.getStackLimit(slot, stack);
    }

    public static class FluidFilter extends SingleFilterStackHandler{

        public static final Codec<FluidFilter> CODEC = RecordCodecBuilder.create(instance->
                instance.group(
                        ItemStack.OPTIONAL_CODEC.fieldOf("filter_stack").forGetter(FluidFilter::getStack)
                ).apply(instance, (stack)-> {
                    FluidFilter filter = new FluidFilter();
                    filter.setStackInSlot(0,stack);
                    return filter;
                })
        );

        public static final StreamCodec<RegistryFriendlyByteBuf,FluidFilter> STREAM_CODEC = StreamCodec.composite(
                ItemStack.OPTIONAL_STREAM_CODEC, FluidFilter::getStack,
                (stack)-> {
                    FluidFilter filter = new FluidFilter();
                    filter.setStackInSlot(0,stack);
                    return filter;
                }
        );

        @Override
        public Item getExpectedItem() {
            return AUItems.FLUID_FILTER.asItem();
        }

        @Override
        protected boolean isValid(ItemStack stack) {
            return stack.getItem() instanceof IFluidFilter filter && filter.isFluidFilter(stack);
        }

        public boolean matches(FluidStack stack){
            if (!hasFilter()) return true;
            ItemStack filterStack = getStack();
            if (!(filterStack.getItem() instanceof IFluidFilter filter)) return false;
            return filter.matchesFilter(stack,filterStack);
        }
    }

    public static class ItemFilter extends SingleFilterStackHandler{

        public static final Codec<ItemFilter> CODEC = RecordCodecBuilder.create(instance->
                instance.group(
                        ItemStack.OPTIONAL_CODEC.fieldOf("filter_stack").forGetter(ItemFilter::getStack)
                ).apply(instance, (stack)-> {
                    ItemFilter filter = new ItemFilter();
                    filter.setStackInSlot(0,stack);
                    return filter;
                })
        );

        public static final StreamCodec<RegistryFriendlyByteBuf,ItemFilter> STREAM_CODEC = StreamCodec.composite(
                ItemStack.OPTIONAL_STREAM_CODEC, ItemFilter::getStack,
                (stack)-> {
                    ItemFilter filter = new ItemFilter();
                    filter.setStackInSlot(0,stack);
                    return filter;
                }
        );

        @Override
        public Item getExpectedItem() {
            return AUItems.ITEM_FILTER.asItem();
        }

        @Override
        protected boolean isValid(ItemStack stack) {
            return stack.getItem() instanceof IItemFilter filter && filter.isItemFilter(stack);
        }

        public boolean matches(ItemStack stack){
            if (!hasFilter()) return true;
            ItemStack filterStack = getStack();
            if (!(filterStack.getItem() instanceof IItemFilter filter)) return false;
            return filter.matchesFilter(stack,filterStack);
        }
    }

    public static class EitherFilter extends SingleFilterStackHandler{

        public static final Codec<EitherFilter> CODEC = RecordCodecBuilder.create(instance->
                instance.group(
                        ItemStack.OPTIONAL_CODEC.fieldOf("filter_stack").forGetter(EitherFilter::getStack)
                ).apply(instance, (stack)-> {
                    EitherFilter filter = new EitherFilter();
                    filter.setStackInSlot(0,stack);
                    return filter;
                })
        );

        public static final StreamCodec<RegistryFriendlyByteBuf,EitherFilter> STREAM_CODEC = StreamCodec.composite(
                ItemStack.OPTIONAL_STREAM_CODEC, EitherFilter::getStack,
                (stack)-> {
                    EitherFilter filter = new EitherFilter();
                    filter.setStackInSlot(0,stack);
                    return filter;
                }
        );

        @Override
        public Item getExpectedItem() {
            if (System.currentTimeMillis() % 2000L < 1000L)
                return AUItems.FLUID_FILTER.asItem();
            return AUItems.ITEM_FILTER.asItem();
        }

        @Override
        protected boolean isValid(ItemStack stack) {
            return (stack.getItem() instanceof IFluidFilter fluidFilter && fluidFilter.isFluidFilter(stack)) || (stack.getItem() instanceof IItemFilter itemFilter && itemFilter.isItemFilter(stack));
        }

        public boolean matches(ItemStack stack){
            if (!hasFilter()) return true;
            ItemStack filterStack = getStack();
            if (!(filterStack.getItem() instanceof IItemFilter filter)) return false;
            return filter.matchesFilter(stack,filterStack);
        }

        public boolean matches(FluidStack stack){
            if (!hasFilter()) return true;
            ItemStack filterStack = getStack();
            if (!(filterStack.getItem() instanceof IFluidFilter filter)) return false;
            return filter.matchesFilter(stack,filterStack);
        }

    }

    private class SlotFilterWidget extends SlotItemHandlerWidget implements TransferPriority {

        private final SingleFilterStackHandler filterHandler;

        public SlotFilterWidget(SingleFilterStackHandler filterHandler, int x, int y) {
            super(filterHandler, 0, x, y);
            this.filterHandler = filterHandler;
            setBackground(InventoryMenu.BLOCK_ATLAS, AuxiliaUtilities.id("item/filter_skeleton"));
        }

        @Override
        public @NotNull List<Component> getTooltips() {
            if (getStack().isEmpty())
                return List.of(Component.translatable(filterHandler.getExpectedItem().getDescriptionId()));
            return super.getTooltips();
        }

        @Override
        public int getPriority() {
            return 1;
        }
    }
}

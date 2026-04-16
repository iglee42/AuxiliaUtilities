package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.items.SingleItemStackHandler;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUMCClickChoiceWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUWidgetBase;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotGhostWidget;
import fr.iglee42.auxiliautilities.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemFilterFluid extends AUItem implements IFluidFilter, MenuProvider {

    public static final int FLUID_SLOTS = 16;

    public ItemFilterFluid(Properties props) {
        super(props.component(AUDataComponents.FILTER_FLAGS,0).component(AUDataComponents.FILTER_ITEMS, NonNullList.withSize(FLUID_SLOTS, ItemStack.EMPTY)));
    }

    @Override
    public boolean isFluidFilter(ItemStack filterStack) {
        return true;
    }

    @Override
    public boolean matchesFilter(FluidStack stack, ItemStack filterStack) {
        boolean inverted = getFlag(filterStack,Flag.INVERTED);
        if (stack.isEmpty()) {
            return inverted;
        }
        boolean matchComponents = !getFlag(filterStack,Flag.IGNORE_COMPONENTS);
        for (int i = 0; i < FLUID_SLOTS; i++) {
            ItemStack ghostStack = getGhostStack(filterStack, i);
            if (InventoryHelper.isStackNotEmpty(ghostStack)){
                if (ghostStack.getItem() instanceof IFluidFilter otherFilter){
                    if (otherFilter.matchesFilter(stack,ghostStack))
                        return !inverted;
                }
                FluidStack fluid = FluidUtil.getFluidContained(ghostStack).orElse(FluidStack.EMPTY);
                if (!fluid.isEmpty()){
                    if ((!matchComponents || FluidStack.isSameFluidSameComponents(fluid,stack)) && (matchComponents || FluidStack.isSameFluid(fluid,stack)))
                        return !inverted;
                }
            }
        }
        return inverted;
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        List<Component> tooltips = new ArrayList<>();
        for (int i = 0; i < FLUID_SLOTS; i++) {
            ItemStack ghostStack = getGhostStack(stack, i);
            if (InventoryHelper.isStackNotEmpty(ghostStack)) {
                if (ghostStack.getItem() instanceof IFluidFilter){
                    tooltips.add(Component.literal("- ").append(ghostStack.getHoverName()));
                    if (ghostStack.getItem() instanceof AUTooltipProvider provider){
                        List<Component> subTooltips = provider.getStorageTooltips(ghostStack, ctx, flag);
                        for (Component subTooltip : subTooltips) {
                            tooltips.add(Component.literal("    ").append(subTooltip));
                        }
                    }
                } else {
                    FluidStack fluid = FluidUtil.getFluidContained(ghostStack).orElse(FluidStack.EMPTY);
                    if (!fluid.isEmpty()){
                        tooltips.add(Component.literal("- ").append(fluid.getHoverName()));
                    } else {
                        tooltips.add(Component.literal("- ").append(ghostStack.getHoverName()));
                    }
                }
            }
        }
        return tooltips;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide){
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.getItem() == this){
                player.openMenu(this);
                return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    public static boolean getFlag(ItemStack stack, Flag flag) {
        int flags = stack.getOrDefault(AUDataComponents.FILTER_FLAGS, 0);
        return (flags & flag.meta) != 0;
    }

    public static void setFlag(ItemStack stack, Flag flag, boolean value) {
        int flags = stack.getOrDefault(AUDataComponents.FILTER_FLAGS, 0);

        if (value) {
            flags |= flag.meta;
        } else {
            flags &= ~flag.meta;
        }
        stack.set(AUDataComponents.FILTER_FLAGS, flags);
    }

    public static List<ItemStack> getItems(ItemStack stack){
        return List.copyOf(stack.getOrDefault(AUDataComponents.FILTER_ITEMS, NonNullList.withSize(FLUID_SLOTS, ItemStack.EMPTY)));
    }

    public static ItemStack getGhostStack(ItemStack stack, int slot){
        return getItems(stack).get(slot);
    }

    public static void setGhostStack(ItemStack stack, int slot, ItemStack fluid) {
        List<ItemStack> fluids = new ArrayList<>(stack.getOrDefault(AUDataComponents.FILTER_ITEMS, NonNullList.withSize(FLUID_SLOTS, ItemStack.EMPTY)));
        fluids.set(slot,fluid);
        stack.set(AUDataComponents.FILTER_ITEMS, fluids);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new FilterConfigContainer(player,id,playerInv.selected,playerInv,playerInv.getSelected());
    }

    public static enum Flag {
        INVERTED,
        IGNORE_COMPONENTS
        ;

        final int meta;

        Flag() {
            this.meta = 1<<ordinal();
        }

        public Component getOnName(){
            return Component.translatable("item."+ AuxiliaUtilities.MODID+".filter.flag."+name().toLowerCase()+"_on");
        }
        public Component getOffName(){
            return Component.translatable("item."+ AuxiliaUtilities.MODID+".filter.flag."+name().toLowerCase()+"_off");
        }

    }

    public static class FilterConfigContainer extends AUMenu {

        private final Player player;
        private final int slot;
        private final ItemStack heldItem;
        SlotGhostWidget[] ghostSlots = new SlotGhostWidget[FLUID_SLOTS];

        public FilterConfigContainer(int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
            this(playerInv.player,id,playerInv.selected,playerInv,playerInv.getSelected());
        }

        protected FilterConfigContainer(Player player, int id, int slot, Inventory playerInv, ItemStack heldItem) {
            super(AUMenus.FLUID_FILTER.get(), id);
            this.player = player;
            this.slot = slot;
            this.heldItem = heldItem;
            int NUM_COLUMNS = (int)Math.sqrt(16.0D);
            int inner_filter_w = NUM_COLUMNS * 18;
            int filter_w = inner_filter_w * 14 / 8;
            int filter_h_offset = filter_w * 3 / 14 - 5;

            AUWidgetBase item = new AUWidgetBase(85 - filter_w / 2, 5, filter_w, filter_w) {
                @Override
                public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
                    ResourceLocation texture = AuxiliaUtilities.id("item/filter_fluids");
                    TextureAtlasSprite sprite = Minecraft.getInstance()
                            .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                            .apply(texture);
                    graphics.blit(
                            guiLeft + x,
                            guiTop + y,
                            0,
                            width,
                            height,
                            sprite,
                            1.0F,
                            1.0F,
                            1.0F,
                            0.75F
                    );
                }
            };
            addWidget(item);
            addTitle(Component.translatable(heldItem.getDescriptionId()));
            for (int i = 0; i < 16; i++) {
                int slotX = i % NUM_COLUMNS;
                int slotY = i / NUM_COLUMNS;
                final int dSlot = i;
                addWidget((this.ghostSlots[i] = new SlotGhostWidget(new SingleItemStackHandler() {
                    public ItemStack getStack() {
                        return ItemFilterFluid.getGhostStack(heldItem, dSlot);
                    }

                    @Override
                    public ItemStack getStackInSlot(int slot) {
                        return ItemFilterFluid.getGhostStack(heldItem, dSlot);
                    }

                    @Override
                    public void setStackInSlot(int slot, ItemStack stack) {
                        ItemFilterFluid.setGhostStack(heldItem, dSlot, stack);
                    }
                },0, 85 - NUM_COLUMNS * 9 + slotX * 18, 10 + filter_h_offset + slotY * 18) {

                    @Override
                    public void set(ItemStack stack) {
                        FluidStack fluidContained = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
                        if (fluidContained.isEmpty() && InventoryHelper.isStackNotEmpty(stack) && !(stack.getItem() instanceof IFluidFilter))
                            return;
                        super.set(stack);
                    }

                    @Override
                    public void renderBackground(GuiGraphics gui, AUContainerScreen screen, int guiLeft, int guiTop) {
                        super.renderBackground(gui, screen, guiLeft, guiTop);
                        ItemStack stack = getItem();
                        FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);

                        if (fluidStack.isEmpty())
                            return;

                        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
                        ResourceLocation still = extensions.getStillTexture(fluidStack);

                        TextureAtlasSprite sprite = Minecraft.getInstance()
                                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                                .apply(still);
                        if (sprite == null)
                            return;
                        int tint = extensions.getTintColor(fluidStack);
                        float r = (tint >> 16 & 255) / 255F;
                        float g = (tint >> 8 & 255) / 255F;
                        float b = (tint & 255) / 255F;
                        gui.setColor(r, g, b, 1F);
                        gui.blit(guiLeft + getX() + 1, guiTop + getY() + 1, 0, 16, 16, sprite);
                        gui.setColor(1F, 1F, 1F, 1F);
                    }
                }));
            }
            crop();
            for (Flag flag : Flag.values()) {
                int j = flag.ordinal();
                int dx = j % 2;
                int dy = j / 2;
                int x = 5 + dx * 162 / 2;
                if (dx == 0) {
                    x = 5;
                } else {
                    int w = 8 + Math.max(AULang.getTextSize(flag.getOnName()), AULang.getTextSize(flag.getOffName()));
                    x = 176 - w;
                }
                int y = 10 + filter_h_offset + NUM_COLUMNS * 18 + 4 + dy * 18;

                addWidget(new AUMCClickChoiceWidget<Boolean>(x,y) {
                    @Override
                    protected void onSelectedServer(Boolean value) {
                        ItemFilterFluid.setFlag(heldItem,flag, value);
                    }

                    @Override
                    public Boolean getSelectedValue() {
                        return ItemFilterFluid.getFlag(heldItem, flag);
                    }
                }.addChoice(false,flag.getOffName(),flag.getOffName()).addChoice(true,flag.getOnName(),flag.getOnName()));
            }
            cropAndAddPlayerSlots(playerInv);
            validate();
        }

        @Override
        public boolean stillValid(Player player) {
            ItemStack heldItem;
            return (player == this.player && this.player.getInventory().selected == slot && InventoryHelper.isStackNotEmpty(heldItem = player.getMainHandItem()) && heldItem.getItem() == this.heldItem.getItem());
        }


        @Override
        public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
            Slot slot = this.slots.get(index);
            if (slot instanceof SlotGhostWidget) {
                slot.set(ItemStack.EMPTY);
            } else {
                ItemStack slotStack = slot.getItem();
                if (!(slotStack.getItem() instanceof IFluidFilter) && FluidUtil.getFluidContained(slotStack).orElse(FluidStack.EMPTY).isEmpty())
                    return ItemStack.EMPTY;
                FluidStack slotFluid = FluidUtil.getFluidContained(slotStack).orElse(FluidStack.EMPTY);
                if (InventoryHelper.isStackNotEmpty(slotStack)) {
                    for (SlotGhostWidget ghostSlot : this.ghostSlots) {
                        ItemStack stack = ghostSlot.getItem();
                        FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
                        if (slotFluid.isEmpty() || (fluidStack.isEmpty() && !(stack.getItem() instanceof IFluidFilter))) continue;
                        if (FluidStack.isSameFluidSameComponents(fluidStack,slotFluid)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    for (SlotGhostWidget ghostSlot : this.ghostSlots) {
                        ItemStack stack = ghostSlot.getItem();
                        if (InventoryHelper.isStackEmpty(stack)) {
                            ghostSlot.set(slotStack.copy());
                            break;
                        }
                    }
                }
            }
            return ItemStack.EMPTY;
        }
    }
}

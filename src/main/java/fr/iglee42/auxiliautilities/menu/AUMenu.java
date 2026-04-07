package fr.iglee42.auxiliautilities.menu;

import com.google.common.collect.ImmutableList;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.widgets.AUTextWidget;
import fr.iglee42.auxiliautilities.menu.widgets.api.*;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotWidget;
import fr.iglee42.auxiliautilities.network.AUPacket;
import fr.iglee42.auxiliautilities.network.AUPackets;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AUMenu extends AbstractContainerMenu {


    public static final ResourceLocation texBackground = AuxiliaUtilities.id( "textures/gui/gui_base.png");
    public static final ResourceLocation texBackgroundBlack = AuxiliaUtilities.id( "textures/gui/gui_base_black.png");
    public static final ResourceLocation texBackgroundIndentation = AuxiliaUtilities.id( "textures/gui/gui_base_indent.png");
    public static final ResourceLocation texBackgroundBorder = AuxiliaUtilities.id( "textures/gui/gui_base_invisible.png");
    
    public int width = 176;
    public int height = 166;
    

    public final HashSet<Slot> playerSlots = new HashSet<>();
    private List<AUWidget> widgets = new ArrayList<>();
    private List<AUWidgetKeyInput> widgetKeyInputs = new ArrayList<>();
    private List<AUWidgetMouseInput> widgetMouseInputs = new ArrayList<>();
    private List<AUWidgetClientNetwork> widgetReceivers = new ArrayList<>();
    private List<AUWidgetClientTick> widgetClientTick = new ArrayList<>();

    public int playerSlotsStart = -1;

    protected AUMenu(@NotNull MenuType<?> type, int id) {
        super(type, id);
    }

    public List<AUWidgetClientTick> getWidgetClientTick() {
        return widgetClientTick;
    }

    public List<AUWidget> getWidgets() {
        return ImmutableList.copyOf(widgets);
    }

    public List<AUWidgetKeyInput> getWidgetKeyInputs() {
        return ImmutableList.copyOf(widgetKeyInputs);
    }

    public List<AUWidgetMouseInput> getWidgetMouseInputs() {
        return ImmutableList.copyOf(widgetMouseInputs);
    }

    public List<AUWidgetClientNetwork> getWidgetReceivers() {
        return ImmutableList.copyOf(widgetReceivers);
    }


    public void cropAndAddPlayerSlots(Inventory inventory) {
        crop(4);
        this.height += 95;
        if (this.width < 176)
            this.width = 176;
        addPlayerSlotsToBottom((Inventory) inventory);
    }

    public void addPlayerSlotsToBottom(Inventory inventory) {
        addPlayerSlots(inventory, (this.width - 162) / 2, this.height - 95);
    }

    public void crop() {
        crop(4);
    }

    public void crop(int border) {
        int maxX = 18;
        int maxY = 18;
        for (AUWidget widget : this.getWidgets()) {
            maxX = Math.max(maxX, widget.getX() + widget.getWidth());
            maxY = Math.max(maxY, widget.getY() + widget.getHeight());
        }
        this.width = maxX + border;
        this.height = maxY + border;
    }

    public void cropForPlayerSlots() {
        crop(4);
        if (this.width < 176)
            this.width = 176;
    }


    public void addPlayerSlots(Inventory inventory, int x, int y) {
        this.playerSlotsStart = 0;
        for (AUWidget widget : this.widgets) {
            if (widget instanceof Slot)
                this.playerSlotsStart++;
        }
        addWidget(new AUTextWidget(x, y + 2,1) {
            @Override
            protected Component getMessage() {
                return inventory.getName();
            }
        });
        int j;
        for (j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                SlotWidget w = new SlotWidget(inventory, k + j * 9 + 9, x + k * 18, y + 14 + j * 18);
                this.playerSlots.add(w);
                addWidget(w);
            }
        }
        for (j = 0; j < 9; j++) {
            SlotWidget w = new SlotWidget(inventory, j, x + j * 18, y + 14 + 58);
            this.playerSlots.add(w);
            addWidget(w);
        }
    }

    @ApiStatus.Internal
    public void addInternalSlot(Slot slot){
        addSlot(slot);
    }


    public void addWidget(AUWidget w) {
        /*if (this.fixed)
            throw new IllegalStateException();*/
        this.widgets.add(w);
        if (w instanceof AUWidgetKeyInput cw)
            this.widgetKeyInputs.add(cw);
        if (w instanceof AUWidgetMouseInput cw)
            this.widgetMouseInputs.add(cw);
        if (w instanceof AUWidgetClientNetwork cw)
            this.widgetReceivers.add(cw);
        if (w instanceof AUWidgetClientTick cw)
            this.widgetClientTick.add(cw);
    }

    public void addTitle(Component name) {
        addWidget(new AUTextWidget((this.width - 162) / 2,5,1) {
            @Override
            protected Component getMessage() {
                return name;
            }
        });
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        ItemStack stack = ItemStack.EMPTY;
        Slot sourceSlot = this.slots.get(slot);
        if (this.playerSlotsStart > 0 && sourceSlot != null && sourceSlot.hasItem()){
            ItemStack sourceStack = sourceSlot.getItem();
            if (sourceStack == null || sourceStack.isEmpty()) return ItemStack.EMPTY;
            stack = sourceStack.copy();
            if (slot < this.playerSlotsStart) {
                if (!this.moveItemStackTo(sourceStack, this.playerSlotsStart, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(sourceStack, 0, this.playerSlotsStart, false))
                return ItemStack.EMPTY;
            if (stack.isEmpty()){
                sourceSlot.set(ItemStack.EMPTY);
            } else {
                sourceSlot.setChanged();
            }
        }
        return stack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean merged = false;

        // Try merging with existing stacks
        if (stack.isStackable()) {
            int index = reverseDirection ? endIndex - 1 : startIndex;

            while (reverseDirection ? index >= startIndex : index < endIndex) {
                Slot slot = this.slots.get(index);

                if (isValidForMerging(slot)) {
                    ItemStack slotStack = slot.getItem();

                    if (!slotStack.isEmpty()
                            && ItemStack.isSameItemSameComponents(stack, slotStack)
                            && slot.mayPlace(stack)) {

                        int combined = slotStack.getCount() + stack.getCount();
                        int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());

                        if (combined <= maxSize) {
                            stack.setCount(0);
                            slot.set(slotStack.copyWithCount( combined));
                            slot.setChanged();
                            merged = true;
                            break;
                        }

                        int space = maxSize - slotStack.getCount();
                        if (space > 0) {
                            stack.shrink(space);
                            slot.set(slotStack.copyWithCount(maxSize));
                            slot.setChanged();
                            merged = true;
                        }
                    }
                }

                index += reverseDirection ? -1 : 1;
            }
        }

        if (stack.isEmpty())
            return merged;

        // Try placing into empty slot
        int index = reverseDirection ? endIndex - 1 : startIndex;

        while (reverseDirection ? index >= startIndex : index < endIndex) {
            Slot slot = this.slots.get(index);

            if (isValidForMerging(slot)) {
                if (!slot.hasItem() && slot.getItem().isEmpty() && slot.mayPlace(stack)) {

                    if (stack.getCount() > slot.getMaxStackSize()) {
                        int toMove = Math.min(slot.getMaxStackSize(), stack.getCount());

                        ItemStack copy = stack.copy();
                        copy.setCount(toMove);

                        stack.shrink(toMove);
                        slot.set(copy);
                    } else {
                        slot.set(stack.copy());
                        stack.setCount(0);
                    }

                    slot.setChanged();
                    merged = true;
                    break;
                }
            }

            index += reverseDirection ? -1 : 1;
        }

        return merged;
    }

    public boolean isValidForMerging(Slot slot){
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateGuiSize(AUContainerScreen screen) {
        screen.setWidthAndHeight(this.width, this.height);
    }

    protected void validate() {
        boolean hasPriority = false;

        for (AUWidget widget : this.widgets) {
            widget.addToContainer(this);

            if (!hasPriority && widget instanceof TransferPriority) {
                hasPriority = true;
            }
        }

        if (hasPriority) {
            this.slots.sort((s1, s2) -> {
                int p1 = (s1 instanceof TransferPriority prio1) ? prio1.getPriority() : 0;

                int p2 = (s2 instanceof TransferPriority prio2) ? prio2.getPriority() : 0;

                int compare = -Integer.compare(p1, p2);

                return compare != 0 ? compare : Integer.compare(s1.index, s2.index);
            });

            for (int i = 0; i < this.slots.size(); i++) {
                Slot slot = this.slots.get(i);
                slot.index = i;
            }
        }
    }

    @Override
    public void clicked(int slot, int button, ClickType type, Player player) {
        if (slot >= 0 && slot < this.slots.size()) {
            Slot s = this.slots.get(slot);
            if (s instanceof AUSlotClickWidget widget)
                widget.onSlotClick(this,slot,button,type,player);
        }
        super.clicked(slot, button, type, player);
    }

    @OnlyIn(Dist.CLIENT)
    public void sendInputPacket(AUWidgetClientNetwork widget,AUMenuPacket packet){
        int i = widgetReceivers.indexOf(widget);
        if (i < 0) {
            AuxiliaUtilities.LOGGER.warn("Tried to send packet to widget {}, but it was not found in the menu's widget list.", widget);
            return;
        }
        PacketDistributor.sendToServer(new MenuInputPacket(i, packet));
    }
    
    public static class MenuInputPacket extends AUPacket {

        public static final StreamCodec<RegistryFriendlyByteBuf,MenuInputPacket> STREAM_CODEC = StreamCodec.of(
            MenuInputPacket::encode,
                MenuInputPacket::decode
        );

        private static void encode(RegistryFriendlyByteBuf buf, MenuInputPacket pkt) {
            buf.writeVarInt(pkt.widgetId);
            AUMenuPacket.encodeTyped(buf, pkt.packet);
        }

        private static MenuInputPacket decode(RegistryFriendlyByteBuf buf) {
            int widgetId = buf.readVarInt();
            AUMenuPacket packet = AUMenuPacket.decodeTyped(buf);
            return new MenuInputPacket(widgetId, packet);
        }

        final int widgetId;
        final AUMenuPacket packet;

        public MenuInputPacket(int widgetId, AUMenuPacket packet) {
            super(AUPackets.MENU_INPUT);
            this.widgetId = widgetId;
            this.packet = packet;
        }

        @Override
        protected void handle(IPayloadContext context) {
            context.enqueueWork(()->{
               if (context.player().containerMenu instanceof AUMenu menu){
                   if (widgetId < 0 || widgetId >= menu.widgetReceivers.size()) {
                       AuxiliaUtilities.LOGGER.warn("Received packet for widget id {}, but it's out of bounds. Ignoring packet.", widgetId);
                       return;
                   }
                   AUWidgetClientNetwork widget = menu.widgetReceivers.get(widgetId);
                   widget.receiveClientPacket(context,packet);
               }
            });
        }
    }
}



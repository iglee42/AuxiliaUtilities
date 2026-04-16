package fr.iglee42.auxiliautilities.items;

import com.google.common.collect.Multimap;
import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler.FlatTransferNode;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler.Type;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.network.AUPacket;
import fr.iglee42.auxiliautilities.network.AUPackets;
import fr.iglee42.auxiliautilities.utils.CommonKeysHandler;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class ItemFlatTransferNode extends AUItem {

    private final Type type;

    public ItemFlatTransferNode(Properties props, Type type) {
        super(props);
        this.type = type;
    }

    public static @Nullable FlatTransferNode getCurrentFlatTransferNode(@NotNull Player player){
        int length = 5;
        Vec3 startPos = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        Vec3 endPos = startPos.add(player.getLookAngle().scale(length));
        int cx0 = (int)Math.floor(Math.min(startPos.x, endPos.x)) >> 4;
        int cz0 = (int)Math.floor(Math.min(startPos.z, endPos.z)) >> 4;
        int cx1 = (int)Math.ceil(Math.max(startPos.x, endPos.x)) >> 4;
        int cz1 = (int)Math.ceil(Math.max(startPos.z, endPos.z)) >> 4;
        double bestDist = -1;
        FlatTransferNode bestNode = null;
        Level level = player.level();
        Multimap<BlockPos, FlatTransferNode> map = FlatTransferNodeHandler.getNodes(level);
        for (int cx = cx0; cx <= cx1; cx++) {
            for (int cz = cz0; cz <= cz1; cz++) {
                for (FlatTransferNode node : map.values()){
                    AABB bounds = node.getBounds();
                    Optional<Vec3> rayTrace = bounds.clip(startPos, endPos);
                    if (rayTrace.isPresent()){
                        double dist = startPos.distanceToSqr(rayTrace.get());
                        if (dist < bestDist || bestDist == -1){
                            bestDist = dist;
                            bestNode = node;
                        }
                    }
                }
            }
        }
        return bestNode;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();
        Direction side = ctx.getClickedFace();
        if (player == null) return InteractionResult.PASS;
        Level level = player.level();
        if (level.isClientSide){
            return InteractionResult.sidedSuccess(true);
        }

            FlatTransferNode existing = getCurrentFlatTransferNode(player);
            if (existing != null){
                player.openMenu(new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return stack.getDisplayName();
                    }

                    @Override
                    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                        return new FlatTransferNodeMenu(id,inv, existing);
                    }
                },buf->FlatTransferNode.STREAM_CODEC.encode(buf, existing));
                return InteractionResult.SUCCESS;
            }
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null){
            if ((type == Type.ITEM && level.getCapability(Capabilities.ItemHandler.BLOCK,pos,level.getBlockState(pos),be,side) != null)
            || (type == Type.FLUID && level.getCapability(Capabilities.FluidHandler.BLOCK,pos,level.getBlockState(pos),be,side) != null)){
                FlatTransferNode newNode = new FlatTransferNode(pos,side,type,!CommonKeysHandler.isHoldingSprint(player));
                boolean flag = false;
                for (FlatTransferNodeHandler.FlatTransferNode node : FlatTransferNodeHandler.getNodes(level).get(pos)) {
                    if (node.side == side) {
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    FlatTransferNodeHandler handler = FlatTransferNodeHandler.get(level);
                    handler.addNode(newNode);
                    handler.setDirty();
                    if (!player.isCreative()){
                        stack.shrink(1);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @SubscribeEvent
    public static void playerLeftClick(PlayerInteractEvent.LeftClickBlock event){
        if (InventoryHelper.isStackEmpty(event.getItemStack())) return;
        if (event.getLevel().isClientSide) return;
        if (event.getItemStack().getItem() instanceof ItemFlatTransferNode){
            if (leftClick(event.getLevel(), event.getEntity(), event.getItemStack())){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void playerLeftClick(PlayerInteractEvent.LeftClickEmpty event){
        if (InventoryHelper.isStackEmpty(event.getItemStack())) return;
        if (!event.getLevel().isClientSide) return;
        if (event.getItemStack().getItem() instanceof ItemFlatTransferNode ){
            PacketDistributor.sendToServer(new LeftClickFlatTransferNodePacket(event.getItemStack()));
        }
    }

    private static boolean leftClick(Level level,Player player,ItemStack stack){
        FlatTransferNode node = getCurrentFlatTransferNode(player);
        if (node == null) return false;
        if (!level.isClientSide) {
            node.isDead = true;
            FlatTransferNodeHandler handler = FlatTransferNodeHandler.get(level);
            if (handler.removeNode(node)) {
                node.dropItemStack(level);
                handler.setDirty();
            }
        }
        return true;
    }

    @Override
    public List<Component> getAdvancedTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        List<Component> tooltips = new ArrayList<>();
        tooltips.add(AULang.FLAT_TRANSFER_NODE_TOOLTIP.get());
        tooltips.add(AULang.FLAT_TRANSFER_NODE_TOOLTIP_1.get());
        tooltips.add(Component.empty());
        tooltips.add(AULang.FLAT_TRANSFER_NODE_TOOLTIP_2.get(Minecraft.getInstance().options.keySprint.getTranslatedKeyMessage().getString()));
        return tooltips;
    }

    public static class FlatTransferNodeMenu extends AUMenu {

        final FlatTransferNode node;
        public FlatTransferNodeMenu( int id,Inventory inv, RegistryFriendlyByteBuf buf) {
            this(id,inv,FlatTransferNode.STREAM_CODEC.decode(buf));
        }

        protected FlatTransferNodeMenu(int id, Inventory inv, FlatTransferNode node) {
            super(AUMenus.FLAT_TRANSFER_NODE.get(), id);
            this.node = node;
            addTitle(node.getDrop().getHoverName());
            addWidget(node.filter.getSlot(width / 2 - 8 , 20));
            cropAndAddPlayerSlots(inv);
            validate();
        }

        @Override
        public boolean stillValid(Player p_38874_) {
            return !node.isDead;
        }
    }

    public static class LeftClickFlatTransferNodePacket extends AUPacket {

        public static final StreamCodec<RegistryFriendlyByteBuf, LeftClickFlatTransferNodePacket> STREAM_CODEC = StreamCodec.composite(
                ItemStack.OPTIONAL_STREAM_CODEC, LeftClickFlatTransferNodePacket::getStack,
                LeftClickFlatTransferNodePacket::new
        );

        final ItemStack stack;

        public LeftClickFlatTransferNodePacket(ItemStack stack) {
            super(AUPackets.LEFT_CLICK_FLAT_TRANSFER_NODE);
            this.stack = stack;
        }

        public ItemStack getStack() {
            return stack;
        }

        @Override
        protected void handle(IPayloadContext context) {
            if (context.player() != null && context.player().level() != null){
                leftClick(context.player().level(), context.player(), stack);
            }
        }
    }
}

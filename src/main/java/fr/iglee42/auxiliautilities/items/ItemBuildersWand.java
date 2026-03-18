package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.utils.PositionPool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

import java.util.List;

public class ItemBuildersWand extends ItemSelectionWand{

    public ItemBuildersWand(Properties p_41383_, int range, float[] col) {
        super(p_41383_, range, col);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Direction side = ctx.getClickedFace();
        BlockState state = level.getBlockState(pos);
        ItemStack pickBlock = new ItemStack(state.getBlock().asItem());
        Player player = ctx.getPlayer();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!player.mayBuild()) return InteractionResult.FAIL;
        List<BlockPos> blocks = getPotentialBlocks(player,level,pos,side,this.range,pickBlock,state,state.getBlock());
        if (blocks.isEmpty()) return InteractionResult.FAIL;
        int slot = 0;
        int origSlot = player.getInventory().selected;
        ItemStack origStack = player.getInventory().getItem(origSlot);
        if (!Inventory.isHotbarSlot(origSlot) || origStack == null || origStack.isEmpty())
            return InteractionResult.FAIL;
        for (BlockPos p : blocks){
            ItemStack currentStack = null;
            if (!player.isCreative()) {
                for (; slot < player.getInventory().getContainerSize(); slot++) {
                    if (slot != origSlot) {
                        currentStack = player.getInventory().getItem(slot);
                        if (currentStack != null && !currentStack.isEmpty()) {
                            if (currentStack.getItem() == pickBlock.getItem()) break;
                        }
                    }
                }
            } else{
                currentStack = pickBlock.copy();
            }
            if (slot >= player.getInventory().getContainerSize() || currentStack == null || currentStack.isEmpty()) break;
            player.getInventory().setItem(origSlot,currentStack.copy());
            currentStack.useOn(new UseOnContext(level,player, InteractionHand.MAIN_HAND, currentStack, new BlockHitResult(
                    p.getCenter(),
                    side,
                    p,
                    false
            )));
            if (!player.isCreative())
                player.getInventory().setItem(slot,player.getInventory().getItem(origSlot));
            player.getInventory().setItem(origSlot,origStack);
            player.getInventory().setChanged();
            if (player instanceof ServerPlayer) ((ServerPlayer)player).inventoryMenu.broadcastFullState();

        }
        return InteractionResult.SUCCESS;
    }

    protected boolean initialCheck(Level level, BlockPos pos, Direction side, ItemStack stack, BlockState state) {
        BlockPos targetPos = pos.relative(side);

        return (
                level.getBlockState(targetPos).canBeReplaced()
        );
    }
    protected int getNumBlocks(Player player, int maxBlocks, ItemStack targetStack) {
        if (player.isCreative()) return maxBlocks;
        int count = 0;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);

            if (!stack.isEmpty()) {

                boolean sameBlock =
                        stack.getItem() == targetStack.getItem()
                                && stack.getDamageValue() == targetStack.getDamageValue();

                if (sameBlock) {

                    if (player.getAbilities().instabuild) {
                        return maxBlocks;
                    }

                    count += stack.getCount();
                }

                if (count >= maxBlocks) {
                    return maxBlocks;
                }
            }
        }

        return count;
    }


    protected boolean checkAndAddBlocks(
            Player player,
            Level level,
            Direction side,
            ItemStack stack,
            Block block,
            BlockState state,
            PositionPool pool,
            BlockPos pos,
            List<BlockPos> blocks
    ) {
        BlockPos targetPos = pool.offset(pos, side);
        if (!player.mayUseItemAt(targetPos, side, stack)) {
            return false;
        }
        if (!level.getBlockState( targetPos).canBeReplaced()) {
            return false;
        }

        blocks.add(targetPos);
        return true;
    }

    @SubscribeEvent
    @Override
    public void renderBlockOutline(RenderHighlightEvent.Block event) {
        drawSelection(event);
    }
}

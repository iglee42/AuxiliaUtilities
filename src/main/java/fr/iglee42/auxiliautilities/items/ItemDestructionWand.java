package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.utils.PositionPool;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemDestructionWand extends ItemSelectionWand{

    private static final Item[] HARVEST_DELEGATES = {Items.DIAMOND_PICKAXE,Items.DIAMOND_AXE,Items.DIAMOND_SHOVEL,Items.DIAMOND_HOE};
    private static final ToolMaterial SPEED_DELEGATES = ToolMaterial.GOLD;

    public ItemDestructionWand(Properties props, int range, float[] col) {
        super(props.component(DataComponents.TOOL,new Tool(
                combineRules(),
                1,
        0
        )), range, col);
    }

    private static List<Tool.Rule> combineRules() {
        List<Tool.Rule> rules = new ArrayList<>();
        for (int i = 0; i < HARVEST_DELEGATES.length; i++) {
            ItemStack harvestDelegate = HARVEST_DELEGATES[i].getDefaultInstance();
            if (harvestDelegate.has(DataComponents.TOOL)) {
                Tool harvestTool = harvestDelegate.get(DataComponents.TOOL);
                harvestTool.rules().forEach(rule->{
                    rules.add(new Tool.Rule(rule.blocks(), Optional.of(SPEED_DELEGATES.speed()),rule.correctForDrops()));
                });
            }
        }
        return rules;
    }

    @SubscribeEvent
    @Override
    public void renderBlockOutline(RenderHighlightEvent.Block event) {
        drawSelection(event);
    }

    @Override
    protected boolean initialCheck(Level level, BlockPos pos, Direction direction, ItemStack pickBlock, BlockState state) {
        return true;
    }

    @Override
    protected int getNumBlocks(Player player, int max, ItemStack stack) {
        return this.range;
    }

    @Override
    protected boolean checkAndAddBlocks(Player player, Level level, Direction side, ItemStack pickBlock, Block block, BlockState state, PositionPool pool, BlockPos pos, List<BlockPos> blocks) {
        if (!state.canHarvestBlock(level,pos,player))
            return false;
        BlockPos p1 = pos.relative(side);
        if (!player.getAbilities().flying && pos.getX() == player.getOnPos().getX() && pos.getY() == player.getOnPos().getY() && pos.getZ()== player.getOnPos().getZ())
            return false;
        BlockState state1 = level.getBlockState(p1);
        Block block1 = state1.getBlock();
        blocks.add(pos);
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (level.isEmptyBlock(pos)) return false;
        if (level.isClientSide) return false;
        if (!(entity instanceof Player player)) return false;
        HitResult hit = player.pick(20.0D, 0.0F, false);
        if (!(hit instanceof BlockHitResult blockHit)
                || !blockHit.getBlockPos().equals(pos)) {
            return false;
        }

        ItemStack pickBlock = new ItemStack(state.getBlock());
        List<BlockPos> potentialBlocks = getPotentialBlocks(player, level, pos,blockHit.getDirection(), this.range,pickBlock, state, state.getBlock());
        if (potentialBlocks.isEmpty()) return false;

        for (BlockPos targetPos : potentialBlocks) {
            BlockState targetState = level.getBlockState(targetPos);
            Block targetBlock = targetState.getBlock();

            if (!targetState.isAir()) {
                if (super.mineBlock(stack, level, targetState, targetPos, player)) {
                    boolean flag =  ((ServerPlayer) player).gameMode.removeBlock(targetPos, state, true);

                    if (flag) {
                        targetBlock.playerDestroy(level, player, targetPos, targetState, level.getBlockEntity(targetPos), stack.copy());
                    }
                }
            }
        }
        return true;
    }

    @SubscribeEvent
    public void blockBreakForCreative(BlockEvent.BreakEvent event){
        if (!event.getPlayer().isCreative() || !event.getPlayer().getMainHandItem().is(this)) return;
        event.getPlayer().getMainHandItem().mineBlock(event.getPlayer().level(),event.getState(),event.getPos(),event.getPlayer());
    }

    @SubscribeEvent
    public void adjustDigSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.isEmpty() || heldItem.getItem() != this) return;

        if (event.getPosition().isEmpty()) return;
        BlockPos pos = event.getPosition().get();
        Level level = player.level();

        if (level.isEmptyBlock(pos)) return;

        HitResult hit = player.pick(20.0D, 0.0F, false);

        if (!(hit instanceof BlockHitResult blockHit)
                || !blockHit.getBlockPos().equals(pos)) {
            event.setCanceled(true);
            return;
        }

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        ItemStack pickBlock = new ItemStack(block);

        List<BlockPos> potentialBlocks = getPotentialBlocks(
                player,
                level,
                pos,
                blockHit.getDirection(),
                this.range,
                pickBlock,
                state,
                block
        );

        if (potentialBlocks.isEmpty()) {
            event.setCanceled(true);
            return;
        }

        event.setNewSpeed(event.getNewSpeed() / potentialBlocks.size());
    }
}

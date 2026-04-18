package fr.iglee42.auxiliautilities.blocks.gp.consumers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.consumers.BEEnderPorcupine;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import fr.iglee42.auxiliautilities.blocks.gp.AUGPEntityBlock;
import fr.iglee42.auxiliautilities.items.ItemSelectionWand;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BlockEnderPorcupine extends AUBlock implements AUGPEntityBlock<BEEnderPorcupine> {


    public BlockEnderPorcupine(Properties props) {
        super(props);
    }

    @Override
    public @NotNull BlockEntityType<BEEnderPorcupine> type() {
        return AUBlockEntityTypes.ENDER_PORCUPINE.get();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState p_49849_, @Nullable LivingEntity entity, ItemStack p_49851_) {
        onPlace(level,pos,entity);
        super.setPlacedBy(level, pos, p_49849_, entity, p_49851_);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_60519_) {
        AUEntityBlock.onRemove(state,level,pos,newState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
        return openMenu(level,pos,player);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawSelection(RenderHighlightEvent.Block event) {
        if (!(event.getCamera().getEntity() instanceof Player player)) return;

        BlockHitResult target = event.getTarget();

        if (target.getType() != HitResult.Type.BLOCK)
            return;

        BlockPos bPos = target.getBlockPos();
        BlockEntity potBe = player.level().getBlockEntity(bPos);
        if (!(potBe instanceof BEEnderPorcupine be)) return;

        List<BlockPos> potentialBlocks = new ArrayList<>();

        BlockPos.betweenClosedStream(be.getTargetA(), be.getTargetB()).forEach(pos -> {
            potentialBlocks.add(bPos.offset(pos.immutable()));
        });

        if (potentialBlocks.isEmpty()) return;

        PoseStack poseStack = event.getPoseStack();
        Vec3 cam = event.getCamera().getPosition();
        VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
        Map<ItemSelectionWand.EdgeKey, Integer> edgeCounts = new LinkedHashMap<>();

        for (BlockPos pos : potentialBlocks) {
            for (double[][] line : ItemSelectionWand.edgeLines) {
                int x1 = pos.getX() + (int) Math.round(line[0][0]);
                int y1 = pos.getY() + (int) Math.round(line[0][1]);
                int z1 = pos.getZ() + (int) Math.round(line[0][2]);
                int x2 = pos.getX() + (int) Math.round(line[1][0]);
                int y2 = pos.getY() + (int) Math.round(line[1][1]);
                int z2 = pos.getZ() + (int) Math.round(line[1][2]);
                edgeCounts.merge(ItemSelectionWand.EdgeKey.of(x1, y1, z1, x2, y2, z2), 1, Integer::sum);
            }
        }

        PoseStack.Pose pose = poseStack.last();

        for (Map.Entry<ItemSelectionWand.EdgeKey, Integer> entry : edgeCounts.entrySet()) {
            if (entry.getValue() == 1) {
                ItemSelectionWand.renderPreviewEdge(consumer, pose, cam, entry.getKey(), 26/255F,94/255F,83/255F, 0.25f, 0.0025F);
            }
        }

        BlockPos pos = be.getBlockPos().offset(be.getTargetOffset());
        Map<ItemSelectionWand.EdgeKey, Integer> selectedEdges = new LinkedHashMap<>();
        for (double[][] line : ItemSelectionWand.edgeLines) {
            int x1 = pos.getX() + (int) Math.round(line[0][0]);
            int y1 = pos.getY() + (int) Math.round(line[0][1]);
            int z1 = pos.getZ() + (int) Math.round(line[0][2]);
            int x2 = pos.getX() + (int) Math.round(line[1][0]);
            int y2 = pos.getY() + (int) Math.round(line[1][1]);
            int z2 = pos.getZ() + (int) Math.round(line[1][2]);
            selectedEdges.merge(ItemSelectionWand.EdgeKey.of(x1, y1, z1, x2, y2, z2), 1, Integer::sum);
        }
        for (Map.Entry<ItemSelectionWand.EdgeKey, Integer> entry : selectedEdges.entrySet()) {
            if (entry.getValue() == 1) {
                ItemSelectionWand.renderPreviewEdge(consumer, pose, cam, entry.getKey(), 37/255F,132/255F,116/255F, 0.75f, 0.015F);
            }
        }

    }


    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!stack.isEmpty() && player.isCrouching() && !level.isClientSide){
            withBlockEntityDo(level,pos,be->{
                if (!be.getTargetOffset().equals(BlockPos.ZERO)) {
                    BlockPos targetPos = be.getBlockPos().offset(be.getTargetOffset());
                    ((ServerPlayer) player).gameMode.useItemOn(
                            (ServerPlayer) player,
                            level,
                            stack,
                            hand,
                            new BlockHitResult(targetPos.getCenter(), result.getDirection(), targetPos, result.isInside())
                    );
                }
            });
        }
        return super.useItemOn(stack, state, level, pos, player, hand, result);
    }

    @SubscribeEvent
    public static void allowUseWhenSneaking(PlayerInteractEvent.RightClickBlock event){
        Player player = event.getEntity();
        if (player.isCrouching()){
            BlockPos pos = event.getPos();
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof BEEnderPorcupine){
                event.setUseBlock(TriState.TRUE);
            }
        }
    }
}

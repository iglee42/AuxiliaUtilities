package fr.iglee42.auxiliautilities.items;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.*;
import fr.iglee42.auxiliautilities.utils.PlayerHelper;
import fr.iglee42.auxiliautilities.utils.PositionPool;
import fr.iglee42.auxiliautilities.utils.SideHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

public abstract class ItemSelectionWand extends Item {

    public static final float offset = 0.5f;

    static double[][][] edgeLines = new double[12][2][3];

    public final int range;
    public final float[] col;

    public ItemSelectionWand(Properties p_41383_, int range, float[] col) {
        super(p_41383_);
        this.range = range;
        this.col = col;
        NeoForge.EVENT_BUS.register(this);
    }

    static {
        for (int i = 0; i < SideHelper.edges.length; i++) {
            Direction[] edge = SideHelper.edges[i];
            Direction a = edge[0];
            Direction b = edge[1];
            Direction c = edge[2];

            double[] pos1 = new double[3];
            pos1[0] = 0.5 + (a.getStepX() + b.getStepX() + c.getStepX()) * offset;
            pos1[1] = 0.5 + (a.getStepY() + b.getStepY() + c.getStepY()) * offset;
            pos1[2] = 0.5 + (a.getStepZ() + b.getStepZ() + c.getStepZ()) * offset;

            double[] pos2 = new double[3];
            pos2[0] = 0.5 + (a.getStepX() + b.getStepX() - c.getStepX()) * offset;
            pos2[1] = 0.5 + (a.getStepY() + b.getStepY() - c.getStepY()) * offset;
            pos2[2] = 0.5 + (a.getStepZ() + b.getStepZ() - c.getStepZ()) * offset;

            double[][] result = new double[2][];
            result[0] = pos1;
            result[1] = pos2;
            edgeLines[i] = result;
        }

    }

    public List<BlockPos> getPotentialBlocks(Player player, Level level, BlockPos pos, Direction side, int maxBlocks, ItemStack pickBlock, BlockState state, Block block){
        if (level.isOutsideBuildHeight(pos) || level.isEmptyBlock(pos) || (!level.isClientSide() && !PlayerHelper.isPlayerReal(player)))
            return ImmutableList.of();
        if (pickBlock == null || pickBlock.isEmpty())
            return ImmutableList.of();
        if (!initialCheck(level, pos, side, pickBlock, state))
            return ImmutableList.of();

        int numBlocks = getNumBlocks(player, maxBlocks, pickBlock);
        if (numBlocks == 0)
            return ImmutableList.of();

        EnumSet<Direction> dirsToSearch = EnumSet.allOf(Direction.class);
        dirsToSearch.remove(side);
        dirsToSearch.remove(side.getOpposite());

        boolean crouching = player.isCrouching();
        boolean sprinting = false; // TODO implement packet to sync sprint key state

        if (sprinting){
            if (side.getStepY() == 0){
                Direction facing = player.getDirection().getAxis() == Direction.Axis.Y ? Direction.NORTH : player.getDirection().getClockWise();
                dirsToSearch.remove(facing);
                dirsToSearch.remove(facing.getOpposite());
            } else {
                dirsToSearch.remove(Direction.NORTH);
                dirsToSearch.remove(Direction.EAST);
                dirsToSearch.remove(Direction.WEST);
                dirsToSearch.remove(Direction.SOUTH);
            }
        } else if (crouching){
            if (side.getStepY() == 0){
                if (side.getStepY() == 0){
                    Direction facing = player.getDirection().getAxis() == Direction.Axis.Y ? Direction.NORTH : player.getDirection().getClockWise();
                    dirsToSearch.remove(facing);
                    dirsToSearch.remove(facing.getOpposite());
                } else {
                    dirsToSearch.remove(Direction.UP);
                    dirsToSearch.remove(Direction.DOWN);
                }
            }
        }
        if (dirsToSearch.isEmpty())
            return ImmutableList.of();

        LinkedHashSet<BlockPos> vecs = new LinkedHashSet<>();
        dirsToSearch.forEach(dir-> vecs.add(BlockPos.ZERO.relative(dir)));
        for (Direction dir1 : dirsToSearch) {
            for (Direction dir2 : dirsToSearch) {
                BlockPos offset = BlockPos.ZERO.relative(dir1).relative(dir2);
                if (Math.abs(offset.getX()) < 2 && Math.abs(offset.getY()) < 2 && Math.abs(offset.getZ()) < 2)
                    vecs.add(offset);
            }
        }
        HashSet<BlockState> states = new HashSet<>();
        states.add(state);
        states.addAll(List.copyOf(block.getStateDefinition().getPossibleStates()));

        LinkedList<BlockPos> queue = new LinkedList<>();
        Set<Vec3i> visited = new HashSet<>(numBlocks);
        PositionPool pool = new PositionPool();
        queue.add(pos);
        visited.add(pos);
        List<BlockPos> blocks = new ArrayList<>();
        BlockPos p;
        while (( p = queue.poll()) != null && blocks.size() < numBlocks){
            if (!states.contains(level.getBlockState(p))) continue;
            if (!checkAndAddBlocks(player, level, side, pickBlock, block, state, pool, p, blocks)) continue;
            for (BlockPos offset : vecs) {
                BlockPos p2 = pool.add(p, offset);

                if (!visited.contains(p2)) {
                    visited.add(p2);

                    int d = Math.max(
                            Math.max(
                                    Math.abs(p2.getX() - pos.getX()),
                                    Math.abs(p2.getY() - pos.getY())
                            ),
                            Math.abs(p2.getZ() - pos.getZ())
                    );

                    ListIterator<BlockPos> it = queue.listIterator();
                    boolean inserted = false;

                    while (it.hasNext()) {
                        BlockPos next = it.next();
                        int d2 = Math.max(
                                Math.max(
                                        Math.abs(next.getX() - pos.getX()),
                                        Math.abs(next.getY() - pos.getY())
                                ),
                                Math.abs(next.getZ() - pos.getZ())
                        );

                        if (d2 >= d) {
                            it.add(p2);
                            inserted = true;
                            break;
                        }
                    }

                    if (!inserted) {
                        queue.addLast(p2);
                    }
                }
            }
        }
        return blocks;
    }

    @OnlyIn(Dist.CLIENT)
    protected void drawSelection(RenderHighlightEvent.Block event) {
        if (!(event.getCamera().getEntity() instanceof Player player)) return;

        ItemStack current = player.getMainHandItem();
        BlockHitResult target = event.getTarget();

        if (current.isEmpty() || current.getItem() != this  || target.getType() != HitResult.Type.BLOCK)
            return;

        BlockPos bPos = target.getBlockPos();
        BlockState state = player.level().getBlockState(bPos);
        if (state.isAir()) return;

        Block block = state.getBlock();

        ItemStack pickBlock = new ItemStack(block, 1);

        List<BlockPos> potentialBlocks = getPotentialBlocks(
                player, player.level(), bPos, target.getDirection(),
                range, pickBlock, state, block
        );

        if (potentialBlocks.isEmpty()) return;

        event.setCanceled(true);

        PoseStack poseStack = event.getPoseStack();
        Vec3 cam = event.getCamera().getPosition();
        VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
        Map<EdgeKey, Integer> edgeCounts = new LinkedHashMap<>();

        for (BlockPos pos : potentialBlocks) {
            for (double[][] line : edgeLines) {
                int x1 = pos.getX() + (int) Math.round(line[0][0]);
                int y1 = pos.getY() + (int) Math.round(line[0][1]);
                int z1 = pos.getZ() + (int) Math.round(line[0][2]);
                int x2 = pos.getX() + (int) Math.round(line[1][0]);
                int y2 = pos.getY() + (int) Math.round(line[1][1]);
                int z2 = pos.getZ() + (int) Math.round(line[1][2]);
                edgeCounts.merge(EdgeKey.of(x1, y1, z1, x2, y2, z2), 1, Integer::sum);
            }
        }

        PoseStack.Pose pose = poseStack.last();

        for (Map.Entry<EdgeKey, Integer> entry : edgeCounts.entrySet()) {
            if (entry.getValue() == 1) {
                renderPreviewEdge(consumer, pose, cam, entry.getKey(), col[0], col[1], col[2], 0.95F, 0.015F);
            }
        }

        for (Map.Entry<EdgeKey, Integer> entry : edgeCounts.entrySet()) {
            if (entry.getValue() > 1) {
                renderPreviewEdge(consumer, pose, cam, entry.getKey(), col[0], col[1], col[2], 0.28F, 0.0F);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderPreviewEdge(VertexConsumer consumer, PoseStack.Pose pose, Vec3 cameraPos, EdgeKey edge, float red, float green, float blue, float alpha, float offsetAmount) {
        emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha, 0.0F, 0.0F, 0.0F);
        if (offsetAmount <= 0.0F) {
            return;
        }

        int axis = edge.axis();
        switch (axis) {
            case 0 -> {
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, offsetAmount, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, -offsetAmount, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, 0.0F, offsetAmount);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, 0.0F, -offsetAmount);
            }
            case 1 -> {
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, offsetAmount, 0.0F, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, -offsetAmount, 0.0F, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, 0.0F, offsetAmount);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, 0.0F, -offsetAmount);
            }
            case 2 -> {
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, offsetAmount, 0.0F, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, -offsetAmount, 0.0F, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, offsetAmount, 0.0F);
                emitEdge(consumer, pose, cameraPos, edge, red, green, blue, alpha * 0.7F, 0.0F, -offsetAmount, 0.0F);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void emitEdge(VertexConsumer consumer, PoseStack.Pose pose, Vec3 cameraPos, EdgeKey edge, float red, float green, float blue, float alpha, float xOffset, float yOffset, float zOffset) {
        float x1 = (float) (edge.ax() - cameraPos.x + xOffset);
        float y1 = (float) (edge.ay() - cameraPos.y + yOffset);
        float z1 = (float) (edge.az() - cameraPos.z + zOffset);
        float x2 = (float) (edge.bx() - cameraPos.x + xOffset);
        float y2 = (float) (edge.by() - cameraPos.y + yOffset);
        float z2 = (float) (edge.bz() - cameraPos.z + zOffset);
        float normalX = x2 - x1;
        float normalY = y2 - y1;
        float normalZ = z2 - z1;

        consumer.addVertex(pose, x1, y1, z1).setColor(red, green, blue, alpha).setNormal(normalX, normalY, normalZ);
        consumer.addVertex(pose, x2, y2, z2).setColor(red, green, blue, alpha).setNormal(normalX, normalY, normalZ);
    }

    private record EdgeKey(int ax, int ay, int az, int bx, int by, int bz) {
        private static EdgeKey of(int ax, int ay, int az, int bx, int by, int bz) {
            if (ax > bx || (ax == bx && ay > by) || (ax == bx && ay == by && az > bz)) {
                return new EdgeKey(bx, by, bz, ax, ay, az);
            }
            return new EdgeKey(ax, ay, az, bx, by, bz);
        }

        private int axis() {
            if (ax != bx) return 0;
            if (ay != by) return 1;
            return 2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void renderBlockOutline(RenderHighlightEvent.Block event);

    protected abstract boolean initialCheck(Level level, BlockPos pos, Direction direction, ItemStack pickBlock, BlockState state);

    protected abstract int getNumBlocks(Player player, int max, ItemStack stack);

    protected abstract boolean checkAndAddBlocks(Player player, Level level, Direction side, ItemStack pickBlock, Block block, BlockState state, PositionPool pool,BlockPos pos,List<BlockPos> blocks);
}

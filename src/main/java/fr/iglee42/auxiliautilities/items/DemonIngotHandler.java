package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class DemonIngotHandler {
    private static final Set<Block> netherBlocks = Set.of(
            Blocks.NETHER_BRICKS,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.NETHER_BRICK_STAIRS
    );

    private static final WeakHashMap<ItemEntity, Boolean> goldIngotsServer = new WeakHashMap<>();
    private static final WeakHashMap<ItemEntity, Boolean> goldIngotsClient = new WeakHashMap<>();

    public static ItemStack getRawStack(ItemEntity entityItem) {
        return entityItem.getItem();
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ItemEntity itemEntity) {
            ItemStack stack = getRawStack(itemEntity);

            if (stack.isEmpty() || stack.getItem() == Items.GOLD_INGOT) {
                if (event.getLevel().isClientSide()) {
                    goldIngotsClient.put(itemEntity, Boolean.TRUE);
                } else {
                    goldIngotsServer.put(itemEntity, Boolean.TRUE);
                }
            }
        }
    }

    @SubscribeEvent
    public static void run(ClientTickEvent.Pre event) {
        handleIngots(goldIngotsClient.keySet());
    }

    @SubscribeEvent
    public static void run(ServerTickEvent.Pre event) {
        handleIngots(goldIngotsServer.keySet());
    }

    private static void handleIngots(Collection<ItemEntity> goldIngots) {

        Iterator<ItemEntity> iterator = goldIngots.iterator();

        while (iterator.hasNext()) {
            ItemEntity goldIngotItem = iterator.next();

            if (goldIngotItem.isRemoved()) {
                iterator.remove();
                continue;
            }

            ItemStack stack = getRawStack(goldIngotItem);

            if (stack.isEmpty()) continue;

            if (stack.getItem() != Items.GOLD_INGOT) {
                iterator.remove();
                continue;
            }

            Level level = goldIngotItem.level();
            AABB bb = goldIngotItem.getBoundingBox().inflate(0.1);

            int x1 = Mth.floor(bb.minX);
            int x2 = Mth.ceil(bb.maxX);
            int y1 = Mth.floor(bb.minY);
            int y2 = Mth.ceil(bb.maxY);
            int z1 = Mth.floor(bb.minZ);
            int z2 = Mth.ceil(bb.maxZ);

            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            boolean found = false;

            outer:
            for (int x = x1; x < x2; x++) {
                for (int y = y1; y < y2; y++) {
                    for (int z = z1; z < z2; z++) {

                        pos.set(x, y, z);

                        if (level.getFluidState(pos).is(Fluids.LAVA)) {

                            found = true;

                            for (Direction dir : Direction.Plane.HORIZONTAL) {
                                if (!netherBlocks.contains(level.getBlockState(pos.relative(dir)).getBlock())) {
                                    found = false;
                                    break;
                                }
                            }

                            if (found) break outer;
                        }
                    }
                }
            }

            if (!found) continue;

            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {

                serverLevel.sendParticles(
                        ParticleTypes.LAVA,
                        goldIngotItem.getX(),
                        goldIngotItem.getY(),
                        goldIngotItem.getZ(),
                        100,
                        0, 0, 0,
                        0
                );

                goldIngotItem.discard();

                int count = stack.getCount();
                count = (int) Math.clamp( count * ((float)(100-level.random.nextInt(5,20))/ 100),3,count);

                ItemStack demonStack = new ItemStack(AUItems.DEMON_INGOT.get(), count);

                ItemEntity demonic =
                        new ItemEntity(level,
                                goldIngotItem.getX(),
                                goldIngotItem.getY(),
                                goldIngotItem.getZ(),
                                demonStack);

                level.addFreshEntity(demonic);

                iterator.remove();
            }
        }
    }
}

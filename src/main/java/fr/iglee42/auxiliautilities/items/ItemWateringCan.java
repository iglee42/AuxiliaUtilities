package fr.iglee42.auxiliautilities.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;

public class ItemWateringCan extends Item {
    public ItemWateringCan(Properties props) {
        super(props.durability(1000).component(DataComponents.DAMAGE,1000));
    }

    @Override
    public int getUseDuration(ItemStack p_41454_, LivingEntity p_344979_) {
        return 72000;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        if (!level.isClientSide && player instanceof FakePlayer) {
            int damage = stack.getDamageValue();

            if (level.getBlockState(pos).getFluidState().isSource()) {
                damage = Math.max(0, damage - 20);
                stack.setDamageValue(damage);
            } else if (damage < stack.getMaxDamage()) {
                waterLocation(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        context.getClickedFace(), stack, player, 1, 4.0);
                stack.setDamageValue(Math.min(damage + 20, stack.getMaxDamage()));
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (!(entity instanceof Player player)) {
            entity.releaseUsingItem();
            return;
        }

        int damage = stack.getDamageValue();

        HitResult hit = player.pick(20.0D, 0.0F, true);
        if (!(hit instanceof BlockHitResult blockHit)) return;

        BlockPos pos = blockHit.getBlockPos();

        if (level.getBlockState(pos).getFluidState().isSource()) {
            if (!level.isClientSide && !player.getAbilities().instabuild) {
                damage = Math.max(0, damage - 5);
                stack.setDamageValue(damage);
            }
            return;
        }

        if (damage >= stack.getMaxDamage()) {
            player.releaseUsingItem();
            return;
        }

        waterLocation(level,
                blockHit.getLocation().x,
                blockHit.getLocation().y,
                blockHit.getLocation().z,
                blockHit.getDirection(),
                stack,
                player,
                player.getAbilities().instabuild ? 3 : 1,
                player.getAbilities().instabuild ? 1.0 / 3.0 : 1.0
        );

        if (!player.getAbilities().instabuild) {
            stack.setDamageValue(Math.min(damage + 1, stack.getMaxDamage()));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        HitResult hit = player.pick(20.0D, 0.0F, true);

        if (hit instanceof BlockHitResult blockHit) {
            BlockPos pos = blockHit.getBlockPos();

            if (level.getBlockState(pos).getFluidState().isSource()) {
                player.startUsingItem(hand);
                return InteractionResultHolder.success(stack);
            }
        }

        if (stack.getDamageValue() >= stack.getMaxDamage() && !player.getAbilities().instabuild) {
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.success(stack);
    }

    private void waterLocation(Level level, double x, double y, double z,
                               Direction side, ItemStack stack, Player player,
                               int range, double speed) {

        if (!level.isClientSide) {
            List<LivingEntity> waterSensitive = level.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(x - range, y - range, z - range, x + range, y + 6, z + range),
                    LivingEntity::isSensitiveToWater
            );

            for (LivingEntity e : waterSensitive) {
                e.hurt(level.damageSources().drown(), 1.0F);
            }
        }

        if (level.isClientSide) {
            Vec3 dir = Vec3.atLowerCornerOf(side.getNormal());

            for (int i = 0; i < 4 * range; i++) {
                level.addParticle(ParticleTypes.SPLASH,
                        x + dir.x * 0.1 + level.random.nextGaussian() * 0.6 * range,
                        y + dir.y * 0.1,
                        z + dir.z * 0.1 + level.random.nextGaussian() * 0.6 * range,
                        0, 0, 0);
            }
            return;
        }

        for (Entity e : level.getEntities(null,
                new AABB(x - range, y - range, z - range, x + range, y + range + 6, z + range))) {

            if (e.isOnFire()) {
                e.clearFire();
            }
        }

        BlockPos center = BlockPos.containing(x, y, z);

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-range, -range, -range),
                center.offset(range, range, range))) {

            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.FIRE)) {
                level.removeBlock(pos, false);
            }

            // Grow plants (random tick simulation)
            if (state.isRandomlyTicking()) {
                state.randomTick((ServerLevel) level, pos, level.random);
            }
        }
    }
}

package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import fr.iglee42.auxiliautilities.mixins.ArrowAccessor;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID, value = Dist.CLIENT)
public class ItemCompoundBow extends BowItem implements AUItemBase {

    public ItemCompoundBow(Properties properties) {
        super(properties);
    }


    private static float getArrowVelocityCustom(int charge) {
        float f = charge / 20.0F;
        if (f < 0.0F)
            return 0.0F;
        f = (f + (float)Math.sqrt(f) * 2.0F) / 3.0F;
        if (f > 1.0F)
            f = 1.0F;
        return f == 1.0F ? f * 1.25f : f;
    }

    @Override
    public void releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        if (p_40669_ instanceof Player player) {
            ItemStack itemstack = player.getProjectile(p_40667_);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(p_40667_, p_40669_) - p_40670_;
                i = net.neoforged.neoforge.event.EventHooks.onArrowLoose(p_40667_, p_40668_, player, i, !itemstack.isEmpty());
                if (i < 0) return;
                float f = getArrowVelocityCustom(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(p_40667_, itemstack, player);
                    if (p_40668_ instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, player, player.getUsedItemHand(), p_40667_, list, f * 3.0F, 1.0F, f == 1.0F, null);
                    }

                    p_40668_.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F / (p_40668_.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    @Override
    protected void shootProjectile(LivingEntity p_331372_, Projectile projectile, int p_330631_, float velocity, float p_331199_, float p_330857_, @Nullable LivingEntity p_331572_) {
        super.shootProjectile(p_331372_, projectile, p_330631_, velocity, p_331199_, p_330857_, p_331572_);
        if (projectile instanceof AbstractArrow arrow && velocity == getArrowVelocityCustom(20) * 3.0F) {
            arrow.getPersistentData().putBoolean("IsBlueArrow", true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void tickClient(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof AbstractArrow blueArrow)) return;
        if (blueArrow.level() == null || blueArrow.level().isClientSide()) return;
        if (!blueArrow.getPersistentData().getBoolean("IsBlueArrow")  || ((ArrowAccessor)blueArrow).isInGround()) return;

        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 4; k++) {
                double radius = i / 5.0 * 0.1;

                double offsetX = blueArrow.getDeltaMovement().x * k / 4.0 + blueArrow.level().random.nextGaussian() * radius;
                double offsetY = blueArrow.getDeltaMovement().y * k / 4.0 + blueArrow.level().random.nextGaussian() * radius;
                double offsetZ = blueArrow.getDeltaMovement().z * k / 4.0 + blueArrow.level().random.nextGaussian() * radius;

                double red = 0.36;
                double green = 0.59;
                double blue = 0.88;

                ((ServerLevel)blueArrow.level()).sendParticles(
                        new DustParticleOptions(new Vector3f((float) red, (float) green, (float) blue),1.0f),
                        blueArrow.getX() + offsetX,
                        blueArrow.getY() + offsetY,
                        blueArrow.getZ() + offsetZ,
                        1,
                        0,0,0,
                        0.001
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltips, TooltipFlag flag) {
        addTooltips(stack, tooltips, ctx, flag);
        super.appendHoverText(stack, ctx, tooltips, flag);
    }
}

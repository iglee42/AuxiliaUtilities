package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import fr.iglee42.auxiliautilities.potions.AUMobEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.UUID;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class DoomEffect extends AUMobEffect {

    private static final UUID MESSAGE_UUID = UUID.fromString("9f8c2d1a-7b64-4e3f-a91c-5d2b8f0e4c73");
    public static final ResourceKey<DamageType> DOOM_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, AuxiliaUtilities.id("doom"));

    public DoomEffect() {
        super(MobEffectCategory.HARMFUL, 3149840);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(AUMobEffects.DOOM);
        if (instance != null){
            int duration = instance.getDuration();
            if (entity.level().isClientSide){
                int time = duration / 20;
                if (time <= 0) return super.applyEffectTick(entity, amplifier);
                if ((time <= 10 || time % 10 == 0) && duration % 20 == 0 && entity instanceof Player player){
                    AULang.DOOM_MESSAGE.sendToPlayer(player,MESSAGE_UUID,time);
                }
            } else if (duration < 4){
                entity.hurt(entity.damageSources().source(DOOM_DAMAGE), Float.MAX_VALUE);
            }
        }

        return super.applyEffectTick(entity, amplifier);
    }

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event){
        if (!(event.getEffect().value() instanceof DoomEffect)) return;
        event.setCanceled(true);
    }
}

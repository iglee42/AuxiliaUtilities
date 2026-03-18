package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import fr.iglee42.auxiliautilities.potions.AUMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import java.util.*;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class RelapseMobEffect extends AUMobEffect {

    WeakHashMap<LivingEntity, Map<Holder<MobEffect>, MobEffectInstance>> effects = new WeakHashMap<>();

    public RelapseMobEffect() {
        super(MobEffectCategory.NEUTRAL, 6312191);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295329_, int p_295167_) {
        return true;
    }

    /*@Override
    public boolean applyEffectTick(LivingEntity entity, int p_19468_) {
        Map<Holder<MobEffect>, MobEffectInstance> oldEffects, currentEffects;
        if (entity.level().isClientSide) return true;
        ServerLevel level = (ServerLevel) entity.level();
        Collection<MobEffectInstance> current = entity.getActiveEffects();
        if (current.isEmpty()) {
            currentEffects = Collections.emptyMap();
            oldEffects = this.effects.remove(entity);
        } else {
            currentEffects = new HashMap<>();
            for (MobEffectInstance instance : current) {
                Holder<MobEffect> effect = instance.getEffect();
                if (!effect.isBound()){
                    continue;
                }
                if (effect.value() != this){
                    if (effect.value().getCategory() == MobEffectCategory.HARMFUL)
                        currentEffects.put(effect, new MobEffectInstance(instance));
                }
            }
            if (currentEffects.isEmpty()){
                oldEffects = this.effects.remove(entity);
            } else {
                oldEffects = this.effects.put(entity,currentEffects);
            }
        }
        if (oldEffects == null) return true;

        RandomSource random = level.getRandom();
        for (Holder<MobEffect> effect : oldEffects.keySet()){
            if (!currentEffects.containsKey(effect)){
                MobEffectInstance instance = oldEffects.get(effect);
                int amplifier = instance.getAmplifier();
                if (amplifier == 0){
                    if (random.nextInt(6) == 0)
                        continue;
                } else if (random.nextInt(2) == 0)
                    amplifier--;
                int duration = instance.getDuration() >> 1;
                if (duration <= 0) continue;
                duration += random.nextInt(duration);
                MobEffectInstance newInstance = new MobEffectInstance(instance.getEffect(), duration,amplifier,instance.isAmbient(),instance.isVisible());
                entity.addEffect(newInstance);
            }
        }


        return super.applyEffectTick(entity, p_19468_);
    }*/

    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event){
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!entity.hasEffect(AUMobEffects.RELAPSE)) return;
        if (event.getEffect().is(AUMobEffects.RELAPSE)){
            event.setCanceled(true);
            return;
        }
        if (event.getEffect().value().isBeneficial()) return;
        MobEffectInstance instance = event.getEffectInstance();
        int amplifier = instance.getAmplifier();
        if (amplifier == 0){
            if (entity.level().getRandom().nextInt(6) == 0) {
                return;
            }
        } else if (entity.level().getRandom().nextInt(2) == 0) {
            amplifier--;
        }
        int duration = instance.getDuration() >> 1;
        if (duration <= 0) return;
        duration += entity.level().random.nextInt(duration);
        MobEffectInstance newInstance = new MobEffectInstance(instance.getEffect(), duration,amplifier,instance.isAmbient(),instance.isVisible());
        event.setCanceled(true);
        entity.removeEffectNoUpdate(instance.getEffect());
        entity.addEffect(newInstance);
    }
}


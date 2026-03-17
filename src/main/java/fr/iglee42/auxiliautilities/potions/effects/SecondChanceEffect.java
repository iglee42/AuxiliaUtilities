package fr.iglee42.auxiliautilities.potions.effects;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.potions.AUMobEffect;
import fr.iglee42.auxiliautilities.potions.AUMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.UUID;

public class SecondChanceEffect extends AUMobEffect {
    private static final UUID MESSAGE_UUID = UUID.fromString("9f8c2d1a-7b64-4e3f-a91c-5d2b8f0e4c74");

    public SecondChanceEffect() {
        super(MobEffectCategory.BENEFICIAL, 6356848);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
        MobEffectInstance instance = entity.getEffect(AUMobEffects.SECOND_CHANCE);
        if (instance == null) return;
        CompoundTag nbt = entity.getPersistentData();
        if (nbt.getBoolean("SecondChanceUsed")){
            if (entity instanceof Player p){
                AULang.SECOND_CHANCE_ALREADY_USED_MESSAGE.sendToPlayer(p,MESSAGE_UUID);
            }
            return;
        }
        entity.removeEffect(AUMobEffects.SECOND_CHANCE);
        if (instance.getAmplifier() > 0){
            entity.addEffect(
                    new MobEffectInstance(instance.getEffect(), instance.getDuration(), instance.getAmplifier() - 1, instance.isAmbient(), instance.isVisible(), instance.showIcon())
            );
        }
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,200));
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS,100));
        entity.setHealth(0.01f);
        entity.heal(entity.getMaxHealth() * 0.5f);
        if (entity instanceof Player p){
            AULang.SECOND_CHANCE_MESSAGE.sendToPlayer(p,MESSAGE_UUID);
        }
        nbt.putBoolean("SecondChanceUsed", true);
        event.setCanceled(true);
    }
}

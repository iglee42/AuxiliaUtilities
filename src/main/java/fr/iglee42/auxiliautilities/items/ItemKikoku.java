package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemKikoku extends AUItem {

    public static final ResourceLocation SOUL_DAMAGE_ID =
            AuxiliaUtilities.id("soul_damage");

    private static final double SOUL_DAMAGE_STEP = 1D / 39D;

    public ItemKikoku() {
        super( new Item.Properties()
                        .stacksTo(1)
                        .sword( ToolMaterial.NETHERITE,
                                10,
                                -2.4f)
                .fireResistant()
        );
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!(attacker instanceof Player player))
            return;

        Level level = target.level();

        if (level.isClientSide)
            return;

        double vx = target.getDeltaMovement().x;
        double vy = target.getDeltaMovement().y;
        double vz = target.getDeltaMovement().z;

        applySoulDamage(target);

        DamageSource divineDamage = player.damageSources().playerAttack(player);
        target.hurt(divineDamage, 4.0F);

        DamageSource armorBypass = player.damageSources().generic();
        target.hurt(armorBypass, 3.0F);

        target.setDeltaMovement(vx, vy, vz);

        return;
    }

    private void applySoulDamage(LivingEntity target) {

        AttributeInstance maxHealth =
                target.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null)
            return;

        AttributeModifier existing =
                maxHealth.getModifier(SOUL_DAMAGE_ID);

        double value = 0;

        if (existing != null)
            value = existing.amount();

        value -= SOUL_DAMAGE_STEP;

        if (value <= -1.0) {

            DamageSource source =
                    target.damageSources().fellOutOfWorld();

            target.hurt(source, Float.MAX_VALUE);
            return;
        }

        if (existing != null)
            maxHealth.removeModifier(existing);

        maxHealth.addPermanentModifier(
                new AttributeModifier(
                        SOUL_DAMAGE_ID,
                        value,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
        );
    }

}
package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
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

public class ItemKikoku extends SwordItem implements AUItemBase {

    public static final ResourceLocation SOUL_DAMAGE_ID =
            AuxiliaUtilities.id("soul_damage");

    private static final double SOUL_DAMAGE_STEP = 1D / 39D;

    public ItemKikoku() {
        super(
                Tiers.NETHERITE,
                new Item.Properties()
                        .stacksTo(1)
                        .attributes(createAttributes())
        );
    }

    private static ItemAttributeModifiers createAttributes() {

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        builder.add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        BASE_ATTACK_DAMAGE_ID,
                        10,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

        builder.add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        BASE_ATTACK_SPEED_ID,
                        -2.4F,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

        return builder.build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!(attacker instanceof Player player))
            return false;

        Level level = target.level();

        if (level.isClientSide)
            return true;

        double vx = target.getDeltaMovement().x;
        double vy = target.getDeltaMovement().y;
        double vz = target.getDeltaMovement().z;

        applySoulDamage(target);

        DamageSource divineDamage = player.damageSources().playerAttack(player);
        target.hurt(divineDamage, 4.0F);

        DamageSource armorBypass = player.damageSources().generic();
        target.hurt(armorBypass, 3.0F);

        target.setDeltaMovement(vx, vy, vz);

        return true;
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

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        addTooltips(p_41421_, p_41423_, p_339594_, p_41424_);
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
    }
}
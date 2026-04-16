package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.function.Consumer;

public class ItemLuxSaber extends SwordItem implements AUItemBase {

    public static final int MAX_ENERGY = 40000;
    public static final int ENERGY_THRESHOLD = 200;

    public static final Tier TIER = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            0,
            5.0F,
            7.0F,
            15,
            ()-> Ingredient.of(ItemStack.EMPTY)
    );

    public ItemLuxSaber(Properties props) {
        super(TIER, props.component(AUDataComponents.STORED_ENERGY,0).component(DataComponents.BASE_COLOR,DyeColor.WHITE));
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers(stack);
        if (stack.getOrDefault(AUDataComponents.STORED_ENERGY,0) >= ENERGY_THRESHOLD){
            modifiers = createAttributes(TIER,3f,0f);
        }
        return modifiers.withModifierAdded(
                Attributes.BLOCK_INTERACTION_RANGE,
                new AttributeModifier(AuxiliaUtilities.id("lux_saber_block_bonus"),
                        3, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).withModifierAdded(
                Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(AuxiliaUtilities.id("lux_saber_entity_bonus"),
                        3, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        );
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {
        for (DyeColor color : DyeColor.values()) {
            for (boolean charged : new boolean[]{true, false}) {
                acceptor.accept(getStack(color, charged));
            }
        }
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getOrDefault(AUDataComponents.STORED_ENERGY,0) >= ENERGY_THRESHOLD){
            if (!(attacker instanceof Player player) || !player.isCreative()) stack.set(AUDataComponents.STORED_ENERGY, stack.getOrDefault(AUDataComponents.STORED_ENERGY,0) - ENERGY_THRESHOLD);
            target.igniteForSeconds(5);
        }
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int energy = stack.getOrDefault(AUDataComponents.STORED_ENERGY,0);
        return Math.round(13.0F * energy / MAX_ENERGY);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (stack.has(DataComponents.BASE_COLOR)){
            DyeColor color = stack.getOrDefault(DataComponents.BASE_COLOR,DyeColor.WHITE);
            return color.getTextColor();
        }
        return super.getBarColor(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        int energy = stack.getOrDefault(AUDataComponents.STORED_ENERGY,0);
        return energy < MAX_ENERGY;
    }

    public static ItemStack getStack(DyeColor color, boolean charged){
        ItemStack stack = new ItemStack(AUItems.LUX_SABER.get());
        stack.set(AUDataComponents.STORED_ENERGY, charged ? MAX_ENERGY : 0);
        stack.set(DataComponents.BASE_COLOR, color);
        return stack;
    }


    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex == 1){
            DyeColor color = stack.getOrDefault(DataComponents.BASE_COLOR,DyeColor.WHITE);
            return color.getTextureDiffuseColor();
        }
        return AUItemBase.super.getColor(stack,tintIndex);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (!stack.has(DataComponents.BASE_COLOR)) return super.getDescriptionId(stack);
        DyeColor color = stack.getOrDefault(DataComponents.BASE_COLOR,DyeColor.WHITE);
        return super.getDescriptionId(stack) + "." + color.getSerializedName();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltips, TooltipFlag flag) {
        addTooltips(stack, tooltips, ctx, flag);
        super.appendHoverText(stack, ctx, tooltips, flag);
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        int energy = stack.getOrDefault(AUDataComponents.STORED_ENERGY,0);
        return List.of(AULang.STORED_ENERGY_TOOLTIP.get(formatInt(energy), formatInt(MAX_ENERGY)).withStyle(ChatFormatting.GRAY));
    }

    public static class ItemEnergyStorage implements IEnergyStorage {
        protected final ItemStack stack;

        protected int capacity;

        protected int maxReceive;

        protected int maxExtract;

        public ItemEnergyStorage(ItemStack stack) {
            this.stack = stack;
            this.capacity = MAX_ENERGY;
            this.maxReceive = ENERGY_THRESHOLD;
            this.maxExtract = ENERGY_THRESHOLD;
        }

        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive())
                return 0;
            int energyReceived = Math.min(this.capacity - getEnergyStored(), Math.min(this.maxReceive, maxReceive));
            if (!simulate)
                setEnergyStored(getEnergyStored() + energyReceived);
            return energyReceived;
        }

        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract())
                return 0;
            int energyExtracted = Math.min(getEnergyStored(), Math.min(this.maxExtract, maxExtract));
            if (!simulate)
                setEnergyStored(getEnergyStored() - energyExtracted);
            return energyExtracted;
        }

        public int getEnergyStored() {
            return stack.getOrDefault(AUDataComponents.STORED_ENERGY,0);
        }

        private void setEnergyStored(int energy) {
            this.stack.set(AUDataComponents.STORED_ENERGY, energy);
        }

        public int getMaxEnergyStored() {
            return this.capacity;
        }

        public boolean canExtract() {
            return (this.maxExtract > 0);
        }

        public boolean canReceive() {
            return (this.maxReceive > 0);
        }

    }
}

package fr.iglee42.auxiliautilities.blockentities.generators;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.potions.AUPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

import static fr.iglee42.auxiliautilities.blockentities.generators.BEGenPotion.getPotionEnergy;
import static fr.iglee42.auxiliautilities.config.AUConfig.DISENCHANTMENT_RATE;

public class BEGenDisenchantment extends AUGeneratorBlockEntity{

    public BEGenDisenchantment(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.DISENCHANTMENT_GENERATOR.get(), pos, state);
    }

    @Override
    protected int requiredItems() {
        return 1;
    }

    @Override
    protected int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    protected int getProgressPerItem(int slot, ItemStack stack) {
        return getBookEnergy(stack) / DISENCHANTMENT_RATE.get();
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return DISENCHANTMENT_RATE.get();
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return getBookEnergy(stack) > 0;
    }

    public static int getBookEnergy(ItemStack stack) {
        if (!stack.isEnchanted() && stack.getItem() != Items.ENCHANTED_BOOK)
            return 0;

        Map<Holder<Enchantment>, Integer> enchantments = new HashMap<>();
        stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet()
                .forEach(entry -> enchantments.put(entry.getKey(), entry.getIntValue()));

        if (enchantments.isEmpty())
            return 0;

        double amount = 0.0D;

        for (Map.Entry<Holder<Enchantment>, Integer> entry : enchantments.entrySet()) {

            Holder<Enchantment> enchantment = entry.getKey();
            if (enchantment == null || !enchantment.isBound() || enchantment.value() == null)
                continue;

            int rawLevel = entry.getValue();
            int level = 1 + rawLevel;

            int weight = enchantment.value().getWeight();

            double v =
                    Math.sqrt(
                            Math.min(level, enchantment.value().getMaxLevel()) / (double) enchantment.value().getMaxLevel()
                    ) *
                            enchantment.value().getMaxLevel() *
                            level *
                            enchantment.value().getMaxLevel();

            amount += v / Math.sqrt(weight) *
                    Math.max(1, enchantment.value().getMinCost(rawLevel));
        }

        return (int) Math.ceil(amount) * 400;
    }

    @Override
    protected ItemStack getReturnItem(int slot, ItemStack stack) {
        return new ItemStack(Items.BOOK);
    }
}

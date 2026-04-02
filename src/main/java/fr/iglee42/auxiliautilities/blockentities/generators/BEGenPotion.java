package fr.iglee42.auxiliautilities.blockentities.generators;

import com.mojang.datafixers.util.Pair;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blocks.BlockCrusher;
import fr.iglee42.auxiliautilities.potions.AUPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BEGenPotion extends AUGeneratorBlockEntity{
    public BEGenPotion(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.POTION_GENERATOR.get(), pos, state);
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
        return getPotionEnergy(stack).getSecond();
    }

    @Override
    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        return getPotionEnergy(item).getFirst();
    }

    @Override
    protected boolean isItemValid(int slot, ItemStack stack) {
        return stack.has(DataComponents.POTION_CONTENTS);
    }

    public static Pair<Integer, Integer> getPotionEnergy(ItemStack stack) {

        if (stack.isEmpty())
            return Pair.of(0, 0);

        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null)
            return Pair.of(0, 0);

        if (contents.is(Potions.WATER))
            return Pair.of(10, 10);

        if (contents.is(Potions.AWKWARD) || contents.is(Potions.THICK) || contents.is(Potions.MUNDANE) || contents.is(AUPotions.OILY.getDelegate()))
            return Pair.of(20, 20);

        int complexity = 0;

        for (MobEffectInstance effect : contents.getAllEffects()) {

            int amplifier = effect.getAmplifier();
            int duration = effect.getDuration();

            complexity += (amplifier + 1) * 2;
            complexity += (int) (Math.log(duration / 20.0 + 1) / Math.log(2));
        }

        if (complexity <= 0)
            return Pair.of(0, 0);

        int rate = 20 * complexity;
        int burnTime = 25 * complexity;

        return Pair.of(rate, burnTime);
    }

    @Override
    protected ItemStack getReturnItem(int slot, ItemStack stack) {
        if (stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION))
            return new ItemStack(Items.GLASS_BOTTLE);
        if (stack.is(Items.TIPPED_ARROW))
            return new ItemStack(Items.ARROW);
        return super.getReturnItem(slot, stack);
    }
}

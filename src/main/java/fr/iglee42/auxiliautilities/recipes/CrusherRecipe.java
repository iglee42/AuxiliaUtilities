package fr.iglee42.auxiliautilities.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.auxiliautilities.recipes.inputs.CrusherRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CrusherRecipe implements Recipe<CrusherRecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final ItemStack secondResult;
    private final float chance;
    private final int energy;
    @Nullable
    private PlacementInfo placementInfo;

    public CrusherRecipe(Ingredient ingredient, ItemStack result, ItemStack secondResult, float chance, int energy) {
        this.ingredient = ingredient;
        this.result = result;
        this.secondResult = secondResult;
        this.chance = chance;
        this.energy = energy;
    }

    @Override
    public boolean matches(CrusherRecipeInput input, Level p_345375_) {
        return ingredient.test(input.getItem(0)) && input.getEnergy() >= energy;
    }


    @Override
    public ItemStack assemble(CrusherRecipeInput input, HolderLookup.Provider p_346030_) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<CrusherRecipeInput>> getSerializer() {
        return AURecipes.CRUSHER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<CrusherRecipeInput>> getType() {
        return Type.INSTANCE;
    }

    @Override
    public PlacementInfo placementInfo() {
        if (placementInfo == null)
            placementInfo = PlacementInfo.create(ingredient);
        return placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return AURecipes.CRUSHER_CATEGORY.get();
    }


    public ItemStack getResult() {
        return result;
    }

    public ItemStack getSecondResult() {
        return secondResult;
    }

    public float getChance() {
        return chance;
    }

    public boolean hasSecondResult() {
        return !secondResult.isEmpty() && chance > 0;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getEnergy() {
        return energy;
    }


    public static class Type implements RecipeType<CrusherRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "crusher";
    }
    public static class Serializer implements RecipeSerializer<CrusherRecipe> {

        private static final MapCodec<CrusherRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.CODEC.fieldOf("ingredient").forGetter(e -> e.ingredient),
                                ItemStack.CODEC.fieldOf("result").forGetter(e->e.result),
                                ItemStack.CODEC.optionalFieldOf("second_result",ItemStack.EMPTY).forGetter(e->e.secondResult),
                                Codec.floatRange(0,1).optionalFieldOf("second_chance",0.0f).forGetter(e->e.chance),
                                ExtraCodecs.POSITIVE_INT.fieldOf("energy").forGetter(e->e.energy)
                        )
                        .apply(instance, CrusherRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, CrusherRecipe::getIngredient,
                ItemStack.STREAM_CODEC, CrusherRecipe::getResult,
                ItemStack.OPTIONAL_STREAM_CODEC, CrusherRecipe::getSecondResult,
                ByteBufCodecs.FLOAT, CrusherRecipe::getChance,
                ByteBufCodecs.INT, CrusherRecipe::getEnergy,
                CrusherRecipe::new
        );


        @Override
        public MapCodec<CrusherRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
package fr.iglee42.auxiliautilities.recipes;

import javax.annotation.Nullable;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import fr.iglee42.auxiliautilities.recipes.inputs.EnchanterRecipeInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;

public class EnchanterRecipe implements Recipe<EnchanterRecipeInput> {

    private final SizedIngredient ingredient;
    private final SizedIngredient lapisIngredient;
    private final ItemStack result;
    private final int energy;
    @Nullable
    private PlacementInfo placementInfo;

    public EnchanterRecipe(SizedIngredient ingredient,SizedIngredient lapisIngredient, ItemStack result, int energy) {
        this.ingredient = ingredient;
        this.lapisIngredient = lapisIngredient;
        this.result = result;
        this.energy = energy;
    }

    @Override
    public boolean matches(EnchanterRecipeInput input, Level p_345375_) {
        return ingredient.test(input.getItem(0)) && lapisIngredient.test(input.getItem(1)) && input.getEnergy() >= energy;
    }


    @Override
    public ItemStack assemble(EnchanterRecipeInput input, HolderLookup.Provider p_346030_) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<EnchanterRecipeInput>> getSerializer() {
        return AURecipes.ENCHANTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<EnchanterRecipeInput>> getType() {
        return Type.INSTANCE;
    }

    public ItemStack getResult() {
        return result;
    }

    public SizedIngredient getIngredient() {
        return ingredient;
    }

    public int getEnergy() {
        return energy;
    }

    public SizedIngredient getLapisIngredient() {
        return lapisIngredient;
    }


    @Override
    public PlacementInfo placementInfo() {
        if (placementInfo == null)
            placementInfo = PlacementInfo.create(List.of(ingredient.ingredient(),lapisIngredient.ingredient()));
        return placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return AURecipes.ENCHANTER_CATEGORY.get();
    }

    public static class Type implements RecipeType<EnchanterRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "enchanter";
    }
    public static class Serializer implements RecipeSerializer<EnchanterRecipe> {

        private static final Codec<SizedIngredient> INGREDIENT_CODEC = Codec.withAlternative(SizedIngredient.NESTED_CODEC,
                ExtraCodecs.POSITIVE_INT, count -> new SizedIngredient(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(Tags.Items.GEMS_LAPIS)), count));

        private static final MapCodec<EnchanterRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                SizedIngredient.NESTED_CODEC.fieldOf("ingredient").forGetter(e -> e.ingredient),
                                INGREDIENT_CODEC.fieldOf("lapis").forGetter(e->e.lapisIngredient),
                                ItemStack.CODEC.fieldOf("result").forGetter(e->e.result),
                                ExtraCodecs.POSITIVE_INT.fieldOf("energy").forGetter(e->e.energy)
                        )
                        .apply(p_340782_, EnchanterRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> STREAM_CODEC = StreamCodec.composite(
                SizedIngredient.STREAM_CODEC, EnchanterRecipe::getIngredient,
                SizedIngredient.STREAM_CODEC, EnchanterRecipe::getLapisIngredient,
                ItemStack.STREAM_CODEC, EnchanterRecipe::getResult,
                ByteBufCodecs.INT, EnchanterRecipe::getEnergy,
                EnchanterRecipe::new
        );


        @Override
        public MapCodec<EnchanterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
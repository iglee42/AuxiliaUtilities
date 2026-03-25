package fr.iglee42.auxiliautilities.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResonatorRecipe implements Recipe<RecipeInput> {

    private final Ingredient ingredient;
    private final ItemStack result;
    private final int requiredGP;
    private final boolean requiresRainbowGenerator;

    public ResonatorRecipe(Ingredient ingredient, ItemStack result, int requiredGP, boolean requiresRainbowGenerator) {
        this.ingredient = ingredient;
        this.result = result;
        this.requiredGP = requiredGP;
        this.requiresRainbowGenerator = requiresRainbowGenerator;
    }

    @Override
    public boolean matches(RecipeInput input, Level p_345375_) {
        return ingredient.test(input.getItem(0));
    }


    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider p_346030_) {
        return getResultItem(p_346030_);
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@Nullable HolderLookup.Provider p_336125_) {
        return result.copy();
    }

    public ItemStack getResult() {
        return result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getRequiredGP() {
        return requiredGP;
    }

    public boolean doesRequiresRainbowGenerator() {
        return requiresRainbowGenerator;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AURecipes.RESONATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ResonatorRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "resonator";
    }
    public static class Serializer implements RecipeSerializer<ResonatorRecipe> {

        private static final MapCodec<ResonatorRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p_340782_ -> p_340782_.group(
                                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(e->e.ingredient),
                                ItemStack.CODEC.fieldOf("result").forGetter(e->e.result),
                                Codec.INT.fieldOf("required_gp").forGetter(e->e.requiredGP),
                                Codec.BOOL.optionalFieldOf("requires_rainbow_generator",false).forGetter(e->e.requiresRainbowGenerator)
                        )
                        .apply(p_340782_, ResonatorRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ResonatorRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,ResonatorRecipe::getIngredient,
                ItemStack.STREAM_CODEC,ResonatorRecipe::getResult,
                ByteBufCodecs.INT, ResonatorRecipe::getRequiredGP,
                ByteBufCodecs.BOOL,ResonatorRecipe::doesRequiresRainbowGenerator,
                ResonatorRecipe::new
        );


        @Override
        public MapCodec<ResonatorRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ResonatorRecipe> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
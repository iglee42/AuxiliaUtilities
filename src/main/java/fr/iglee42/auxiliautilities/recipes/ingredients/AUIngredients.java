package fr.iglee42.auxiliautilities.recipes.ingredients;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AUIngredients {

    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, AuxiliaUtilities.MODID);


    public static final Supplier<IngredientType<LassoIngredient>> LASSO =
            INGREDIENT_TYPES.register("lasso",
                    () -> new IngredientType<>(LassoIngredient.CODEC, LassoIngredient.STREAM_CODEC));
}

package fr.iglee42.auxiliautilities.recipes;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AURecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, AuxiliaUtilities.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AuxiliaUtilities.MODID);

    public static final DeferredHolder<RecipeType<?>,RecipeType<ResonatorRecipe>> RESONATOR = RECIPE_TYPES.register(ResonatorRecipe.Type.ID, ()->ResonatorRecipe.Type.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ResonatorRecipe>> RESONATOR_SERIALIZER = RECIPE_SERIALIZERS.register(ResonatorRecipe.Type.ID, ResonatorRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>,RecipeType<EnchanterRecipe>> ENCHANTER = RECIPE_TYPES.register(EnchanterRecipe.Type.ID, ()->EnchanterRecipe.Type.INSTANCE);
    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<EnchanterRecipe>> ENCHANTER_SERIALIZER = RECIPE_SERIALIZERS.register(EnchanterRecipe.Type.ID, EnchanterRecipe.Serializer::new);

    public static void register(IEventBus bus){
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}

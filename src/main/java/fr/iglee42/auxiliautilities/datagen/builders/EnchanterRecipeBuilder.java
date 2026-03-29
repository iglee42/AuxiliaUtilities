package fr.iglee42.auxiliautilities.datagen.builders;

import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public class EnchanterRecipeBuilder {

    public static void enchanter(RecipeOutput consumer, ResourceLocation id, ItemLike ingredient,int lapisCount, Item output, int energy){
        enchanter(consumer,id,SizedIngredient.of(ingredient,1),lapisCount,new ItemStack(output),energy);
    }

    public static void enchanter(RecipeOutput consumer, ResourceLocation id, SizedIngredient ingredient, int lapisCount, ItemStack output, int energy){
        enchanter(consumer,id,ingredient, SizedIngredient.of(Tags.Items.GEMS_LAPIS,lapisCount),output,energy);
    }

    public static void enchanter(RecipeOutput consumer, ResourceLocation id, SizedIngredient ingredient, SizedIngredient lapisIngredient, ItemStack output, int energy){
        consumer.accept(id,new EnchanterRecipe(ingredient,lapisIngredient,output,energy),null);
    }
}

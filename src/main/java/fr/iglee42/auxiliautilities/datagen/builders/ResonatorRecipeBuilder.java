package fr.iglee42.auxiliautilities.datagen.builders;

import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ResonatorRecipeBuilder {

    public static void resonator(RecipeOutput consumer, ResourceLocation id, ItemLike ingredient, Item output, int gp){
        resonator(consumer,id,Ingredient.of(ingredient),new ItemStack(output),gp,false);
    }

    public static void resonator(RecipeOutput consumer, ResourceLocation id, Ingredient ingredient, ItemStack output, int gp){
        resonator(consumer,id,ingredient,output,gp,false);
    }

    public static void resonator(RecipeOutput consumer, ResourceLocation id, Ingredient ingredient, ItemStack output, int gp, boolean requireRainbowGenerator){
        consumer.accept(id,new ResonatorRecipe(ingredient,output,gp,requireRainbowGenerator),null);
    }
}

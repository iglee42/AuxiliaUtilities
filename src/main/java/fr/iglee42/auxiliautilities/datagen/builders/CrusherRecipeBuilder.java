package fr.iglee42.auxiliautilities.datagen.builders;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.recipes.CrusherRecipe;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class CrusherRecipeBuilder {

    public static void singleRecipe(HolderGetter<Item> items, RecipeOutput consumer, String id, TagKey<Item> ingredient, ItemLike output, int energy){
        singleRecipe(items,consumer,id,ingredient,output,1,energy);
    }


    public static void singleRecipe(RecipeOutput consumer, String id, ItemLike ingredient, ItemLike output, int energy){
        singleRecipe(consumer,id,ingredient,output,1,energy);
    }

    public static void singleRecipe(HolderGetter<Item> items, RecipeOutput consumer, String id, TagKey<Item> ingredient, ItemLike output, int outputCount, int energy){
        crusher(consumer,id,Ingredient.of(items.getOrThrow(ingredient)),new ItemStack(output,outputCount),ItemStack.EMPTY,0.0f,energy);
    }


    public static void singleRecipe(RecipeOutput consumer, String id, ItemLike ingredient, ItemLike output,int outputCount, int energy){
        crusher(consumer,id,Ingredient.of(ingredient),new ItemStack(output,outputCount),ItemStack.EMPTY,0.0f,energy);
    }



    public static void crusher(RecipeOutput consumer, String id, ItemLike ingredient, ItemLike output,ItemLike secondOutput, float chance, int energy){
        crusher(consumer,id,ingredient,output,1,secondOutput,1,chance,energy);
    }

    public static void crusher(HolderGetter<Item> items, RecipeOutput consumer, String id, TagKey<Item> ingredient, ItemLike output,ItemLike secondOutput, float chance, int energy){
        crusher(items,consumer,id,ingredient,output,1,secondOutput,1,chance,energy);
    }

    public static void crusher(RecipeOutput consumer, String id, ItemLike ingredient, ItemLike output,int outputCount,ItemLike secondOutput,int secondOutputCount, float chance, int energy){
        crusher(consumer,id,Ingredient.of(ingredient),new ItemStack(output,outputCount),new ItemStack(secondOutput,secondOutputCount),chance,energy);
    }

    public static void crusher(HolderGetter<Item> items, RecipeOutput consumer, String id, TagKey<Item> ingredient, ItemLike output,int outputCount,ItemLike secondOutput,int secondOutputCount, float chance, int energy){
        crusher(consumer,id,Ingredient.of(items.getOrThrow(ingredient)),new ItemStack(output,outputCount),new ItemStack(secondOutput,secondOutputCount),chance,energy);
    }



    public static void crusher(RecipeOutput consumer, String id, Ingredient ingredient, ItemStack output,ItemStack secondOutput,float chance, int energy){
        chance = Mth.clamp(chance,0,1);
        consumer.accept(ResourceKey.create(Registries.RECIPE,AuxiliaUtilities.id("crusher/"+id)),new CrusherRecipe(ingredient,output, secondOutput,chance, energy),null);
    }
}

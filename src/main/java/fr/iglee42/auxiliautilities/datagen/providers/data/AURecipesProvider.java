package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.items.ItemLuxSaber;
import fr.iglee42.auxiliautilities.items.ItemSunCrystal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.concurrent.CompletableFuture;

public class AURecipesProvider extends RecipeProvider {
    public AURecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,AUItems.GLASS_CUTTER)
                .pattern("  I")
                .pattern(" SI")
                .pattern("I  ")
                .define('I',Tags.Items.INGOTS_IRON)
                .define('S',Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.ENDER_SHARD.get(), 8)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(AUItems.GLASS_CUTTER)
                .unlockedBy("has_item", has(AUItems.GLASS_CUTTER))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL)
                .requires(AUItems.ENDER_SHARD.get(),8)
                .unlockedBy("has_item", has(AUItems.ENDER_SHARD))
                .save(output, AuxiliaUtilities.id("ender_pearl_from_shards"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,AUItems.WATERING_CAN)
                .pattern("S  ")
                .pattern("SBS")
                .pattern(" S ")
                .define('B',Items.BOWL)
                .define('S',Tags.Items.STONES)
                .unlockedBy("has_item", has(Items.BOWL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUItems.UNSTABLE_INGOT)
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S',Tags.Items.RODS_WOODEN)
                .define('D',Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUItems.STABLE_UNSTABLE_INGOT)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', AUItems.STABLE_UNSTABLE_NUGGET.get())
                .unlockedBy("has_item", has(AUItems.STABLE_UNSTABLE_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUItems.STABLE_UNSTABLE_NUGGET)
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('S',Tags.Items.RODS_WOODEN)
                .define('D',Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,AUItems.STABLE_UNSTABLE_NUGGET,9)
                .requires(AUItems.STABLE_UNSTABLE_INGOT)
                .unlockedBy("has_item", has(AUItems.STABLE_UNSTABLE_INGOT))
                .save(output,AuxiliaUtilities.id("stable_unstable_nugget_from_ingot"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.ANGEL_BLOCK.get())
                .pattern(" G ")
                .pattern("WOW")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('W',Items.FEATHER)
                .define('O', Tags.Items.OBSIDIANS_NORMAL)
                .unlockedBy("has_item", has(Items.FEATHER))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,AUItems.SUN_CRYSTAL.get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        for (DyeColor color : DyeColor.values()){
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemLuxSaber.getStack(color,false))
                    .pattern("DGD")
                    .pattern("DCD")
                    .pattern("DRD")
                    .define('D',Tags.Items.INGOTS_IRON)
                    .define('C', DataComponentIngredient.of(true, DataComponents.DAMAGE,0,AUItems.SUN_CRYSTAL.get()))
                    .define('G', BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getName() + "_stained_glass")))
                    .define('R',Tags.Items.DUSTS_REDSTONE)
                    .unlockedBy("has_item", has(AUItems.SUN_CRYSTAL.get()))
                    .save(output, AuxiliaUtilities.id("lux_saber_" + color.getName()));
        }

    }
}

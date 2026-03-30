package fr.iglee42.auxiliautilities.jei;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.jei.categories.EnchanterCategory;
import fr.iglee42.auxiliautilities.jei.categories.ResonatorCategory;
import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

@JeiPlugin
public class AUJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath("igleemods", "auxiliautilities");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ResonatorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new EnchanterCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(AUBlocks.RESONATOR, ResonatorCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AUBlocks.ENCHANTER, EnchanterCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(AUBlocks.FURNACE, RecipeTypes.SMELTING);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ResonatorCategory.RECIPE_TYPE,
                new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ResonatorRecipe.Type.INSTANCE)));
        registration.addRecipes(EnchanterCategory.RECIPE_TYPE,
                new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(EnchanterRecipe.Type.INSTANCE)));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(
                AUContainerScreen.class,
                new AUContainerScreenHandler(registration.getJeiHelpers())
        );
    }
}

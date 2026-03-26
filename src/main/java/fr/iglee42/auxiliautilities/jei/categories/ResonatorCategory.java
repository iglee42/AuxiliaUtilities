package fr.iglee42.auxiliautilities.jei.categories;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;

public class ResonatorCategory implements IRecipeCategory<RecipeHolder<ResonatorRecipe>> {

    public static final RecipeType<RecipeHolder<ResonatorRecipe>> RECIPE_TYPE = RecipeType.createFromVanilla(
            ResonatorRecipe.Type.INSTANCE);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable slot;


    public ResonatorCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AUBlocks.RESONATOR.get()));
        this.arrow = helper.createAnimatedRecipeArrow(230);
        this.slot = helper.getSlotDrawable();
    }


    @Override
    public @NotNull RecipeType<RecipeHolder<ResonatorRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return AULang.RESONATOR.get();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(RecipeHolder<ResonatorRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        arrow.draw(stack,47,14);
        stack.drawString(Minecraft.getInstance().font,  holder.value().getRequiredGP() +" GP",47,5, Color.GRAY.getRGB(),false);
        slot.draw(stack,18,13);
        slot.draw(stack,78,13);
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipeHolder<ResonatorRecipe> holder, @Nonnull IFocusGroup focusGroup) {
        ResonatorRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT,19, 14).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT,79, 14).addIngredient(VanillaTypes.ITEM_STACK,recipe.getResult());
    }

}
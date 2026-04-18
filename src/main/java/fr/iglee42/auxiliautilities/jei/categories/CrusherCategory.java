package fr.iglee42.auxiliautilities.jei.categories;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.recipes.CrusherRecipe;
import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class CrusherCategory implements IRecipeCategory<RecipeHolder<CrusherRecipe>> {

    public static final IRecipeHolderType<CrusherRecipe> RECIPE_TYPE = IRecipeHolderType.create(
            CrusherRecipe.Type.INSTANCE);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable slot;


    public CrusherCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(136, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AUBlocks.CRUSHER.get()));
        this.arrow = helper.createAnimatedRecipeArrow(230);
        this.slot = helper.getSlotDrawable();
    }


    @Override
    public @NotNull IRecipeType<RecipeHolder<CrusherRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return AULang.CRUSHER.get();
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
    public void draw(RecipeHolder<CrusherRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        arrow.draw(stack, 47,14);
        slot.draw(stack, 18,13);
        slot.draw(stack, 78,13);
        slot.draw(stack, 98,13);
        int energy = holder.value().getEnergy();
        long baseTimeTicks = Math.max(1L, energy / 40L);
        stack.drawString(Minecraft.getInstance().font,  NumberFormat.getInstance(Locale.UK).format(energy) +" FE " + AULang.formatDurationSeconds(baseTimeTicks, true),27,40, Color.GRAY.getRGB(),false);
        stack.drawString(Minecraft.getInstance().font,  NumberFormat.getPercentInstance(Locale.UK).format(holder.value().getChance()),99,33, Color.GRAY.getRGB(),false);
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull RecipeHolder<CrusherRecipe> holder, @Nonnull IFocusGroup focusGroup) {
        CrusherRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 14).add(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 14).add(recipe.getResult());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 14).add(recipe.getSecondResult());
    }

}
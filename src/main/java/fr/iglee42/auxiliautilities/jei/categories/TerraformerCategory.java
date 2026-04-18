package fr.iglee42.auxiliautilities.jei.categories;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.terraformer.BETerraformerExtension;
import fr.iglee42.auxiliautilities.blockentities.terraformer.TerraformerType;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class TerraformerCategory implements IRecipeCategory<TerraformerCategory.ExtensionWrapper> {

    public static final IRecipeType<ExtensionWrapper> RECIPE_TYPE = IRecipeType.create(AuxiliaUtilities.MODID, "terraformer",
            ExtensionWrapper.class);


    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable slot;

    public TerraformerCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(136, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AUBlocks.TERRAFORMER));
        this.arrow = helper.createAnimatedRecipeArrow(230);
        this.slot = helper.getSlotDrawable();
    }


    @Override
    public IRecipeType<ExtensionWrapper> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return AULang.TERRAFORMER.get();
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
    public void draw(ExtensionWrapper wrapper, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {

        arrow.draw(stack, 47,17);
        slot.draw(stack,24,16);
        stack.drawString(Minecraft.getInstance().font,  NumberFormat.getInstance(Locale.UK).format((long) wrapper.tfEnergy * BETerraformerExtension.INCREASE_MULTIPLIER) +" TF",75,22, Color.GRAY.getRGB(),false);
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ExtensionWrapper wrapper, @Nonnull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.CRAFTING_STATION,25,0).add(AUBlocks.ANTENNA);
        builder.addSlot(RecipeIngredientRole.INPUT,25,17).add(wrapper.ingredient);
        builder.addSlot(RecipeIngredientRole.CRAFTING_STATION,25,35).add(AUBlocks.BLOCKS.getEntries().stream().filter(b->b.getId().getPath().equals(wrapper.type.getSerializedName())).findFirst().map(DeferredHolder::get)
                .map(ItemLike::asItem).orElse(Items.AIR));
    }


    public record ExtensionWrapper(Ingredient ingredient, int tfEnergy, TerraformerType type){

        public ExtensionWrapper(ItemLike item, int tfEnergy,TerraformerType type){
            this(Ingredient.of(item),tfEnergy,type);
        }

        public ExtensionWrapper(TagKey<Item> item, int tfEnergy,TerraformerType type){
            this(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(item)),tfEnergy,type);
        }

    }

}
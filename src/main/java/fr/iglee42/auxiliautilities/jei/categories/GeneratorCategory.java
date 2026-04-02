package fr.iglee42.auxiliautilities.jei.categories;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockGenerator;
import fr.iglee42.auxiliautilities.menu.widgets.AUFluidTankWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SingleFluidIngredient;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GeneratorCategory implements IRecipeCategory<GeneratorCategory.GeneratorWrapper> {

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;
    private final IDrawable slot;
    private final DeferredBlock<BlockGenerator> block;

    public static RecipeType<GeneratorWrapper> getGeneratorRecipeType(DeferredBlock<BlockGenerator> block){
        return RecipeType.create(AuxiliaUtilities.MODID, block.getId().getPath(), GeneratorWrapper.class);
    }

    public GeneratorCategory(IGuiHelper helper,DeferredBlock<BlockGenerator> block) {
        this.background = helper.createBlankDrawable(136, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));
        this.arrow = helper.createAnimatedRecipeArrow(230);
        this.slot = helper.getSlotDrawable();
        this.block = block;
    }


    @Override
    public @NotNull RecipeType<GeneratorWrapper> getRecipeType() {
        return getGeneratorRecipeType(block);
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable(block.get().getDescriptionId());
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
    public void draw(GeneratorWrapper wrapper, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        int count = wrapper.ingredients.size() + (!wrapper.fluid.isEmpty() ? 1 : 0);
        int inputStartX = 58 - (count - 1) * 20;
        int inputIndex = 0;
        if (count > 0) {

            for (int slot = 0; slot < wrapper.ingredients.size(); slot++) {
                this.slot.draw(stack,inputStartX + inputIndex * 20 - 1,13);
                inputIndex++;
            }

            if (!wrapper.fluid.isEmpty()) {
                this.slot.draw(stack,inputStartX + inputIndex * 20 - 1,13);
                inputIndex++;
            }
        }
        arrow.draw(stack, inputStartX + inputIndex * 20 - 1,14);

        int energy = wrapper.energyPerTick * wrapper.time;
        stack.drawString(Minecraft.getInstance().font,  NumberFormat.getInstance(Locale.UK).format(energy) +" FE " + AULang.formatDurationSeconds(wrapper.time, true) + " " + wrapper.energyPerTick + " FE/tick",5,40, Color.GRAY.getRGB(),false);
    }


    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull GeneratorWrapper wrapper, @Nonnull IFocusGroup focusGroup) {
        int count = wrapper.ingredients.size() + (!wrapper.fluid.isEmpty() ? 1 : 0);
        if (count > 0) {
            int inputStartX = 58 - (count - 1) * 20;
            int inputIndex = 0;

            for (int slot = 0; slot < wrapper.ingredients.size(); slot++) {
                var ingredient = wrapper.ingredients.get(slot);
                if (block.is(AUBlocks.SLIMEY_GENERATOR.getId()) && ingredient.getValues()[0] instanceof Ingredient.TagValue tv && tv.tag().equals(Tags.Items.SLIME_BALLS)){
                    builder.addInputSlot(inputStartX + inputIndex * 20,14)
                            .addIngredients(VanillaTypes.ITEM_STACK, Arrays.asList(SizedIngredient.of(tv.tag(),4).getItems()));
                    inputIndex++;
                    continue;
                }
                builder.addInputSlot(inputStartX + inputIndex * 20,14)
                        .addIngredients(ingredient);
                inputIndex++;
            }

            if (!wrapper.fluid.isEmpty()) {
                builder.addInputSlot(inputStartX + inputIndex * 20,14)
                        .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(wrapper.fluid.getStacks()));
            }
        }
    }


    public record GeneratorWrapper(List<Ingredient> ingredients, FluidIngredient fluid, int energyPerTick, int time){

        public GeneratorWrapper(ItemLike item,FluidStack fluid,int energyPerTick,int time){
            this(List.of(Ingredient.of(item)),FluidIngredient.of(fluid),energyPerTick,time);
        }

        public GeneratorWrapper(TagKey<Item> item, FluidStack fluid, int energyPerTick, int time){
            this(List.of(Ingredient.of(item)),FluidIngredient.of(fluid),energyPerTick,time);
        }

        public GeneratorWrapper(ItemLike item, int energyPerTick, int time){
            this(List.of(Ingredient.of(item)),FluidIngredient.empty(),energyPerTick,time);
        }

        public GeneratorWrapper(TagKey<Item> item, int energyPerTick, int time){
            this(List.of(Ingredient.of(item)),FluidIngredient.empty(),energyPerTick,time);
        }

        public GeneratorWrapper(ItemLike item, TagKey<Fluid> fluid, int energyPerTick, int time){
            this(List.of(Ingredient.of(item)),FluidIngredient.tag(fluid),energyPerTick,time);
        }

        public GeneratorWrapper(TagKey<Item> item, TagKey<Fluid> fluid, int energyPerTick, int time){
            this(List.of(Ingredient.of(item)),FluidIngredient.tag(fluid),energyPerTick,time);
        }

    }

}
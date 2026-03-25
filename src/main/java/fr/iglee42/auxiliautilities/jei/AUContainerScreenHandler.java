package fr.iglee42.auxiliautilities.jei;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.AUWidgetBase;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetJEICategory;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AUContainerScreenHandler implements IGuiContainerHandler<AUContainerScreen> {


    private final IJeiHelpers jeiHelpers;

    public AUContainerScreenHandler(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(AUContainerScreen screen, double guiMouseX, double guiMouseY) {
        AUMenu menu = screen.getMenu();
        return menu.getWidgets().stream().filter(AUWidgetBase.class::isInstance).map(AUWidgetBase.class::cast)
                .filter(w->w.categoryId() != null)
                .filter(w->jeiHelpers.getRecipeType(w.categoryId()).isPresent())
                .map(w->createArea(
                        w.getX(),
                        w.getY(),
                        w.getWidth(),
                        w.getHeight(),
                        jeiHelpers.getRecipeType(w.categoryId()).get()
                )).collect(Collectors.toSet());
    }

    IGuiClickableArea createArea(int xPos, int yPos, int width, int height, RecipeType<?>... recipeTypes) {
        Rect2i area = new Rect2i(xPos, yPos, width, height);
        List<RecipeType<?>> recipeTypesList = Arrays.asList(recipeTypes);
        return new IGuiClickableArea() {
            @Override
            public Rect2i getArea() {
                return area;
            }

            @Override
            public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
                recipesGui.showTypes(recipeTypesList);
            }

            @Override
            public void getTooltip(ITooltipBuilder tooltip) {
                tooltip.add(Component.empty());
            }
        };
    }
}

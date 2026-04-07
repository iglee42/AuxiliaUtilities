package fr.iglee42.auxiliautilities.jei;

import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.items.ItemFilterFluid;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.widgets.AUWidgetBase;
import fr.iglee42.auxiliautilities.menu.widgets.api.AUWidgetJEICategory;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotGhostWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotWidget;
import fr.iglee42.auxiliautilities.network.SubmitGhostItemPacket;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

public class AUContainerScreenHandler implements IGuiContainerHandler<AUContainerScreen>, IGhostIngredientHandler<AUContainerScreen> {


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

    @Override
    public <I> List<Target<I>> getTargetsTyped(AUContainerScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        List<Target<I>> targets = new ArrayList<>();
        if (ingredient.getType() == VanillaTypes.ITEM_STACK){
            for (int i = 0; i < gui.getMenu().slots.size() - 36; i++) {
                Slot slot = gui.getMenu().slots.get(i);
                if (slot instanceof SlotGhostWidget ) {
                    targets.add(new GhostTarget<>(gui, i));
                }
            }
        }
        if (ingredient.getType() == NeoForgeTypes.FLUID_STACK && gui.getMenu() instanceof ItemFilterFluid.FilterConfigContainer){
            for (int i = 0; i < gui.getMenu().slots.size() - 36; i++) {
                Slot slot = gui.getMenu().slots.get(i);
                if (slot instanceof SlotGhostWidget ) {
                    targets.add(new GhostTarget<>(gui, i));
                }
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {}

    @Override
    public boolean shouldHighlightTargets() { return true; }

    private static class GhostTarget<I> implements Target<I>{

        private final AUContainerScreen screen;
        private final int slot;
        private final Rect2i area;

        public GhostTarget(AUContainerScreen screen, int slot) {
            this.screen = screen;
            this.slot = slot;
            Slot slotW = screen.getMenu().slots.get(slot);
            if (slotW instanceof SlotWidget slotWidget) {
                this.area = new Rect2i(screen.getGuiLeft() + slotWidget.getX(), screen.getGuiTop() + slotWidget.getY(), slotWidget.getWidth(), slotWidget.getHeight());
            } else {
                this.area = new Rect2i(screen.getGuiLeft() + slotW.x, screen.getGuiTop() + slotW.y, 16, 16);
            }
        }

        @Override
        public Rect2i getArea() {
            return area;
        }

        @Override
        public void accept(I ingredient) {
            ItemStack stack = ItemStack.EMPTY;
            if (ingredient instanceof ItemStack) {
                stack = ((ItemStack) ingredient).copy();
            } else if (ingredient instanceof FluidStack) {
                FluidStack fluidStack = ((FluidStack) ingredient).copy();
                fluidStack.setAmount(1000);
                stack = fluidStack.getFluidType().getBucket(fluidStack).copy();
            }
            stack.setCount(1);
            if (screen.getMenu().getSlot(slot) instanceof SlotGhostWidget ghostSlot && !stack.isEmpty()) {
                ghostSlot.set(stack);
                PacketDistributor.sendToServer(new SubmitGhostItemPacket(stack, slot));
            }
        }
    }
}

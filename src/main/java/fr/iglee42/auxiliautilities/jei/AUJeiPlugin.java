package fr.iglee42.auxiliautilities.jei;

import fr.iglee42.auxiliautilities.blockentities.generators.*;
import fr.iglee42.auxiliautilities.blockentities.terraformer.AUTerraformerDataMaps;
import fr.iglee42.auxiliautilities.blockentities.terraformer.TerraformerType;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockGenerator;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.jei.categories.*;
import fr.iglee42.auxiliautilities.jei.categories.GeneratorCategory.GeneratorWrapper;
import fr.iglee42.auxiliautilities.jei.subtypes.*;
import fr.iglee42.auxiliautilities.recipes.CrusherRecipe;
import fr.iglee42.auxiliautilities.recipes.EnchanterRecipe;
import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SimpleFluidIngredient;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.*;
import java.util.function.Function;

import static fr.iglee42.auxiliautilities.config.AUConfig.DISENCHANTMENT_RATE;

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
        registration.addRecipeCategories(new CrusherCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new TerraformerCategory(registration.getJeiHelpers().getGuiHelper()));
        for (Generators gen : Generators.values()) {
            registration.addRecipeCategories(new GeneratorCategory(registration.getJeiHelpers().getGuiHelper(), gen.block));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ResonatorCategory.RECIPE_TYPE,AUBlocks.RESONATOR);
        registration.addCraftingStation(EnchanterCategory.RECIPE_TYPE,AUBlocks.ENCHANTER);
        registration.addCraftingStation(RecipeTypes.SMELTING,AUBlocks.FURNACE);
        registration.addCraftingStation(CrusherCategory.RECIPE_TYPE,AUBlocks.CRUSHER);
        registration.addCraftingStation(TerraformerCategory.RECIPE_TYPE,AUBlocks.TERRAFORMER);
        for (Generators gen : Generators.values()) {
            registration.addCraftingStation(GeneratorCategory.getGeneratorRecipeType(gen.block),gen.block.get());
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        /*registration.addRecipes(ResonatorCategory.RECIPE_TYPE,
                new ArrayList<>(Minecraft.getInstance().level.recipeAccess().stonecutterRecipes().getAllRecipesFor(ResonatorRecipe.Type.INSTANCE)));
        registration.addRecipes(EnchanterCategory.RECIPE_TYPE,
                new ArrayList<>(Minecraft.getInstance().level.recipeAccess().getAllRecipesFor(EnchanterRecipe.Type.INSTANCE)));
        registration.addRecipes(CrusherCategory.RECIPE_TYPE,
                new ArrayList<>(Minecraft.getInstance().level.recipeAccess().getAllRecipesFor(CrusherRecipe.Type.INSTANCE)));*/

        List<TerraformerCategory.ExtensionWrapper> recipes = new ArrayList<>();
        for (TerraformerType type : TerraformerType.values()) {
            registration.getIngredientManager().getAllItemStacks().stream()
                    .map((stack) -> {
                        Holder<Item> itemHolder = stack.getItemHolder();
                       AUTerraformerDataMaps.TerraformerItem data = itemHolder.getData(type.getDataMapType());
                        if (data != null) {
                            return new TerraformerCategory.ExtensionWrapper(Ingredient.of(stack.getItem()),data.energyProvided(),type);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(TerraformerCategory.ExtensionWrapper::tfEnergy))
                    .forEach(recipes::add);
        }
        registration.addRecipes(TerraformerCategory.RECIPE_TYPE, recipes);
        for (Generators gen : Generators.values()) {
            registration.addRecipes(GeneratorCategory.getGeneratorRecipeType(gen.block), gen.recipes.apply(registration.getIngredientManager()));
        }
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(
                AUContainerScreen.class,
                new AUContainerScreenHandler(registration.getJeiHelpers())
        );
        registration.addGhostIngredientHandler(AUContainerScreen.class, new AUContainerScreenHandler(registration.getJeiHelpers()));
    }


    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(AUItems.SUN_CRYSTAL.asItem(), DamageItemSubtype.INSTANCE);
        registration.registerSubtypeInterpreter(AUItems.WATERING_CAN.asItem(), DamageItemSubtype.INSTANCE);
        registration.registerSubtypeInterpreter(AUItems.LUX_SABER.asItem(), LuxSaberSubtype.INSTANCE);
        registration.registerSubtypeInterpreter(AUItems.BIOME_MARKER.asItem(), BiomeMarkerSubtype.INSTANCE);
        registration.registerSubtypeInterpreter(AUItems.ANGEL_RING.asItem(), AngelRingSubtype.INSTANCE);
        registration.registerSubtypeInterpreter(AUItems.GOLDEN_LASSO.asItem(), LassoSubtype.INSTANCE);
        registration.registerSubtypeInterpreter(AUItems.CURSED_LASSO.asItem(), LassoSubtype.INSTANCE);
    }

    enum Generators {
        SURVIVAL(AUBlocks.SURVIVAL_GENERATOR,
                manager -> manager.getAllItemStacks().stream()
                        .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                            int burnTime = stack.getBurnTime(null,Minecraft.getInstance().level.fuelValues());
                            if (burnTime > 0) {
                                consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, 5, burnTime * 10));
                            }
                        })
                        .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                        .toList()),
        FURNACE(AUBlocks.FURNACE_GENERATOR,
                manager -> manager.getAllItemStacks().stream()
                        .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                            int burnTime = stack.getBurnTime(null,Minecraft.getInstance().level.fuelValues());
                            if (burnTime > 0) {
                                consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, 40, burnTime / 10));
                            }
                        })
                        .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                        .toList()),
        OVERCLOCKED(AUBlocks.OVERCLOCKED_GENERATOR,
                manager -> manager.getAllItemStacks().stream()
                        .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                            int burnTime = stack.getBurnTime(null,Minecraft.getInstance().level.fuelValues());
                            if (burnTime > 0) {
                                consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, burnTime, 1));
                            }
                        })
                        .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                        .toList()),
        CULINARY(AUBlocks.CULINARY_GENERATOR,
                manager -> manager.getAllItemStacks().stream()
                        .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                            if (stack.has(DataComponents.FOOD)) {
                                consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, (int) BEGenCulinary.getEnergyRate(stack), (int) (BEGenCulinary.getEnergyRate(stack) * 10)));
                            }
                        })
                        .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                        .toList()),
        MAGMATIC(AUBlocks.MAGMATIC_GENERATOR,
                new GeneratorWrapper(List.of(), SimpleFluidIngredient.of(BuiltInRegistries.FLUID.getOrThrow(Tags.Fluids.LAVA)), 40, 125)),
        REDSTONE_HEATED(AUBlocks.HEATED_REDSTONE_GENERATOR,
                new GeneratorWrapper(Tags.Items.DUSTS_REDSTONE, Tags.Fluids.LAVA, 160, 125)),
        SLIMEY(AUBlocks.SLIMEY_GENERATOR,
                new GeneratorWrapper(List.of(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(Tags.Items.SLIME_BALLS)), Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(Tags.Items.BUCKETS_MILK))), null, 400, 480)),
        DEATH(AUBlocks.DEATH_GENERATOR, dataMap(AUGeneratorsDataMaps.DEATH_ITEMS)),
        PINK(AUBlocks.PINK_GENERATOR,
                new GeneratorWrapper(Tags.Items.DYES_PINK, 40, 10),
                new GeneratorWrapper(Tags.Items.DYED_PINK, 40, 10)),
        POTION(AUBlocks.POTION_GENERATOR,
                manager -> manager.getAllItemStacks().stream()
                        .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                            if (stack.has(DataComponents.POTION_CONTENTS)) {
                                consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, BEGenPotion.getPotionEnergy(stack).getFirst(), BEGenPotion.getPotionEnergy(stack).getSecond()));
                            }
                        })
                        .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                        .toList()),
        DISENCHANTMENT(AUBlocks.DISENCHANTMENT_GENERATOR,
                manager -> manager.getAllItemStacks().stream()
                        .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                            if (BEGenDisenchantment.getBookEnergy(stack) > 0) {
                                consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, DISENCHANTMENT_RATE.get(), BEGenDisenchantment.getBookEnergy(stack) / DISENCHANTMENT_RATE.get()));
                            }
                        })
                        .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                        .toList()),
        ENDER(AUBlocks.ENDER_GENERATOR,dataMap(AUGeneratorsDataMaps.ENDER_ITEMS)),
        EXPLOSIVE(AUBlocks.EXPLOSIVE_GENERATOR,dataMap(AUGeneratorsDataMaps.EXPLOSIVE_ITEMS)),
        FROSTY(AUBlocks.FROSTY_GENERATOR,dataMap(AUGeneratorsDataMaps.FROSTY_ITEMS)),
        HALITOSIS(AUBlocks.HALITOSIS_GENERATOR,dataMap(AUGeneratorsDataMaps.HALITOSIS_ITEMS)),
        NETHER_STAR(AUBlocks.NETHER_STAR_GENERATOR,dataMap(AUGeneratorsDataMaps.NETHER_STAR_ITEMS));
        private final DeferredBlock<BlockGenerator> block;
        private final Function<IIngredientManager, List<GeneratorWrapper>> recipes;

        Generators(DeferredBlock<BlockGenerator> block, GeneratorWrapper... recipes) {
            this(block, manager -> Arrays.stream(recipes).toList());
        }

        Generators(DeferredBlock<BlockGenerator> block, Function<IIngredientManager, List<GeneratorWrapper>> recipes) {
            this.block = block;
            this.recipes = recipes;
        }

        private static <T extends AUGeneratorsDataMaps.MapItem> Function<IIngredientManager, List<GeneratorWrapper>> dataMap(DataMapType<Item,T> mapType){
            return manager -> manager.getAllItemStacks().stream()
                    .<GeneratorWrapper>mapMulti((stack, consumer) -> {
                        Holder<Item> itemHolder = stack.getItemHolder();
                        T data = itemHolder.getData(mapType);
                        if (data != null) {
                            consumer.accept(new GeneratorWrapper(List.of(Ingredient.of(stack.getItem())), null, data.energyPerTick(), data.time()));
                        }
                    })
                    .sorted(Comparator.comparingInt(GeneratorWrapper::time))
                    .toList();
        }
    }
}

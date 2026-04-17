package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockOpiniumCore;
import fr.iglee42.auxiliautilities.blocks.CompressedBlockSet;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.datagen.builders.CrusherRecipeBuilder;
import fr.iglee42.auxiliautilities.datagen.builders.EnchanterRecipeBuilder;
import fr.iglee42.auxiliautilities.datagen.builders.ResonatorRecipeBuilder;
import fr.iglee42.auxiliautilities.items.ItemAngelRing;
import fr.iglee42.auxiliautilities.items.ItemLasso;
import fr.iglee42.auxiliautilities.items.ItemLuxSaber;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.recipes.ingredients.LassoIngredient;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class AURecipesProvider extends RecipeProvider {
    public AURecipesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        addCrusherRecipes(output);
        addEnchanterRecipes(output);
        addResonatorRecipes(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.GLASS_CUTTER)
                .pattern("  I")
                .pattern(" SI")
                .pattern("I  ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.ENDER_SHARD.get(), 8)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(AUItems.GLASS_CUTTER)
                .unlockedBy("has_item", has(AUItems.GLASS_CUTTER))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL)
                .requires(AUItems.ENDER_SHARD.get(), 8)
                .unlockedBy("has_item", has(AUItems.ENDER_SHARD))
                .save(output, AuxiliaUtilities.id("ender_pearl_from_shards"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.WATERING_CAN)
                .pattern("S  ")
                .pattern("SBS")
                .pattern(" S ")
                .define('B', Items.BOWL)
                .define('S', Tags.Items.STONES)
                .unlockedBy("has_item", has(Items.BOWL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.UNSTABLE_INGOT)
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', AUItems.DIVISION_SIGIL)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(AUItems.DIVISION_SIGIL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.STABLE_UNSTABLE_INGOT)
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', AUItems.PSEUDO_INVERSION_SIGIL)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(AUItems.DIVISION_SIGIL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.STABLE_UNSTABLE_INGOT)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', AUItems.STABLE_UNSTABLE_NUGGET.get())
                .unlockedBy("has_item", has(AUItems.STABLE_UNSTABLE_NUGGET))
                .save(output, AuxiliaUtilities.id("stable_unstable_ingot_from_nuggets"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.STABLE_UNSTABLE_NUGGET)
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Tags.Items.NUGGETS_IRON)
                .define('S', AUTags.Items.DIVISION_SIGILS)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.STABLE_UNSTABLE_NUGGET, 9)
                .requires(AUItems.STABLE_UNSTABLE_INGOT)
                .unlockedBy("has_item", has(AUItems.STABLE_UNSTABLE_INGOT))
                .save(output, AuxiliaUtilities.id("stable_unstable_nugget_from_ingot"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.ANGEL_BLOCK.get())
                .pattern(" G ")
                .pattern("WOW")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('W', Items.FEATHER)
                .define('O', Tags.Items.OBSIDIANS_NORMAL)
                .unlockedBy("has_item", has(Items.FEATHER))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.SUN_CRYSTAL.get())
                .requires(Tags.Items.GEMS_DIAMOND)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        for (DyeColor color : DyeColor.values()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemLuxSaber.getStack(color, false))
                    .pattern("DGD")
                    .pattern("DCD")
                    .pattern("DRD")
                    .define('D', AUTags.Items.EVIL_INFUSED_IRON_INGOTS)
                    .define('C', DataComponentIngredient.of(true, DataComponents.DAMAGE, 0, AUItems.SUN_CRYSTAL.get()))
                    .define('G', BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getName() + "_stained_glass")))
                    .define('R', AUItems.RESONATING_REDSTONE_CRYSTAL)
                    .unlockedBy("has_item", has(AUItems.SUN_CRYSTAL.get()))
                    .save(output, AuxiliaUtilities.id("lux_saber_" + color.getName()));
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.REDSTONE_GEAR)
                .pattern(" T ")
                .pattern("TPT")
                .pattern(" T ")
                .define('P', ItemTags.PLANKS)
                .define('T', Items.REDSTONE_TORCH)
                .unlockedBy("has_item", has(Items.REDSTONE_TORCH))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.EYE_OF_REDSTONE)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL))
                .save(output);

        ingotSet(output, AUItems.DEMON_NUGGET, AUItems.DEMON_INGOT, AUBlocks.DEMON_BLOCK);
        ingotSet(output, AUItems.ENCHANTED_NUGGET, AUItems.ENCHANTED_INGOT, AUBlocks.ENCHANTED_BLOCK);
        ingotSet(output, AUItems.EVIL_INFUSED_IRON_NUGGET, AUItems.EVIL_INFUSED_IRON_INGOT, AUBlocks.EVIL_INFUSED_IRON_BLOCK);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.MOON_STONE)
                .pattern("LLL")
                .pattern("LDL")
                .pattern("LLL")
                .define('L', AUItems.LUNAR_REACTIVE_DUST)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(AUItems.LUNAR_REACTIVE_DUST.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.MOON_STONE, 9)
                .pattern("LLL")
                .pattern("LDL")
                .pattern("LLL")
                .define('L', AUItems.LUNAR_REACTIVE_DUST)
                .define('D', AUTags.Items.UNSTABLE_INGOTS)
                .unlockedBy("has_item", has(AUItems.LUNAR_REACTIVE_DUST.get()))
                .save(output, AuxiliaUtilities.id("moon_stone_from_unstable_ingot"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.RESONATING_REDSTONE_CRYSTAL.get())
                .requires(AUItems.ENDER_SHARD)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_item", has(AUItems.ENDER_SHARD.get()))
                .save(output);

        sickle(output, AUItems.WOODEN_SICKLE, ItemTags.PLANKS);
        sickle(output, AUItems.STONE_SICKLE, ItemTags.STONE_TOOL_MATERIALS);
        sickle(output, AUItems.IRON_SICKLE, Tags.Items.INGOTS_IRON);
        sickle(output, AUItems.GOLDEN_SICKLE, Tags.Items.INGOTS_GOLD);
        sickle(output, AUItems.DIAMOND_SICKLE, Tags.Items.GEMS_DIAMOND);
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(AUItems.DIAMOND_SICKLE.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                AUItems.NETHERITE_SICKLE.get()
        ).unlocks("has_item", has(Items.NETHERITE_INGOT)).save(output, AuxiliaUtilities.id("netherite_sickle_smithing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.MANUAL_MILL)
                .pattern(" G ")
                .pattern("SCS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('S', AUBlocks.POLISHED_STONE.getBlock())
                .unlockedBy("has_item", has(AUItems.REDSTONE_GEAR.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUItems.SPEED_UPGRADE)
                .requires(AUItems.UPGRADE_BASE)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy("has_item", has(AUItems.UPGRADE_BASE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.RESONATOR)
                .pattern("RCR")
                .pattern("IAI")
                .pattern("III")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.STORAGE_BLOCKS_COAL)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('A', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.SOLAR_PANEL)
                .pattern("LLL")
                .pattern("SCS")
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('S', AUBlocks.POLISHED_STONE.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.LUNAR_PANEL)
                .pattern("LLL")
                .pattern("SCS")
                .define('L', AUItems.LUNAR_REACTIVE_DUST)
                .define('S', AUBlocks.POLISHED_STONE.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.LAVA_MILL)
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SGS")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', AUBlocks.STONEBURNT.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.WATER_MILL)
                .pattern("SSS")
                .pattern("GCG")
                .pattern("SSS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S', AUBlocks.STONEBURNT.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.WIND_MILL)
                .pattern("SSS")
                .pattern(" GC")
                .pattern("SSS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S', AUBlocks.STONEBURNT.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.FIRE_MILL)
                .pattern("SCS")
                .pattern("SGS")
                .pattern("SFS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S', AUBlocks.STONEBURNT.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('F', Items.NETHER_BRICK_FENCE)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.DRAGON_EGG_MILL)
                .pattern("SGS")
                .pattern("PCP")
                .pattern("SFS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S', AUBlocks.STONEBURNT.getBlock())
                .define('C', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('F', AUItems.EYE_OF_REDSTONE)
                .define('P', Items.NETHER_STAR)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        for (DecorativeBlockSet set : new DecorativeBlockSet[]{AUBlocks.POLISHED_STONE, AUBlocks.BORDER_STONE, AUBlocks.CROSSED_STONE}) {
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getBlock(), Blocks.STONE_BRICKS);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getStairs(), Blocks.STONE_BRICKS);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getSlab(), Blocks.STONE_BRICKS, 2);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getWall(), Blocks.STONE_BRICKS);

            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getBlock(), Blocks.STONE);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getStairs(), Blocks.STONE);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getSlab(), Blocks.STONE, 2);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getWall(), Blocks.STONE);
        }

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, AUBlocks.CROSSED_STONE.getBlock(), AUBlocks.POLISHED_STONE.getBlock());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, AUBlocks.CROSSED_STONE.getStairs(), AUBlocks.POLISHED_STONE.getBlock());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, AUBlocks.CROSSED_STONE.getSlab(), AUBlocks.POLISHED_STONE.getBlock(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, AUBlocks.CROSSED_STONE.getWall(), AUBlocks.POLISHED_STONE.getBlock());

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS) {
            stairBuilder(set.getStairs().get(), Ingredient.of(set.getBlock().get()))
                    .unlockedBy("has_item", has(set.getBlock().get()))
                    .save(output);
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getStairs().get(), set.getBlock().get());
            slab(output, RecipeCategory.BUILDING_BLOCKS, set.getSlab().get(), set.getBlock().get());
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getSlab().get(), set.getBlock().get(), 2);
            wall(output, RecipeCategory.BUILDING_BLOCKS, set.getWall().get(), set.getBlock().get());
            stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, set.getWall().get(), set.getBlock().get(), 2);
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AUBlocks.DIAGONAL_WOOD, 5)
                .pattern("SP")
                .pattern("PS")
                .define('S', AUTags.Items.WOODEN_STAIRS)
                .define('P', ItemTags.PLANKS)
                .unlockedBy("has_item", has(ItemTags.PLANKS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, AUBlocks.MAGICAL_WOOD)
                .requires(Items.BOOKSHELF)
                .requires(Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_item", has(Items.BOOKSHELF))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, AUBlocks.MAGICAL_PLANKS, 4)
                .requires(AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.BUILDERS_WAND)
                .pattern("  G")
                .pattern(" P ")
                .pattern("P  ")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('P', AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.DESTRUCTION_WAND)
                .pattern(" GG")
                .pattern(" PG")
                .pattern("P  ")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('P', AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.SOUND_MUFFLER)
                .requires(ItemTags.WOOL)
                .requires(Items.NOTE_BLOCK)
                .unlockedBy("has_item", has(Items.NOTE_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.ENCHANTER)
                .pattern(" B ")
                .pattern("DMD")
                .pattern("III")
                .define('B', Items.ENCHANTED_BOOK)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('M', AUBlocks.MACHINE_BLOCK)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_item", has(Items.ENCHANTED_BOOK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.MACHINE_BLOCK, 4)
                .pattern("IRI")
                .pattern("RCR")
                .pattern("IRI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.CHESTS_WOODEN)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.FURNACE)
                .pattern("III")
                .pattern("ICI")
                .pattern("III")
                .define('I', Tags.Items.BRICKS_NORMAL)
                .define('C', AUBlocks.MACHINE_BLOCK)
                .unlockedBy("has_item", has(AUBlocks.MACHINE_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.CRUSHER)
                .pattern("IPI")
                .pattern("ICI")
                .pattern("IPI")
                .define('I', Tags.Items.BRICKS_NORMAL)
                .define('C', AUBlocks.MACHINE_BLOCK)
                .define('P', Ingredient.of(Items.PISTON, Items.STICKY_PISTON))
                .unlockedBy("has_item", has(AUBlocks.MACHINE_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, AUItems.MAGICAL_APPLE, 8)
                .pattern("III")
                .pattern("ICI")
                .pattern("III")
                .define('I', Items.APPLE)
                .define('C', AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.ENCHANTED_SPEED_UPGRADE)
                .pattern("AIA")
                .pattern("ICI")
                .pattern("AIA")
                .define('I', AUTags.Items.ENCHANTED_INGOTS)
                .define('C', AUItems.SPEED_UPGRADE)
                .define('A', AUItems.MAGICAL_APPLE)
                .unlockedBy("has_item", has(AUTags.Items.ENCHANTED_INGOTS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.ULTIMATE_SPEED_UPGRADE)
                .pattern("AIA")
                .pattern("ICI")
                .pattern("AIA")
                .define('I', AUTags.Items.EVIL_INFUSED_IRON_INGOTS)
                .define('C', AUItems.ENCHANTED_SPEED_UPGRADE)
                .define('A', AUItems.DROP_OF_EVIL)
                .unlockedBy("has_item", has(AUTags.Items.EVIL_INFUSED_IRON_INGOTS))
                .save(output);

        for (CompressedBlockSet set : CompressedBlockSet.ALL_SETS) {
            for (int i = 1; i <= set.getMaxTier(); i++) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, i == 1 ? set.getBaseBlock() : set.getBlock(i - 1), 9)
                        .requires(set.getBlock(i))
                        .unlockedBy("has_item", has(set.getBaseBlock()))
                        .save(output, AuxiliaUtilities.id("compressed/" + set.getName() + "/" + i + "_uncompress"));
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, set.getBlock(i))
                        .pattern("CCC")
                        .pattern("CCC")
                        .pattern("CCC")
                        .define('C', i == 1 ? set.getBaseBlock() : set.getBlock(i - 1))
                        .unlockedBy("has_item", has(set.getBaseBlock()))
                        .save(output, AuxiliaUtilities.id("compressed/" + set.getName() + "/" + i));
            }
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AUBlocks.REDSTONE_CLOCK)
                .pattern("SRS")
                .pattern("RTR")
                .pattern("SRS")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('T', Items.REDSTONE_TORCH)
                .define('S', Tags.Items.STONES)
                .unlockedBy("has_item", has(Items.REDSTONE_TORCH))
                .save(output);

        createGeneratorRecipes(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.MISERABLE_OPINIUM_CORE)
                .pattern(" C ")
                .pattern("CIC")
                .pattern(" C ")
                .define('C', BlockOpiniumCore.Tier.MISERABLE.getOrbit())
                .define('I', BlockOpiniumCore.Tier.MISERABLE.getMain())
                .unlockedBy("has_item", has(BlockOpiniumCore.Tier.MISERABLE.getMain()))
                .save(output);

        for (int i = 1; i < BlockOpiniumCore.Tier.values().length; i++) {
            BlockOpiniumCore.Tier tier = BlockOpiniumCore.Tier.values()[i];
            BlockOpiniumCore.Tier previous = BlockOpiniumCore.Tier.values()[i - 1];
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.OPINIUM_CORE.get(tier))
                    .pattern(" O ")
                    .pattern("CIC")
                    .pattern(" O ")
                    .define('C', tier.getOrbit())
                    .define('I', tier.getMain())
                    .define('O', AUBlocks.OPINIUM_CORE.get(previous))
                    .unlockedBy("has_item", has(AUBlocks.OPINIUM_CORE.get(previous)))
                    .save(output);
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.BIOME_MARKER)
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', AUItems.LUNAR_REACTIVE_DUST)
                .define('B', Tags.Items.INGOTS_IRON)
                .define('C', ItemTags.SAPLINGS)
                .unlockedBy("has_item", has(AUItems.LUNAR_REACTIVE_DUST))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.COMPOUND_BOW)
                .pattern(" OS")
                .pattern("I S")
                .pattern(" OS")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Tags.Items.STRINGS)
                .define('O', AUBlocks.PERFECTED_OPINIUM_CORE)
                .unlockedBy("has_item", has(AUBlocks.PERFECTED_OPINIUM_CORE))
                .save(output);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AUItems.KIKOKU)
                .pattern("O")
                .pattern("O")
                .pattern("S")
                .define('O', AUBlocks.PERFECTED_OPINIUM_CORE)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(AUBlocks.PERFECTED_OPINIUM_CORE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.ITEM_FILTER)
                .pattern("RTR")
                .pattern("TST")
                .pattern("RTR")
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('T', Tags.Items.RODS_WOODEN)
                .define('S', Tags.Items.STRINGS)
                .unlockedBy("has_item", has(Tags.Items.STRINGS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.FLUID_FILTER)
                .pattern("RTR")
                .pattern("TST")
                .pattern("RTR")
                .define('R', Tags.Items.GEMS_LAPIS)
                .define('T', Tags.Items.RODS_WOODEN)
                .define('S', Tags.Items.STRINGS)
                .unlockedBy("has_item", has(Tags.Items.STRINGS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.KLEIN_BOTTLE)
                .requires(Items.GLASS_BOTTLE)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Tags.Items.ENDER_PEARLS)
                .unlockedBy("has_item", has(Items.GLASS_BOTTLE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.STONE_DRUM)
                .pattern("FSF")
                .pattern("FBF")
                .pattern("FSF")
                .define('F', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('S', Ingredient.of(Items.COBBLESTONE_SLAB, Items.STONE_SLAB))
                .define('B', Items.BOWL)
                .unlockedBy("has_item", has(Items.BOWL))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.STONE_DRUM)
                .requires(AUBlocks.STONE_DRUM)
                .unlockedBy("has_item", has(AUBlocks.STONE_DRUM))
                .save(output, AuxiliaUtilities.id("stone_drum_clear"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.IRON_DRUM)
                .pattern("FSF")
                .pattern("FBF")
                .pattern("FSF")
                .define('F', Tags.Items.INGOTS_IRON)
                .define('S', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .define('B', Items.CAULDRON)
                .unlockedBy("has_item", has(Items.CAULDRON))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.IRON_DRUM)
                .requires(AUBlocks.IRON_DRUM)
                .unlockedBy("has_item", has(AUBlocks.IRON_DRUM))
                .save(output, AuxiliaUtilities.id("iron_drum_clear"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.REINFORCED_LARGE_DRUM)
                .pattern("FSF")
                .pattern("FBF")
                .pattern("FSF")
                .define('F', Tags.Items.GEMS_DIAMOND)
                .define('S', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .define('B', AUBlocks.IRON_DRUM)
                .unlockedBy("has_item", has(AUBlocks.IRON_DRUM))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.REINFORCED_LARGE_DRUM)
                .requires(AUBlocks.REINFORCED_LARGE_DRUM)
                .unlockedBy("has_item", has(AUBlocks.REINFORCED_LARGE_DRUM))
                .save(output, AuxiliaUtilities.id("reinforced_large_drum_clear"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.DEMONICALLY_GARGANTUAN_DRUM)
                .pattern("FSF")
                .pattern("FBF")
                .pattern("FSF")
                .define('F', AUItems.DEMON_INGOT)
                .define('S', AUBlocks.KLEIN_BOTTLE)
                .define('B', AUBlocks.REINFORCED_LARGE_DRUM)
                .unlockedBy("has_item", has(AUBlocks.REINFORCED_LARGE_DRUM))
                .save(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.DEMONICALLY_GARGANTUAN_DRUM)
                .requires(AUBlocks.DEMONICALLY_GARGANTUAN_DRUM)
                .unlockedBy("has_item", has(AUBlocks.DEMONICALLY_GARGANTUAN_DRUM))
                .save(output, AuxiliaUtilities.id("demonically_gargantuan_drum_clear"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.ENDER_PORCUPINE)
                .pattern("RTR")
                .pattern("TST")
                .pattern("RTR")
                .define('S', AUItems.POWER_MANAGER)
                .define('T', Tags.Items.ENDER_PEARLS)
                .define('R', Tags.Items.STRINGS)
                .unlockedBy("has_item", has(AUItems.POWER_MANAGER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.POWER_MANAGER)
                .pattern(" R")
                .pattern("SS")
                .pattern("SS")
                .define('S', Items.STONE)
                .define('R', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.SANDY_GLASS, 4)
                .requires(Tags.Items.SANDS)
                .requires(Tags.Items.SANDS)
                .requires(Blocks.GLASS)
                .requires(Blocks.GLASS)
                .unlockedBy("has_item", has(Blocks.GLASS))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(AUBlocks.SANDY_GLASS),
                RecipeCategory.MISC,
                AUBlocks.THICKENED_GLASS,
                0.5f,
                200
        ).unlockedBy("has_item", has(AUBlocks.SANDY_GLASS)).save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.THICKENED_GLASS_BORDERED, 4)
                .pattern("GG")
                .pattern("GG")
                .define('G', AUBlocks.THICKENED_GLASS)
                .unlockedBy("has_item", has(AUBlocks.THICKENED_GLASS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.THICKENED_GLASS_PATTERNED, 4)
                .pattern("GG")
                .pattern("GG")
                .define('G', AUBlocks.THICKENED_GLASS_BORDERED)
                .unlockedBy("has_item", has(AUBlocks.THICKENED_GLASS_BORDERED))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.DARK_GLASS, 2)
                .requires(AUBlocks.THICKENED_GLASS, 2)
                .requires(Tags.Items.DYES_BLACK)
                .unlockedBy("has_item", has(AUBlocks.THICKENED_GLASS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.GLOWING_GLASS, 2)
                .requires(AUBlocks.THICKENED_GLASS, 2)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy("has_item", has(AUBlocks.THICKENED_GLASS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.OBSIDIAN_GLASS, 2)
                .requires(AUBlocks.THICKENED_GLASS, 2)
                .requires(Tags.Items.OBSIDIANS)
                .unlockedBy("has_item", has(AUBlocks.THICKENED_GLASS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.ETHEREAL_GLASS, 8)
                .pattern("GGG")
                .pattern("GMG")
                .pattern("GGG")
                .define('G', Tags.Items.GLASS_BLOCKS_CHEAP)
                .define('M', AUItems.MOON_STONE)
                .unlockedBy("has_item", has(AUItems.MOON_STONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.INEFFABLE_GLASS, 8)
                .pattern("GGG")
                .pattern("GMG")
                .pattern("GGG")
                .define('G', AUBlocks.THICKENED_GLASS)
                .define('M', AUItems.MOON_STONE)
                .unlockedBy("has_item", has(AUItems.MOON_STONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.DARK_INEFFABLE_GLASS, 8)
                .pattern("GGG")
                .pattern("GMG")
                .pattern("GGG")
                .define('G', AUBlocks.DARK_GLASS)
                .define('M', AUItems.MOON_STONE)
                .unlockedBy("has_item", has(AUItems.MOON_STONE))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.REVERSE_ETHEREAL_GLASS)
                .requires(AUBlocks.ETHEREAL_GLASS)
                .requires(Items.REDSTONE_TORCH)
                .unlockedBy("has_item", has(AUBlocks.ETHEREAL_GLASS))
                .save(output);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.WOODEN_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', Items.WOODEN_SWORD)
                .define('P', ItemTags.PLANKS)
                .define('L', ItemTags.LOGS)
                .unlockedBy("has_item", has(Items.WOODEN_SWORD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.STONE_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', Items.STONE_SWORD)
                .define('P', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('L', AUBlocks.COMPRESSED_COBBLESTONE.getBlock(1))
                .unlockedBy("has_item", has(Items.STONE_SWORD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.COPPER_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', AUBlocks.STONE_SPIKE)
                .define('P', Tags.Items.INGOTS_COPPER)
                .define('L', Tags.Items.STORAGE_BLOCKS_COPPER)
                .unlockedBy("has_item", has(AUBlocks.STONE_SPIKE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.IRON_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', Items.IRON_SWORD)
                .define('P', Tags.Items.INGOTS_IRON)
                .define('L', Tags.Items.STORAGE_BLOCKS_IRON)
                .unlockedBy("has_item", has(Items.IRON_SWORD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.GOLDEN_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', Items.GOLDEN_SWORD)
                .define('P', Tags.Items.INGOTS_GOLD)
                .define('L', Tags.Items.STORAGE_BLOCKS_GOLD)
                .unlockedBy("has_item", has(Items.GOLDEN_SWORD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.DIAMOND_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', Items.DIAMOND_SWORD)
                .define('P', Tags.Items.GEMS_DIAMOND)
                .define('L', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .unlockedBy("has_item", has(Items.DIAMOND_SWORD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.NETHERITE_SPIKE, 4)
                .pattern(" S ")
                .pattern("SPS")
                .pattern("PLP")
                .define('S', Items.NETHERITE_SWORD)
                .define('P', Tags.Items.INGOTS_NETHERITE)
                .define('L', Tags.Items.STORAGE_BLOCKS_NETHERITE)
                .unlockedBy("has_item", has(Items.NETHERITE_SWORD))
                .save(output);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.CLIMOGRAPH_BLOCK)
                .pattern("IDI")
                .pattern("SMS")
                .pattern("IDI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('S', ItemTags.SAPLINGS)
                .define('M', AUBlocks.MACHINE_BLOCK)
                .unlockedBy("has_item", has(AUBlocks.MACHINE_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.TERRAFORMER)
                .pattern("SMS")
                .pattern("IDI")
                .define('I', Tags.Items.DUSTS_REDSTONE)
                .define('D', Items.COMPARATOR)
                .define('S', Tags.Items.ENDER_PEARLS)
                .define('M', AUBlocks.CLIMOGRAPH_BLOCK)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.ANTENNA)
                .pattern("R R")
                .pattern("ISI")
                .pattern(" I ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Items.END_ROD)
                .define('S', ItemTags.SAPLINGS)
                .unlockedBy("has_item", has(Items.END_ROD))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.HUMIDIFIER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(Tags.Items.BUCKETS_WATER)
                .requires(Tags.Items.BUCKETS_WATER)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.DEHUMIDIFIER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(Tags.Items.SANDS)
                .requires(Tags.Items.SANDS)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.HEATER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(Tags.Items.BUCKETS_LAVA)
                .requires(Tags.Items.BUCKETS_LAVA)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.COOLER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(Items.SNOWBALL)
                .requires(Items.SNOWBALL)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.DESHOSTILIFIER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(Items.MYCELIUM)
                .requires(Items.MYCELIUM)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.MAGIC_ABSORBER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(ItemTags.ANVIL)
                .requires(ItemTags.ANVIL)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.MAGIC_INFUSER)
                .requires(AUBlocks.CLIMOGRAPH_BLOCK)
                .requires(Items.ENCHANTING_TABLE)
                .requires(Items.ENCHANTING_TABLE)
                .unlockedBy("has_item", has(AUBlocks.CLIMOGRAPH_BLOCK))
                .save(output);

        angelRing(output, Ingredient.of(Tags.Items.GLASS_BLOCKS_CHEAP), Ingredient.of(Tags.Items.GLASS_BLOCKS_CHEAP), ItemAngelRing.AngelRingWings.NONE);
        angelRing(output, Ingredient.of(Tags.Items.FEATHERS), Ingredient.of(Tags.Items.FEATHERS), ItemAngelRing.AngelRingWings.FEATHER);
        angelRing(output, Ingredient.of(Tags.Items.DYES_PURPLE), Ingredient.of(Tags.Items.DYES_PINK), ItemAngelRing.AngelRingWings.BUTTERFLY);
        angelRing(output, Ingredient.of(Tags.Items.LEATHERS), Ingredient.of(Tags.Items.LEATHERS), ItemAngelRing.AngelRingWings.BAT);
        angelRing(output, Ingredient.of(Tags.Items.NUGGETS_GOLD), Ingredient.of(Tags.Items.NUGGETS_GOLD), ItemAngelRing.AngelRingWings.GOLDEN);
        angelRing(output, Ingredient.of(Items.COAL), Ingredient.of(Items.CHARCOAL), ItemAngelRing.AngelRingWings.DEMON);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.CHICKEN_RING)
                .pattern("FIF")
                .pattern("ICI")
                .pattern("RIR")
                .define('F', Tags.Items.FEATHERS)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('C', new LassoIngredient(true, EntityType.CHICKEN).toVanilla())
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.FLYING_SQUID_RING)
                .pattern("FDF")
                .pattern("CRP")
                .pattern("FDF")
                .define('F', Tags.Items.DYES_BLACK)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('R', AUItems.CHICKEN_RING)
                .define('P', Tags.Items.ENDER_PEARLS)
                .define('C', new LassoIngredient(true, EntityType.SQUID).toVanilla())
                .unlockedBy("has_item", has(AUItems.CHICKEN_RING))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AUItems.GOLDEN_LASSO)
                .pattern("GSG")
                .pattern("S S")
                .pattern("GSG")
                .define('G', Tags.Items.NUGGETS_GOLD)
                .define('S', Tags.Items.STRINGS)
                .unlockedBy("has_item", has(Tags.Items.NUGGETS_GOLD))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, AUItems.CURSED_LASSO)
                .requires(AUItems.GOLDEN_LASSO)
                .requires(AUItems.DROP_OF_EVIL)
                .unlockedBy("has_item", has(AUItems.GOLDEN_LASSO))
                .save(output);
    }

    private void angelRing(RecipeOutput output, Ingredient input, Ingredient input2, ItemAngelRing.AngelRingWings wings) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ItemAngelRing.getWithWings(wings))
                .requires(AUItems.ANGEL_RING)
                .requires(input)
                .requires(input2)
                .unlockedBy("has_item", has(AUItems.ANGEL_RING))
                .save(output, AuxiliaUtilities.id("angel_ring_" + wings.name().toLowerCase() + "_convert"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemAngelRing.getWithWings(wings))
                .pattern("AGB")
                .pattern("GSG")
                .pattern("LGC")
                .define('A',input)
                .define('B',input2)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', AUItems.FLYING_SQUID_RING)
                .define('L',new LassoIngredient(true,EntityType.BAT).toVanilla())
                .define('C',new LassoIngredient(false,EntityType.GHAST).toVanilla())
                .unlockedBy("has_item", has(AUItems.FLYING_SQUID_RING))
                .save(output, AuxiliaUtilities.id("angel_ring_" + wings.name().toLowerCase()));
    }

    private void createGeneratorRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.SURVIVAL_GENERATOR)
                .pattern("CCC")
                .pattern("CIC")
                .pattern("RFR")
                .define('C', ItemTags.STONE_CRAFTING_MATERIALS)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('F', Items.FURNACE)
                .unlockedBy("has_item", has(Items.FURNACE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.FURNACE_GENERATOR)
                .pattern("CCC")
                .pattern("CIC")
                .pattern("RFR")
                .define('C', Tags.Items.INGOTS_IRON)
                .define('I', AUBlocks.MACHINE_BLOCK)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('F', Items.FURNACE)
                .unlockedBy("has_item", has(AUBlocks.MACHINE_BLOCK))
                .save(output);

        List<Ingredient.Value> vegetables = new ArrayList<>(Collections.singleton(new Ingredient.ItemValue(Items.WHEAT.getDefaultInstance())));
        vegetables.addAll(Arrays.stream(Ingredient.of(Tags.Items.FOODS_VEGETABLE).getValues()).toList());

        List<Ingredient.Value> cooked = new ArrayList<>(Arrays.stream(Ingredient.of(Tags.Items.FOODS_COOKED_FISH).getValues()).toList());
        cooked.addAll(Arrays.stream(Ingredient.of(Tags.Items.FOODS_COOKED_MEAT).getValues()).toList());
        generatorRecipe(output, AUBlocks.CULINARY_GENERATOR, Ingredient.fromValues(vegetables.stream()), Ingredient.fromValues(cooked.stream()));

        generatorRecipe(output, AUBlocks.MAGMATIC_GENERATOR, Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.BUCKETS_LAVA));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUBlocks.HEATED_REDSTONE_GENERATOR)
                .pattern("RRR")
                .pattern("RIR")
                .pattern("RFR")
                .define('I', AUBlocks.MAGMATIC_GENERATOR)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('F', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy("has_item", has(AUBlocks.MAGMATIC_GENERATOR))
                .save(output);

        generatorRecipe(output, AUBlocks.ENDER_GENERATOR, Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.OBSIDIANS));
        generatorRecipe(output, AUBlocks.POTION_GENERATOR, Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Items.BREWING_STAND));
        generatorRecipe(output, AUBlocks.PINK_GENERATOR, Ingredient.of(Tags.Items.DYES_PINK), Ingredient.of(Tags.Items.DYED_PINK));
        generatorRecipe(output, AUBlocks.OVERCLOCKED_GENERATOR, Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD));
        generatorRecipe(output, AUBlocks.EXPLOSIVE_GENERATOR, Ingredient.of(Tags.Items.GUNPOWDERS), Ingredient.of(Items.TNT));
        generatorRecipe(output, AUBlocks.NETHER_STAR_GENERATOR, Ingredient.of(Items.WITHER_SKELETON_SKULL), Ingredient.of(Tags.Items.NETHER_STARS));
        generatorRecipe(output, AUBlocks.HALITOSIS_GENERATOR, Ingredient.of(Items.PURPUR_BLOCK, Items.PURPUR_PILLAR), Ingredient.of(Items.END_ROD));
        generatorRecipe(output, AUBlocks.FROSTY_GENERATOR, Ingredient.of(Items.SNOWBALL), Ingredient.of(Items.ICE));
        generatorRecipe(output, AUBlocks.DEATH_GENERATOR, Ingredient.of(Items.BONE, Items.ROTTEN_FLESH), Ingredient.of(Items.SPIDER_EYE));
        generatorRecipe(output, AUBlocks.DISENCHANTMENT_GENERATOR, Ingredient.of(AUBlocks.MAGICAL_WOOD), Ingredient.of(Items.ENCHANTING_TABLE));
        generatorRecipe(output, AUBlocks.SLIMEY_GENERATOR, Ingredient.of(Tags.Items.SLIME_BALLS), Ingredient.of(Tags.Items.STORAGE_BLOCKS_SLIME));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.RAINBOW_GENERATOR_BOTTOM)
                .pattern("ABC")
                .pattern("DRE")
                .pattern("FGH")
                .define('A', AUBlocks.NETHER_STAR_GENERATOR)
                .define('B', AUBlocks.OVERCLOCKED_GENERATOR)
                .define('C', AUBlocks.PINK_GENERATOR)
                .define('D', AUBlocks.POTION_GENERATOR)
                .define('E', AUBlocks.HEATED_REDSTONE_GENERATOR)
                .define('F', AUBlocks.SLIMEY_GENERATOR)
                .define('G', AUBlocks.SURVIVAL_GENERATOR)
                .define('H', AUBlocks.EXPLOSIVE_GENERATOR)
                .define('R', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AUItems.RAINBOW_GENERATOR_TOP)
                .pattern("ABC")
                .pattern("DRE")
                .pattern("FGH")
                .define('A', AUBlocks.CULINARY_GENERATOR)
                .define('B', AUBlocks.DEATH_GENERATOR)
                .define('C', AUBlocks.HALITOSIS_GENERATOR)
                .define('D', AUBlocks.DISENCHANTMENT_GENERATOR)
                .define('E', AUBlocks.ENDER_GENERATOR)
                .define('F', AUBlocks.FURNACE_GENERATOR)
                .define('G', AUBlocks.FROSTY_GENERATOR)
                .define('H', AUBlocks.MAGMATIC_GENERATOR)
                .define('R', AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AUBlocks.RAINBOW_GENERATOR)
                .requires(AUItems.RAINBOW_GENERATOR_BOTTOM)
                .requires(AUItems.RAINBOW_GENERATOR_TOP)
                .unlockedBy("has_bottom", has(AUItems.RAINBOW_GENERATOR_BOTTOM))
                .unlockedBy("has_top", has(AUItems.RAINBOW_GENERATOR_TOP))
                .save(output);

    }

    private void generatorRecipe(RecipeOutput output, ItemLike generator, Ingredient C, Ingredient I) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, generator)
                .pattern("CCC")
                .pattern("CIC")
                .pattern("RFR")
                .define('C', C)
                .define('I', I)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('F', AUBlocks.FURNACE_GENERATOR)
                .unlockedBy("has_item", has(AUBlocks.FURNACE_GENERATOR))
                .save(output);
    }

    private void ingotSet(RecipeOutput output, DeferredItem<?> nugget, DeferredItem<?> ingot, DeferredBlock<?> block) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', nugget.get())
                .unlockedBy("has_item", has(nugget.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget.get(), 9)
                .requires(ingot.get())
                .unlockedBy("has_item", has(ingot.get()))
                .save(output, AuxiliaUtilities.id(nugget.getId().getPath() + "_from_ingot"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ingot.get())
                .unlockedBy("has_item", has(ingot.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot.get(), 9)
                .requires(block.get())
                .unlockedBy("has_item", has(block.get()))
                .save(output, AuxiliaUtilities.id(ingot.getId().getPath() + "_from_block"));
    }

    private void sickle(RecipeOutput output, DeferredItem<?> sickle, TagKey<Item> tag) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, sickle.get())
                .pattern(" II")
                .pattern("  I")
                .pattern("SII")
                .define('I', tag)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(tag))
                .save(output);
    }

    private void addCrusherRecipes(RecipeOutput output) {
        //WOOLS
        for (DyeColor color : DyeColor.values()) {
            CrusherRecipeBuilder.crusher(output,
                    "wool/" + color.getSerializedName(),
                    BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_wool")),
                    Items.STRING,
                    3,
                    BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_dye")),
                    1,
                    0.05f,
                    4000
            );
            CrusherRecipeBuilder.crusher(output,
                    "carpet/" + color.getSerializedName(),
                    BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_carpet")),
                    Items.STRING,
                    2,
                    BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_dye")),
                    1,
                    0.05f,
                    4000
            );
        }
        //MISC
        CrusherRecipeBuilder.crusher(output, "blaze_rod", Tags.Items.RODS_BLAZE, Items.BLAZE_POWDER, 2, Items.BLAZE_POWDER, 3, 0.4f, 4000);
        CrusherRecipeBuilder.crusher(output, "bone", Tags.Items.BONES, Items.BONE_MEAL, 3, Items.BONE_MEAL, 3, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "cobblestone", Tags.Items.COBBLESTONES_NORMAL, Items.GRAVEL, Items.SAND, 0.1f, 4000);
        CrusherRecipeBuilder.crusher(output, "gravel", Tags.Items.GRAVELS, Items.SAND, Items.CLAY, 0.1f, 4000);
        CrusherRecipeBuilder.crusher(output, "ender_eye", Items.ENDER_EYE, Items.ENDER_PEARL, Items.BLAZE_POWDER, 0.5f, 4000);
        CrusherRecipeBuilder.singleRecipe(output, "ores/glowstone", Items.GLOWSTONE, Items.GLOWSTONE_DUST, 4, 4000);
        CrusherRecipeBuilder.singleRecipe(output, "ores/amethyst", Items.AMETHYST_BLOCK, Items.AMETHYST_SHARD, 4, 4000);

        //FLOWERS
        Map<Item, Item> flowers = new HashMap<>(Map.of(
                Items.DANDELION, Items.YELLOW_DYE,
                Items.POPPY, Items.RED_DYE,
                Items.BLUE_ORCHID, Items.LIGHT_BLUE_DYE,
                Items.ALLIUM, Items.MAGENTA_DYE,
                Items.AZURE_BLUET, Items.LIGHT_GRAY_DYE,
                Items.RED_TULIP, Items.RED_DYE,
                Items.ORANGE_TULIP, Items.ORANGE_DYE,
                Items.WHITE_TULIP, Items.LIGHT_GRAY_DYE,
                Items.PINK_TULIP, Items.PINK_DYE,
                Items.OXEYE_DAISY, Items.LIGHT_GRAY_DYE
        ));
        flowers.putAll(Map.of(
                Items.CORNFLOWER, Items.BLUE_DYE,
                Items.LILY_OF_THE_VALLEY, Items.WHITE_DYE,
                Items.WITHER_ROSE, Items.BLACK_DYE,
                Items.PINK_PETALS, Items.PINK_DYE,
                Items.TORCHFLOWER, Items.ORANGE_DYE,
                Items.BEETROOT, Items.RED_DYE,
                Items.BONE_MEAL, Items.WHITE_DYE,
                Items.INK_SAC, Items.BLACK_DYE,
                Items.COCOA_BEANS, Items.BROWN_DYE
        ));

        flowers.forEach((flower, dye) -> {
            CrusherRecipeBuilder.singleRecipe(output, "flower/" + BuiltInRegistries.ITEM.getKey(flower).getPath(), flower, dye, 2, 4000);
        });

        Map<Item, Item> bigFlowers = new HashMap<>(Map.of(
                Items.SUNFLOWER, Items.YELLOW_DYE,
                Items.LILAC, Items.MAGENTA_DYE,
                Items.ROSE_BUSH, Items.RED_DYE,
                Items.PEONY, Items.PINK_DYE,
                Items.PITCHER_PLANT, Items.CYAN_DYE
        ));

        bigFlowers.forEach((flower, dye) -> {
            CrusherRecipeBuilder.singleRecipe(output, "flower/" + BuiltInRegistries.ITEM.getKey(flower).getPath(), flower, dye, 4, 4000);
        });

        //ORES
        CrusherRecipeBuilder.crusher(output, "ores/coal", Tags.Items.ORES_COAL, Items.COAL, 4, Items.COAL, 4, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/copper", Tags.Items.ORES_COPPER, Items.RAW_COPPER, 2, Items.RAW_COPPER, 2, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/iron", Tags.Items.ORES_IRON, Items.RAW_IRON, 2, Items.RAW_IRON, 2, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/gold", Tags.Items.ORES_GOLD, Items.RAW_GOLD, 2, Items.RAW_GOLD, 2, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/lapis", Tags.Items.ORES_LAPIS, Items.LAPIS_LAZULI, 8, Items.LAPIS_LAZULI, 4, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/redstone", Tags.Items.ORES_REDSTONE, Items.REDSTONE, 8, Items.REDSTONE, 4, 0.5f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/quartz", Tags.Items.ORES_QUARTZ, Items.QUARTZ, 2, Items.QUARTZ, 4, 0.75f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/diamond", Tags.Items.ORES_DIAMOND, Items.DIAMOND, 2, Items.DIAMOND, 2, 0.2f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/emerald", Tags.Items.ORES_EMERALD, Items.EMERALD, 2, Items.EMERALD, 2, 0.2f, 4000);
        CrusherRecipeBuilder.crusher(output, "ores/netherite", Tags.Items.ORES_NETHERITE_SCRAP, Items.NETHERITE_SCRAP, 2, Items.NETHERITE_SCRAP, 2, 0.1f, 4000);


    }

    private void addResonatorRecipes(RecipeOutput output) {
        ResonatorRecipeBuilder.resonator(
                output,
                "stoneburnt",
                AUBlocks.POLISHED_STONE.getBlock().asItem(),
                AUBlocks.STONEBURNT.getBlock().asItem(),
                8
        );

        ResonatorRecipeBuilder.resonator(
                output,
                "red_coal",
                Ingredient.of(ItemTags.COALS),
                new ItemStack(AUItems.RED_COAL.get()),
                16
        );

        ResonatorRecipeBuilder.resonator(
                output,
                "lunar_reactive_dust",
                Ingredient.of(Tags.Items.GEMS_LAPIS),
                new ItemStack(AUItems.LUNAR_REACTIVE_DUST.get()),
                16
        );
        ResonatorRecipeBuilder.resonator(
                output,
                "quartzburnt",
                Items.QUARTZ_BLOCK,
                AUBlocks.QUARTZBURNT.getBlock().asItem(),
                8
        );

        ResonatorRecipeBuilder.resonator(
                output,
                "upgrade_base",
                Items.LIGHT_WEIGHTED_PRESSURE_PLATE,
                AUItems.UPGRADE_BASE.get(),
                8
        );

        ResonatorRecipeBuilder.resonator(
                output,
                "rainbow_stone",
                Ingredient.of(AUBlocks.STONEBURNT.getBlock().asItem()),
                new ItemStack(AUBlocks.RAINBOW_STONE.getBlock()),
                64,
                true
        );

        ResonatorRecipeBuilder.resonator(
                output,
                "redstone_glass",
                AUBlocks.THICKENED_GLASS.asItem(),
                AUBlocks.REDSTONE_GLASS.asItem(),
                1
        );

    }

    private void addEnchanterRecipes(RecipeOutput output) {
        EnchanterRecipeBuilder.enchanter(output,
                "magical_wood",
                Items.BOOKSHELF,
                1,
                AUBlocks.MAGICAL_WOOD.asItem(),
                64_000
        );

        EnchanterRecipeBuilder.enchanter(output,
                "enchanted_metal_block",
                SizedIngredient.of(Tags.Items.STORAGE_BLOCKS_GOLD, 1),
                9,
                new ItemStack(AUBlocks.ENCHANTED_BLOCK.asItem()),
                24_000
        );

        EnchanterRecipeBuilder.enchanter(output,
                "enchanted_metal_ingot",
                SizedIngredient.of(Tags.Items.INGOTS_GOLD, 1),
                1,
                new ItemStack(AUItems.ENCHANTED_INGOT.asItem()),
                8_000
        );

        EnchanterRecipeBuilder.enchanter(output,
                "evil_infused_iron_block",
                SizedIngredient.of(Tags.Items.STORAGE_BLOCKS_IRON, 8),
                SizedIngredient.of(Items.NETHER_STAR, 8),
                new ItemStack(AUBlocks.EVIL_INFUSED_IRON_BLOCK.asItem(), 8),
                192_000
        );

        EnchanterRecipeBuilder.enchanter(output,
                "evil_infused_iron_ingot",
                SizedIngredient.of(Tags.Items.INGOTS_IRON, 8),
                SizedIngredient.of(Items.NETHER_STAR, 1),
                new ItemStack(AUItems.EVIL_INFUSED_IRON_INGOT.asItem(), 8),
                64_000
        );
        EnchanterRecipeBuilder.enchanter(output,
                "magical_apple",
                SizedIngredient.of(Items.APPLE, 16),
                1,
                new ItemStack(AUItems.MAGICAL_APPLE.get(), 16),
                16_000
        );
    }
}

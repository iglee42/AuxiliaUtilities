package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.datagen.builders.ResonatorRecipeBuilder;
import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.items.ItemLuxSaber;
import fr.iglee42.auxiliautilities.tags.AUTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

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
                    .define('D', AUTags.Items.EVIL_INFUSED_IRON_INGOTS)
                    .define('C', DataComponentIngredient.of(true, DataComponents.DAMAGE,0,AUItems.SUN_CRYSTAL.get()))
                    .define('G', BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getName() + "_stained_glass")))
                    .define('R',AUItems.RESONATING_REDSTONE_CRYSTAL)
                    .unlockedBy("has_item", has(AUItems.SUN_CRYSTAL.get()))
                    .save(output, AuxiliaUtilities.id("lux_saber_" + color.getName()));
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUItems.REDSTONE_GEAR)
                .pattern(" T ")
                .pattern("TPT")
                .pattern(" T ")
                .define('P', ItemTags.PLANKS)
                .define('T', Items.REDSTONE_TORCH)
                .unlockedBy("has_item", has(Items.REDSTONE_TORCH))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,AUItems.EYE_OF_REDSTONE)
                .requires(Tags.Items.ENDER_PEARLS)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL))
                .save(output);

        ingotSet(output,AUItems.DEMON_NUGGET,AUItems.DEMON_INGOT,AUBlocks.DEMON_BLOCK);
        ingotSet(output,AUItems.ENCHANTED_NUGGET,AUItems.ENCHANTED_INGOT,AUBlocks.ENCHANTED_BLOCK);
        ingotSet(output,AUItems.EVIL_INFUSED_IRON_NUGGET,AUItems.EVIL_INFUSED_IRON_INGOT,AUBlocks.EVIL_INFUSED_IRON_BLOCK);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUItems.MOON_STONE)
                .pattern("LLL")
                .pattern("LDL")
                .pattern("LLL")
                .define('L',AUItems.LUNAR_REACTIVE_DUST)
                .define('D',Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(AUItems.LUNAR_REACTIVE_DUST.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUItems.MOON_STONE,9)
                .pattern("LLL")
                .pattern("LDL")
                .pattern("LLL")
                .define('L',AUItems.LUNAR_REACTIVE_DUST)
                .define('D',AUTags.Items.UNSTABLE_INGOTS)
                .unlockedBy("has_item", has(AUItems.LUNAR_REACTIVE_DUST.get()))
                .save(output,AuxiliaUtilities.id("moon_stone_from_unstable_ingot"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,AUItems.RESONATING_REDSTONE_CRYSTAL.get())
                .requires(AUItems.ENDER_SHARD)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .unlockedBy("has_item", has(AUItems.ENDER_SHARD.get()))
                .save(output);

        sickle(output,AUItems.WOODEN_SICKLE,ItemTags.PLANKS);
        sickle(output,AUItems.STONE_SICKLE,ItemTags.STONE_TOOL_MATERIALS);
        sickle(output,AUItems.IRON_SICKLE,Tags.Items.INGOTS_IRON);
        sickle(output,AUItems.GOLDEN_SICKLE,Tags.Items.INGOTS_GOLD);
        sickle(output,AUItems.DIAMOND_SICKLE,Tags.Items.GEMS_DIAMOND);
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(AUItems.DIAMOND_SICKLE.get()),
                Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.TOOLS,
                AUItems.NETHERITE_SICKLE.get()
        ).unlocks("has_item", has(Items.NETHERITE_INGOT)).save(output,AuxiliaUtilities.id("netherite_sickle_smithing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.MANUAL_MILL)
                .pattern(" G ")
                .pattern("SCS")
                .define('G',AUItems.REDSTONE_GEAR)
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('S',AUBlocks.POLISHED_STONE.getBlock())
                .unlockedBy("has_item", has(AUItems.REDSTONE_GEAR.get()))
                .save(output);

        ResonatorRecipeBuilder.resonator(
                output,
                AuxiliaUtilities.id("red_coal"),
                Ingredient.of(ItemTags.COALS),
                new ItemStack(AUItems.RED_COAL.get()),
                16
        );

        ResonatorRecipeBuilder.resonator(
                output,
                AuxiliaUtilities.id("lunar_reactive_dust"),
                Ingredient.of(Tags.Items.GEMS_LAPIS),
                new ItemStack(AUItems.LUNAR_REACTIVE_DUST.get()),
                16
        );
        ResonatorRecipeBuilder.resonator(
                output,
                AuxiliaUtilities.id("quartzburnt"),
                Items.QUARTZ_BLOCK,
                AUBlocks.QUARTZBURNT.getBlock().asItem(),
                8
        );

        ResonatorRecipeBuilder.resonator(
                output,
                AuxiliaUtilities.id("upgrade_base"),
                Items.LIGHT_WEIGHTED_PRESSURE_PLATE,
                AUItems.UPGRADE_BASE.get(),
                8
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,AUItems.SPEED_UPGRADE)
                .requires(AUItems.UPGRADE_BASE)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .unlockedBy("has_item", has(AUItems.UPGRADE_BASE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.RESONATOR)
                .pattern("RCR")
                .pattern("IAI")
                .pattern("III")
                .define('R',Tags.Items.DUSTS_REDSTONE)
                .define('C', Tags.Items.STORAGE_BLOCKS_COAL)
                .define('I',Tags.Items.INGOTS_IRON)
                .define('A',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.SOLAR_PANEL)
                .pattern("LLL")
                .pattern("SCS")
                .define('L',Tags.Items.GEMS_LAPIS)
                .define('S',AUBlocks.POLISHED_STONE.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.LUNAR_PANEL)
                .pattern("LLL")
                .pattern("SCS")
                .define('L',AUItems.LUNAR_REACTIVE_DUST)
                .define('S',AUBlocks.POLISHED_STONE.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.LAVA_MILL)
                .pattern("SSS")
                .pattern("SCS")
                .pattern("SGS")
                .define('G',Tags.Items.INGOTS_GOLD)
                .define('S',AUBlocks.STONEBURNT.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.WATER_MILL)
                .pattern("SSS")
                .pattern("GCG")
                .pattern("SSS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S',AUBlocks.STONEBURNT.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.WIND_MILL)
                .pattern("SSS")
                .pattern(" GC")
                .pattern("SSS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S',AUBlocks.STONEBURNT.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.FIRE_MILL)
                .pattern("SCS")
                .pattern("SGS")
                .pattern("SFS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S',AUBlocks.STONEBURNT.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('F',Items.NETHER_BRICK_FENCE)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,AUBlocks.DRAGON_EGG_MILL)
                .pattern("SGS")
                .pattern("PCP")
                .pattern("SFS")
                .define('G', AUItems.REDSTONE_GEAR)
                .define('S',AUBlocks.STONEBURNT.getBlock())
                .define('C',AUItems.RESONATING_REDSTONE_CRYSTAL)
                .define('F',AUItems.EYE_OF_REDSTONE)
                .define('P',Items.NETHER_STAR)
                .unlockedBy("has_item", has(AUItems.RESONATING_REDSTONE_CRYSTAL.get()))
                .save(output);

        ResonatorRecipeBuilder.resonator(
                output,
                AuxiliaUtilities.id("stoneburnt"),
                AUBlocks.POLISHED_STONE.getBlock().asItem(),
                AUBlocks.STONEBURNT.getBlock().asItem(),
                8
        );

        for (DecorativeBlockSet set : new DecorativeBlockSet[]{AUBlocks.POLISHED_STONE,AUBlocks.BORDER_STONE,AUBlocks.CROSSED_STONE}) {
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getBlock(), Blocks.STONE_BRICKS);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getStairs(), Blocks.STONE_BRICKS);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getSlab(), Blocks.STONE_BRICKS,2);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getWall(), Blocks.STONE_BRICKS);

            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getBlock(), Blocks.STONE);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getStairs(), Blocks.STONE);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getSlab(), Blocks.STONE,2);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getWall(), Blocks.STONE);
        }

        stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,AUBlocks.CROSSED_STONE.getBlock(), AUBlocks.POLISHED_STONE.getBlock());
        stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,AUBlocks.CROSSED_STONE.getStairs(), AUBlocks.POLISHED_STONE.getBlock());
        stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,AUBlocks.CROSSED_STONE.getSlab(), AUBlocks.POLISHED_STONE.getBlock(),2);
        stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,AUBlocks.CROSSED_STONE.getWall(), AUBlocks.POLISHED_STONE.getBlock());

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS) {
            stairBuilder(set.getStairs().get(), Ingredient.of(set.getBlock().get()))
                    .unlockedBy("has_item", has(set.getBlock().get()))
                    .save(output);
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getStairs().get(),set.getBlock().get());
            slab(output, RecipeCategory.BUILDING_BLOCKS, set.getSlab().get(), set.getBlock().get());
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getSlab().get(),set.getBlock().get(),2);
            wall(output, RecipeCategory.BUILDING_BLOCKS, set.getWall().get(), set.getBlock().get());
            stonecutterResultFromBase(output,RecipeCategory.BUILDING_BLOCKS,set.getWall().get(),set.getBlock().get(),2);
        }

       ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,AUBlocks.DIAGONAL_WOOD,5)
                .pattern("SP")
                .pattern("PS")
                .define('S',AUTags.Items.WOODEN_STAIRS)
                .define('P',ItemTags.PLANKS)
                .unlockedBy("has_item", has(ItemTags.PLANKS))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,AUBlocks.MAGICAL_WOOD)
                .requires(Items.BOOKSHELF)
                .requires(Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_item", has(Items.BOOKSHELF))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,AUBlocks.MAGICAL_PLANKS,4)
                .requires(AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,AUItems.BUILDERS_WAND)
                .pattern("  G")
                .pattern(" P ")
                .pattern("P  ")
                .define('G',Tags.Items.INGOTS_GOLD)
                .define('P',AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,AUItems.DESTRUCTION_WAND)
                .pattern(" GG")
                .pattern(" PG")
                .pattern("P  ")
                .define('G',Tags.Items.INGOTS_GOLD)
                .define('P',AUBlocks.MAGICAL_WOOD)
                .unlockedBy("has_item", has(AUBlocks.MAGICAL_WOOD.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,AUBlocks.SOUND_MUFFLER)
                .requires(ItemTags.WOOL)
                .requires(Items.NOTE_BLOCK)
                .unlockedBy("has_item", has(Items.NOTE_BLOCK))
                .save(output);
    }

    private void ingotSet(RecipeOutput output,DeferredItem<?> nugget, DeferredItem<?> ingot, DeferredBlock<?> block){
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot.get())
                    .pattern("NNN")
                    .pattern("NNN")
                    .pattern("NNN")
                    .define('N', nugget.get())
                    .unlockedBy("has_item", has(nugget.get()))
                    .save(output);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget.get(),9)
                    .requires(ingot.get())
                    .unlockedBy("has_item", has(ingot.get()))
                    .save(output,AuxiliaUtilities.id(nugget.getId().getPath()+"_from_ingot"));

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block.get())
                    .pattern("III")
                    .pattern("III")
                    .pattern("III")
                    .define('I', ingot.get())
                    .unlockedBy("has_item", has(ingot.get()))
                    .save(output);

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot.get(),9)
                    .requires(block.get())
                    .unlockedBy("has_item", has(block.get()))
                    .save(output,AuxiliaUtilities.id(ingot.getId().getPath()+"_from_block"));
    }

    private void sickle(RecipeOutput output, DeferredItem<?> sickle, TagKey<Item> tag){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, sickle.get())
                .pattern(" II")
                .pattern("  I")
                .pattern("SII")
                .define('I', tag)
                .define('S',Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(tag))
                .save(output);
    }
}

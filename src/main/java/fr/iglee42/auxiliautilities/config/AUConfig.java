package fr.iglee42.auxiliautilities.config;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

import java.util.List;

public class AUConfig {

    public static final Builder BUILDER = new Builder();
    public static final ModConfigSpec SPEC;

    // Generators
    public static final IntValue DISENCHANTMENT_RATE;
    public static final IntValue RAINBOW_RATE;
    public static final IntValue RAINBOW_RANGE;

    // Ender Porcupine
    //public static final IntValue PORCUPINE_MAX_RANGE;
    public static final IntValue PORCUPINE_TIME;

    // Terraformer
    public static final IntValue TERRAFORMER_RANGE;
    public static final IntValue TERRAFORMER_TIME;

    // Rings
    public static final IntValue CHICKEN_CONSUMPTION;
    public static final IntValue FLYING_SQUID_CONSUMPTION;
    public static final IntValue ANGEL_CONSUMPTION;
    public static final IntValue ANGEL_PASSIVE_CONSUMPTION;

    // Lux Saber
    public static final IntValue SABER_THRESHOLD;
    public static final IntValue SABER_MAX_ENERGY;

    // Unstable Ingot
    public static final IntValue UNSTABLE_TIME;

    // Division Sigil
    public static final IntValue END_SIEGE_ITEMS_CHESTS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> NORTH_CHEST_ITEMS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SOUTH_CHEST_ITEMS;
    public static final ModConfigSpec.BooleanValue DEFAULT_EAST_CHEST;
    public static final ModConfigSpec.BooleanValue DEFAULT_WEST_CHEST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> EAST_CHEST_ITEMS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WEST_CHEST_ITEMS;
    public static final IntValue END_SIEGE_KILLS;


    static {

        BUILDER.push("blocks");
        {
            BUILDER.push("generators");
            DISENCHANTMENT_RATE = BUILDER.comment("The energy rate (FE/tick) of the disenchantment generator").defineInRange("disenchantment_rate", 40, 1, Integer.MAX_VALUE);
            RAINBOW_RATE = BUILDER.comment("The energy rate (FE/tick) of the rainbow generator").defineInRange("rainbow_rate", 25_000_000, 1, Integer.MAX_VALUE);
            RAINBOW_RANGE = BUILDER.comment("The range (in blocks) in which the other generators should be placed the other generator for the rainbow generator to count them").defineInRange("rainbow_range", 8, 2, 64);
            BUILDER.pop();

            BUILDER.push("ender_porcupine");
            //PORCUPINE_MAX_RANGE = BUILDER.comment("The maximum range (in block) of the Ender Porcupine").worldRestart().defineInRange("porcupine_max_range",)
            PORCUPINE_TIME = BUILDER.comment("The time (in ticks) before the Ender Porcupine change the selected position").worldRestart().defineInRange("porcupine_time",20,1,Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("terraformer");
            TERRAFORMER_RANGE = BUILDER.comment("The maximum range (in blocks) of the Terraformer").worldRestart().defineInRange("terraformer_max_range",64,1,Integer.MAX_VALUE);
            TERRAFORMER_TIME = BUILDER.comment("The time (in ticks) which it takes for the Terraformer to change the biome of a block").worldRestart().defineInRange("terraformer_time",20,1,Integer.MAX_VALUE);
            BUILDER.pop();
        }
        BUILDER.pop();

        BUILDER.push("items");
        {
            BUILDER.push("rings");
            CHICKEN_CONSUMPTION = BUILDER.comment("The GP passive consumption of the Chicken Ring").worldRestart().defineInRange("chicken_ring_consumption", 1, 0, 256);
            FLYING_SQUID_CONSUMPTION = BUILDER.comment("The GP passive consumption of the Ring of the Flying Squid").worldRestart().defineInRange("flying_squid_ring_consumption", 16, 0, 256);
            ANGEL_CONSUMPTION = BUILDER.comment("The GP consumption of the Angel Ring when flying").worldRestart().defineInRange("angel_ring_consumption", 32, 0, 256);
            ANGEL_PASSIVE_CONSUMPTION = BUILDER.comment("The GP consumption of the Angel Ring when not flying").worldRestart().defineInRange("angel_ring_passive_consumption", 1, 0, 256);
            BUILDER.pop();

            BUILDER.push("lux_saber");
            SABER_THRESHOLD = BUILDER.comment("The Energy (in FE) required for the Lux Saber to do damage when attacking").worldRestart().defineInRange("lux_saber_threshold", 200, 0, Integer.MAX_VALUE);
            SABER_MAX_ENERGY = BUILDER.comment("The Energy (in FE) that can store a Lux Saber").worldRestart().defineInRange("lux_saber_energy", 40_000, 0, Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("unstable_ingot");
            UNSTABLE_TIME = BUILDER.comment("The Time (in ticks) before the unstable ingot explode").defineInRange("unstable_ingot_timeout", 200, 1, Integer.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("division_sigil");
            BUILDER.comment("This section includes configurations relative to the division sigil and the End Siege");
            NORTH_CHEST_ITEMS = BUILDER.comment("The items for the items in the northen chest").worldRestart().defineList("north_chest_items",List.of(
                    "minecraft:stone",
                    "minecraft:deepslate",
                    "minecraft:glass",
                    "minecraft:terracotta",
                    "minecraft:charcoal",
                    "minecraft:iron_ingot",
                    "minecraft:copper_ingot",
                    "minecraft:gold_ingot",
                    "minecraft:brick",
                    "minecraft:cooked_porkchop",
                    "minecraft:cooked_beef",
                    "minecraft:cooked_rabbit",
                    "minecraft:cooked_chicken",
                    "minecraft:cooked_mutton",
                    "minecraft:cooked_cod",
                    "minecraft:cooked_salmon",
                    "minecraft:green_dye",
                    "minecraft:nether_brick"
            ),()->"minecraft:stone",(obj)->{
                if (!(obj instanceof String str))return false;
                ResourceLocation id = ResourceLocation.tryParse(str);
                if (id == null) return false;
                return BuiltInRegistries.ITEM.containsKey(id);
            });
            SOUTH_CHEST_ITEMS = BUILDER.comment("The items for the items in the southern chest").worldRestart().defineList("south_chest_items",List.of(
                    "minecraft:grass_block",
                    "minecraft:dirt",
                    "minecraft:sand",
                    "minecraft:gravel",
                    "minecraft:clay",
                    "minecraft:coal_ore",
                    "minecraft:iron_ore",
                    "minecraft:copper_ore",
                    "minecraft:gold_ore",
                    "minecraft:diamond_ore",
                    "minecraft:emerald_ore",
                    "minecraft:redstone_ore",
                    "minecraft:lapis_ore",
                    "minecraft:deepslate_coal_ore",
                    "minecraft:deepslate_iron_ore",
                    "minecraft:deepslate_copper_ore",
                    "minecraft:deepslate_gold_ore",
                    "minecraft:deepslate_diamond_ore",
                    "minecraft:deepslate_emerald_ore",
                    "minecraft:deepslate_redstone_ore",
                    "minecraft:deepslate_lapis_ore",
                    "minecraft:nether_quartz_ore",
                    "minecraft:nether_gold_ore",
                    "minecraft:ancient_debris",
                    "minecraft:obsidian"
            ),()->"minecraft:grass_block",(obj)->{
                if (!(obj instanceof String str))return false;
                ResourceLocation id = ResourceLocation.tryParse(str);
                if (id == null) return false;
                return BuiltInRegistries.ITEM.containsKey(id);
            });

            DEFAULT_EAST_CHEST = BUILDER.comment("Should the east chest content be the default content (vanilla potions) ?").define("default_east_chest",true);
            EAST_CHEST_ITEMS = BUILDER.comment("The items for the items in the eastern chest (Used if the previous config is set to false)").worldRestart().defineList("east_chest_items",List.of(),()->"minecraft:stone",(obj)->{
                if (!(obj instanceof String str))return false;
                ResourceLocation id = ResourceLocation.tryParse(str);
                if (id == null) return false;
                return BuiltInRegistries.ITEM.containsKey(id);
            });
            DEFAULT_WEST_CHEST = BUILDER.comment("Should the west chest content be the default content (music discs) ?").define("default_west_chest",true);
            WEST_CHEST_ITEMS = BUILDER.comment("The items for the items in the western chest (Used if the previous config is set to false)").worldRestart().defineList("west_chest_items",List.of(),()->"minecraft:stone",(obj)->{
                if (!(obj instanceof String str))return false;
                ResourceLocation id = ResourceLocation.tryParse(str);
                if (id == null) return false;
                return BuiltInRegistries.ITEM.containsKey(id);
            });
            END_SIEGE_ITEMS_CHESTS = BUILDER.comment("The amount of different items requires in each chest before starting the End Siege").worldRestart().defineInRange("end_siege_items_per_chest",12,1,36);
            END_SIEGE_KILLS = BUILDER.comment("The amount of kills the player must do before his sigil be upgraded").worldRestart().defineInRange("end_siege_kills",100,1,Integer.MAX_VALUE);
            BUILDER.pop();
        }
        BUILDER.pop();

        /*BUILDER.push("Effects");
        {

        }
        BUILDER.pop();*/
        SPEC = BUILDER.build();
    }

}

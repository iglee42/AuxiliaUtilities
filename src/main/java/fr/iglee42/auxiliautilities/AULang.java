package fr.iglee42.auxiliautilities;

import fr.iglee42.auxiliautilities.client.AUClient;
import fr.iglee42.auxiliautilities.network.SendChatMessagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnegative;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum AULang {

    TAB("itemGroup." + AuxiliaUtilities.MODID, "Auxilia Utilities"),

    DOOM_EFFECT("effect","doom","Doom"),
    FIZZY_LIFTING_EFFECT("effect","fizzy_lifting","Fizzy Lifting"),
    LOVE_EFFECT("effect","love","Love"),
    GRAVITY_EFFECT("effect","gravity","Gravity"),
    SECOND_CHANCE_EFFECT("effect","second_chance","Second Chance"),
    PURGING_EFFECT("effect","purging","Purging"),
    GREEK_FIRE_EFFECT("effect","greek_fire","Greek Fire"),
    RELAPSE_EFFECT("effect","relapse","Relapse"),

    ANGEL_BLOCK("block","angel_block","Angel Block"),
    DEMON_BLOCK("block","demon_block","Demon Block"),
    ENCHANTED_BLOCK("block","enchanted_block","Enchanted Block"),
    EVIL_INFUSED_IRON_BLOCK("block","evil_infused_iron_block","Evil Infused Iron Block"),

    MANUAL_MILL("block","manual_mill","Manual Mill"),
    SOLAR_PANEL("block","solar_panel","Solar Panel"),
    LUNAR_PANEL("block","lunar_panel","Lunar Panel"),
    FIRE_MILL("block","fire_mill","Fire Mill"),
    LAVA_MILL("block","lava_mill","Lava Mill"),
    WATER_MILL("block","water_mill","Water Mill"),
    WIND_MILL("block","wind_mill","Wind Mill"),
    DRAGON_EGG_MILL("block","dragon_egg_mill","Dragon Egg Mill"),
    CREATIVE_MILL("block","creative_mill","Creative Mill"),
    SOUND_MUFFLER("block","sound_muffler","Sound Muffler"),
    RESONATOR("block","resonator","Resonator"),
    ENCHANTER("block","enchanter","Enchanter"),
    FURNACE("block","furnace","Electric Furnace"),
    CRUSHER("block","crusher","Crusher"),
    MAGICAL_WOOD("block","magical_wood","Magical Wood"),
    MAGICAL_PLANKS("block","magical_planks","Magical Planks"),
    DIAGONAL_WOOD("block","diagonal_wood","Diagonal Wood"),
    MACHINE_BLOCK("block","machine_block","Machine Block"),

    ENDER_LILLY("block","ender_lilly","Ender Lilly"),
    RED_ORCHID("block","red_orchid","Red Orchid"),
    COMPRESSED_BLOCKS("block","compressed_blocks","%s Blocks"),
    REDSTONE_CLOCK("block","redstone_clock","Redstone Clock"),

    SURVIVAL_GENERATOR("block","survival_generator","Survival Generator"),
    FURNACE_GENERATOR("block","furnace_generator","Furnace Generator"),
    OVERCLOCKED_GENERATOR("block","overclocked_generator","Overclocked Generator"),
    CULINARY_GENERATOR("block","culinary_generator","Culinary Generator"),
    MAGMATIC_GENERATOR("block","magmatic_generator","Magmatic Generator"),
    POTION_GENERATOR("block","potion_generator","Potion Generator"),
    SLIMEY_GENERATOR("block","slimey_generator","Slimey Generator"),
    PINK_GENERATOR("block","pink_generator","Pink Generator"),
    DEATH_GENERATOR("block","death_generator","Death Generator"),
    HEATED_REDSTONE_GENERATOR("block","heated_redstone_generator","Heated Redstone Generator"),
    ENDER_GENERATOR("block","ender_generator","Ender Generator"),
    DISENCHANTMENT_GENERATOR("block","disenchantment_generator","Disenchantment Generator"),
    EXPLOSIVE_GENERATOR("block","explosive_generator","Explosive Generator"),
    FROSTY_GENERATOR("block","frosty_generator","Frosty Generator"),
    HALITOSIS_GENERATOR("block","halitosis_generator","Halitosis Generator"),
    NETHER_STAR_GENERATOR("block","nether_star_generator","Nether Star Generator"),
    RAINBOW_GENERATOR("block","rainbow_generator","Rainbow Generator !"),
    RAINBOW_GENERATOR_BOTTOM("item","rainbow_generator_bottom","Rainbow Generator (Bottom Half)"),
    RAINBOW_GENERATOR_TOP("item","rainbow_generator_top","Rainbow Generator (Top Half)"),

    MISERABLE_OPINIUM_CORE("block","miserable_opinium_core","Opinium Core (Miserable)"),
    PATHETIC_OPINIUM_CORE("block","pathetic_opinium_core","Opinium Core (Pathetic)"),
    MEDIOCRE_OPINIUM_CORE("block","mediocre_opinium_core","Opinium Core (Mediocre)"),
    PASSABLE_OPINIUM_CORE("block","passable_opinium_core","Opinium Core (Passable)"),
    DECENT_OPINIUM_CORE("block","decent_opinium_core","Opinium Core (Decent)"),
    SOLID_OPINIUM_CORE("block","solid_opinium_core","Opinium Core (Solid)"),
    GOOD_OPINIUM_CORE("block","good_opinium_core","Opinium Core (Good)"),
    DAMN_GOOD_OPINIUM_CORE("block","damn_good_opinium_core","Opinium Core (Damn Good)"),
    AMAZING_OPINIUM_CORE("block","amazing_opinium_core","Opinium Core (Amazing)"),
    INSPIRING_OPINIUM_CORE("block","inspiring_opinium_core","Opinium Core (Inspiring)"),
    PERFECTED_OPINIUM_CORE("block","perfected_opinium_core","Opinium Core (Perfected)"),

    STONE_DRUM("block","stone_drum","Stone Drum"),
    COPPER_DRUM("block","copper_drum","Copper Drum"),
    IRON_DRUM("block","iron_drum","Iron Drum"),
    REINFORCED_LARGE_DRUM("block","reinforced_large_drum","Reinforced Large Drum"),
    NETHERITE_DRUM("block","netherite_drum","Netherite Drum"),
    DEMONICALLY_GARGANTUAN_DRUM("block","demonically_gargantuan_drum","Demonically Gargantuan Drum"),
    CREATIVE_DRUM("block","creative_drum","Creative Drum"),
    KLEIN_BOTTLE("block","klein_bottle","Klein Bottle"),
    ENDER_PORCUPINE("block","ender_porcupine","Ender Porcupine"),
    CURSED_EARTH("block","cursed_earth","Cursed Earth"),

    SANDY_GLASS("block","sandy_glass","Sandy Glass"),
    THICKENED_GLASS("block","thickened_glass","Thickened Glass"),
    THICKENED_GLASS_BORDERED("block","thickened_glass_bordered","Thickened Glass (Bordered)"),
    THICKENED_GLASS_PATTERNED("block","thickened_glass_patterned","Thickened Glass (Patterned)"),
    DARK_INEFFABLE_GLASS("block","dark_ineffable_glass","Dark Ineffable Glass"),
    DARK_GLASS("block","dark_glass","Dark Glass"),
    GLOWING_GLASS("block","glowing_glass","Glowing Glass"),
    REDSTONE_GLASS("block","redstone_glass","Redstone Glass"),
    OBSIDIAN_GLASS("block","obsidian_glass","Obsidian Glass"),
    ETHEREAL_GLASS("block","ethereal_glass","Ethereal Glass"),
    INEFFABLE_GLASS("block","ineffable_glass","Ineffable Glass"),
    REVERSE_ETHEREAL_GLASS("block","reverse_ethereal_glass","Reverse Ethereal Glass"),

    CLIMOGRAPH_BLOCK("block","climograph_block","Climograph Base Unit"),
    TERRAFORMER("block","terraformer","Terraformer"),
    COOLER("block","cooler","Cooler"),
    HEATER("block","heater","Heater"),
    HUMIDIFIER("block","humidifier","Humidifier"),
    DEHUMIDIFIER("block","dehumidifier","Dehumidifier"),
    MAGIC_INFUSER("block","magic_infuser","Magic Infuser"),
    MAGIC_ABSORBER("block","magic_absorber","Magic Absorber"),
    DESHOSTILIFIER("block","deshostilifier","Deshostilifier"),
    ANTENNA("block","antenna","Antenna"),

    WOODEN_SPIKE("block","wooden_spike","Wooden Spike"),
    STONE_SPIKE("block","stone_spike","Stone Spike"),
    COPPER_SPIKE("block","copper_spike","Copper Spike"),
    IRON_SPIKE("block","iron_spike","Iron Spike"),
    GOLDEN_SPIKE("block","golden_spike","Golden Spike"),
    DIAMOND_SPIKE("block","diamond_spike","Diamond Spike"),
    NETHERITE_SPIKE("block","netherite_spike","Netherite Spike"),
    CREATIVE_SPIKE("block","creative_spike","Creative Spike"),

    BUILDERS_WAND_ITEM("item","builders_wand","Builder's Wand"),
    CREATIVE_BUILDERS_WAND_ITEM("item","creative_builders_wand","Creative Builder's Wand"),
    DESTRUCTION_WAND_ITEM("item","destruction_wand","Destruction Wand"),
    CREATIVE_DESTRUCTION_WAND_ITEM("item","creative_destruction_wand","Creative Destruction Wand"),
    ENDER_SHARD("item","ender_shard","Ender Shard"),
    GLASS_CUTTER("item","glass_cutter","Glass Cutter"),
    WATERING_CAN("item","watering_can","Watering Can"),
    UNSTABLE_INGOT("item","unstable_ingot","Unstable Ingot"),
    STABLE_UNSTABLE_INGOT("item","stable_unstable_ingot","Stable-\"Unstable Ingot\""),
    STABLE_UNSTABLE_NUGGET("item","stable_unstable_nugget","Stable-\"Unstable Nugget\""),
    SUN_CRYSTAL("item","sun_crystal","Sun Crystal"),
    SUN_CRYSTAL_EMPTY("item","sun_crystal.empty","Sun Crystal (Empty)"),
    LUX_SABER("item","lux_saber","Lux Saber"),
    RESONATING_REDSTONE_CRYSTAL("item","resonating_redstone_crystal","Resonating Redstone Crystal"),
    REDSTONE_GEAR("item","redstone_gear","Redstone Gear"),
    EYE_OF_REDSTONE("item","eye_of_redstone","Eye of Redstone"),
    LUNAR_REACTIVE_DUST("item","lunar_reactive_dust","Lunar Reactive Dust"),
    RED_COAL("item","red_coal","Red Coal"),
    MOON_STONE("item","moon_stone","Moon Stone"),
    DEMON_INGOT("item","demon_ingot","Demon Ingot"),
    ENCHANTED_INGOT("item","enchanted_ingot","Enchanted Ingot"),
    EVIL_INFUSED_IRON_INGOT("item","evil_infused_iron_ingot","Evil Infused Iron Ingot"),
    DEMON_NUGGET("item","demon_nugget","Demon Nugget"),
    ENCHANTED_NUGGET("item","enchanted_nugget","Enchanted Nugget"),
    EVIL_INFUSED_IRON_NUGGET("item","evil_infused_iron_nugget","Evil Infused Iron Nugget"),
    UPGRADE_BASE("item","upgrade_base","Upgrade Base"),
    SPEED_UPGRADE("item","speed_upgrade","Speed Upgrade"),
    ENCHANTED_SPEED_UPGRADE("item","enchanted_speed_upgrade","Speed Upgrade (Magical)"),
    ULTIMATE_SPEED_UPGRADE("item","ultimate_speed_upgrade","Speed Upgrade (Ultimate)"),
    MAGICAL_APPLE("item","magical_apple","Magical Apple"),
    BIOME_MARKER("item","biome_marker","Biome Marker"),
    COMPOUND_BOW("item","compound_bow","Compound Bow"),
    KIKOKU("item","kikoku","Kikoku"),
    FLUID_DROPLET("item","fluid_droplet","Fluid Droplet"),
    ENERGY_DROPLET("item","energy_droplet","Energy Droplet"),
    FLAT_ITEM_TRANSFER_NODE("item","flat_item_transfer_node","Flat Transfer Node (Items)"),
    FLAT_FLUID_TRANSFER_NODE("item","flat_fluid_transfer_node","Flat Transfer Node (Fluids)"),
    FILTER_ITEMS("item","item_filter","Items Filter"),
    FILTER_FLUIDS("item","fluid_filter","Fluids Filter"),
    POWER_MANAGER("item","power_manager","Power Manager"),
    DROP_OF_EVIL("item","drop_of_evil","Drop of Evil"),

    UNACTIVATED_DIVISION_SIGIL("item","unactivated_division_sigil","Division Sigil"),
    DIVISION_SIGIL("item","division_sigil","Division Sigil (Activated)"),
    PSEUDO_INVERSION_SIGIL("item","pseudo_inversion_sigil","Pseudo-Inversion Sigil"),

    CHICKEN_RING("item","chicken_ring","Chicken Wing Ring"),
    FLYING_SQUID_RING("item","flying_squid_ring","Ring of the Flying Squid"),
    ANGEL_RING("item","angel_ring","Angel Ring"),

    GOLDEN_LASSO("item","golden_lasso","Golden Lasso"),
    CURSED_LASSO("item","cursed_lasso","Cursed Lasso"),

    UPGRADE_SPEED("upgrade","speed","Increases speed of operations"),

    WOODEN_SICKLE("item","wooden_sickle","Wooden Sickle"),
    STONE_SICKLE("item","stone_sickle","Stone Sickle"),
    IRON_SICKLE("item","iron_sickle","Iron Sickle"),
    GOLDEN_SICKLE("item","golden_sickle","Golden Sickle"),
    DIAMOND_SICKLE("item","diamond_sickle","Diamond Sickle"),
    NETHERITE_SICKLE("item","netherite_sickle","Netherite Sickle"),

    DOOM_MESSAGE("message","doom_effect","The Specter of Death will arrive in %s seconds."),
    SECOND_CHANCE_MESSAGE("message","second_chance_effect","Second Chance !"),
    SECOND_CHANCE_ALREADY_USED_MESSAGE("message","second_chance_effect.already_used","Unfortunately we can't be lucky too many times !"),
    MAGICAL_APPLE_USE("message","magical_apple_use","You feel your luck is changing"),

    UNSTABLE_INGOT_TOOLTIP_ERROR("tooltip","unstable_ingot.error","§cERROR : Divide by diamond"),
    UNSTABLE_INGOT_TOOLTIP_0("tooltip","unstable_ingot","This ingot is highly unstable and will explode after 10 seconds."),
    UNSTABLE_INGOT_TOOLTIP_1("tooltip","unstable_ingot.1","Will also explode if the crafting window is closed or the ingot is thrown on the ground."),
    UNSTABLE_INGOT_TOOLTIP_2("tooltip","unstable_ingot.2","Additionally these ingots do not stack."),
    UNSTABLE_INGOT_TOOLTIP_3("tooltip","unstable_ingot.3","§l - Do not craft unless ready -\n"),
    UNSTABLE_INGOT_TOOLTIP_4("tooltip","unstable_ingot.4","Must be crafted in a vanilla crafting table."),
    UNSTABLE_INGOT_TOOLTIP_EXPLOSION("tooltip","unstable_ingot.explosion","Explosion in %s"),
    STORED_ENERGY_TOOLTIP("tooltip","stored_energy","%s FE / %s FE"),
    STORED_FLUID_TOOLTIP("tooltip","stored_fluid","%s of %s mB"),
    STORED_ENERGY_TOOLTIP_ITEM("tooltip","stored_energy_item","Stored Energy : %s FE"),
    STORED_FLUID_TOOLTIP_ITEM("tooltip","stored_fluid_item","Stored Fluid : %s mB of %s"),
    STORED_BIOME_TOOLTIP("tooltip","stored_biome","Stored Biome : %s"),
    AREA_TOOLTIP("tooltip","area","Area : %sx%s blocks"),
    RANGE_TOOLTIP("tooltip","range","Range : %s blocks"),
    HOLD_SHIFT_TOOLTIP("tooltip","hold_shift","§7Hold Shift for description"),
    HOLD_CTRL_TOOLTIP("tooltip","hold_control","§7Hold Control for more info"),
    RESONATOR_REQUIRES_RAINBOW_GENERATOR("tooltip","resonator_requires_rainbow_generator","Requires an active Rainbow Generator"),

    GP_TOOLTIP("tooltip","gp","Grid Power : %s / %s"),
    BLOCK_NO_GP("tooltip","block_no_gp","No Power Used/Generated"),
    BLOCK_GENERATE_GP("tooltip","block_generate_gp","Power Generating : %s GP"),
    BLOCK_DRAIN_GP("tooltip","block_drain_gp","Power Drain : %s GP"),
    NO_GP_HOLDERS("tooltip","no_gp_holders","No GP Holders"),
    GP_GENERATORS("tooltip","gp_generators","GP Generators"),
    GP_DRAINERS("tooltip","gp_drainers","GP Drainers"),
    GP_INACTIVE("tooltip","gp_inactive","Inactive GP Holders"),

    ENCHANTER_NEEDS_BOOKSHELVES("tooltip","enchanter_needs_bookshelves","Enchanter requires a full set of bookshelves nearby or other enchanting boosting block !"),

    PROGRESS_TIME_TOOLTIP("tooltip","time_progress","%s / %s"),
    SPEED_UPGRADES_TOOLTIP("tooltip","speed_upgrades","Speed Upgrades"),
    MAX_UPGRADES_TOOLTIP("tooltip","max_upgrades","Max Upgrades : %s"),
    POWER_PENALTY_TOOLTIP("tooltip","power_penalty","Power Penalty : +%s GP"),
    POWER_PENALTY_TOOLTIP_LEVEL("tooltip","power_penalty_level","Power Penalty (level %s) : +%s GP"),

    KIKOKU_TOOLTIP("tooltip","kikoku","A powerful katana used to slay powerful foes."),
    KIKOKU_TOOLTIP_1("tooltip","kikoku.1","§7Thanks to RWTema for creating Extra Utilities §c❤§7."),

    CLIMOGRAPH_TOOLTIP("tooltip","climograph","Climograph"),
    TERRAFORMER_TOOLTIP("tooltip","terraformer","A powerful machine that changes the biome of a large area with the help of climographs."),
    HUMIDIFIER_TOOLTIP("tooltip","humidifier","Increases humidity of the area."),
    DEHUMIDIFIER_TOOLTIP("tooltip","dehumidifier","Absorbs humidity of the area."),
    HEATER_TOOLTIP("tooltip","heater","Increases temperature of the area."),
    COOLER_TOOLTIP("tooltip","cooler","Reduces temperature of the area."),
    DESHOSTILIFIER_TOOLTIP("tooltip","deshostilifier","Reduces hostility of the area."),
    MAGIC_ABSORBER_TOOLTIP("tooltip","magic_absorber","Absorbs magical energy of the area."),
    MAGIC_INFUSER_TOOLTIP("tooltip","magic_infuser","Infuses magical energy into the area."),
    ANTENNA_TOOLTIP("tooltip","antenna","A device that used to send all the terraforming force (TF) to the terraformer."),

    CHICKEN_RING_TOOLTIP("tooltip","chicken_ring","Flight of the majestic beast!"),
    CHICKEN_RING_TOOLTIP_1("tooltip","chicken_ring.1","Uses 1 GP."),
    FLYING_SQUID_RING_TOOLTIP("tooltip","flying_squid_ring","Jet propulsion at your fingertips!"),
    FLYING_SQUID_RING_TOOLTIP_1("tooltip","flying_squid_ring.1","Uses 16 GP."),
    ANGEL_RING_TOOLTIP("tooltip","angel_ring","Fly like the god of this world."),
    ANGEL_RING_TOOLTIP_1("tooltip","angel_ring.1","Uses 32 GP."),

    ORIGIN_GUI_MESSAGE("gui","origin","Origin : [%s, %s, %s]"),
    SCANNING_GUI_MESSAGE("gui","scanning","Scanning : [%s, %s, %s]"),
    RANGE_START_GUI_MESSAGE("gui","range_start","Block Range Start"),
    RANGE_END_GUI_MESSAGE("gui","range_end","Block Range End"),

    MISSING_ANTENNA_GUI_MESSAGE("gui","missing_antenna","Missing Antenna"),
    NO_BIOME_MARKER_GUI_MESSAGE("gui","no_biome_marker","No Biome Marker."),
    SEARCHING_GUI_MESSAGE("gui","searching","Searching..."),
    PROCESS_POS_GUI_MESSAGE("gui","process_pos","Processing : %s %s %s"),
    TRANSFORM_TIME_GUI_MESSAGE("gui","transform_time","Transform Time : %s"),
    MISSING_CLIMOGRAPHS_GUI_MESSAGE("gui","missing_climographs","Missing Climographs : "),
    PLACE_IN_RANGE_GUI_MESSAGE("gui","place_in_range","Place Climographs within %s blocks of Terraformer"),

    ACTIVATION_RITUAL("message","activation_ritual","Activation Ritual :"),
    ACTIVATION_RITUAL_VALID("message","activation_ritual.valid","Perform the sacrifice !"),
    ACTIVATION_RITUAL_SKY_INVALID("message","activation_ritual.can_see_sky.invalid","Altar cannot see the moon"),
    ACTIVATION_RITUAL_SKY_VALID("message","activation_ritual.can_see_sky.valid","Altar can see the moon"),
    ACTIVATION_RITUAL_REDSTONE_INVALID("message","activation_ritual.redstone_circle.invalid","Altar does not have a redstone circle"),
    ACTIVATION_RITUAL_REDSTONE_VALID("message","activation_ritual.redstone_circle.valid","Altar has a redstone circle"),
    ACTIVATION_RITUAL_DIRT_INVALID("message","activation_ritual.dirt_under.invalid","Altar and Circle not placed on dirt"),
    ACTIVATION_RITUAL_DIRT_VALID("message","activation_ritual.dirt_under.valid","Altar and Circle placed on dirt"),
    ACTIVATION_RITUAL_NATURAL_INVALID("message","activation_ritual.enough_natural.invalid","Area lacks sufficient natural earth"),
    ACTIVATION_RITUAL_NATURAL_VALID("message","activation_ritual.enough_natural.valid","Altar has sufficient natural earth"),
    ACTIVATION_RITUAL_MIDNIGHT_INVALID("message","activation_ritual.midnight.invalid","Sacrifice must be made at midnight"),
    ACTIVATION_RITUAL_MIDNIGHT_VALID("message","activation_ritual.midnight.valid","Time is right"),
    ACTIVATION_RITUAL_LIGHT_INVALID("message","activation_ritual.no_light.invalid","Altar must not be lit by outside sources"),
    ACTIVATION_RITUAL_LIGHT_VALID("message","activation_ritual.no_light.valid","Altar is in darkness"),

    STABILIZATION_RITUAL("message","stabilization_ritual","Stabilization Ritual :"),
    STABILIZATION_RITUAL_OVERWORLD("message","stabilization_ritual.overworld","Too much natural earth"),
    STABILIZATION_RITUAL_NETHER("message","stabilization_ritual.nether","Too hot"),
    STABILIZATION_RITUAL_OTHER("message","stabilization_ritual.other_dimension","This dimension doesn't seems to have a power source"),
    STABILIZATION_RITUAL_NOT_MAIN_END("message","stabilization_ritual.end_too_far","Too far from the power source"),
    STABILIZATION_RITUAL_READY("message","stabilization_ritual.ready","Everything is prepared."),
    STABILIZATION_RITUAL_SACRIFICE("message","stabilization_ritual.sacrifice","Sacrifice one who would sacrifice himself."),
    STABILIZATION_RITUAL_NO_MARKINGS("message","stabilization_ritual.no_markings","Ritual Markings: No markings present"),
    STABILIZATION_RITUAL_ONE_MARKING("message","stabilization_ritual.one_marking","Ritual Markings: Only 1 type of marking present"),
    STABILIZATION_RITUAL_STRENGTH("message","stabilization_ritual.strength","Ritual Markings: Strength - %s"),
    STABILIZATION_RITUAL_MISSING_CHEST("message","stabilization_ritual.missing_chest","%s Chest not present"),
    STABILIZATION_RITUAL_NORTH_CHEST("message","stabilization_ritual.north_chest","To the north, Children of Fire : %s / %s"),
    STABILIZATION_RITUAL_SOUTH_CHEST("message","stabilization_ritual.south_chest","To the south, Gifts of Earth : %s / %s"),
    STABILIZATION_RITUAL_EAST_CHEST("message","stabilization_ritual.east_chest","To the east, Descendants of Water : %s / %s"),
    STABILIZATION_RITUAL_WEST_CHEST("message","stabilization_ritual.west_chest","To the south, Spices of Air : %s / %s"),

    END_SIEGE("message","end_siege","The End Siege"),
    END_SIEGE_BEGIN("message","end_siege_begin","The Siege of the End has begun !"),
    END_SIEGE_END("message","end_siege_end","The Siege of the End has ended !"),

    SIGIL_UPGRADED("message","sigil_upgrades","Your sigil has been upgraded !"),
    KILLS("message","kills","Kills : %s"),

    NORTHERN("message","northern","Northern"),
    SOUTHERN("message","southern","Southern"),
    WESTERN("message","western","Western"),
    EASTERN("message","eastern","Eastern"),

    LASSO_HOSTILE_MOB("message","lasso.hostile_mob","%s is a hostile mob."),
    LASSO_NOT_HOSTILE_MOB("message","lasso.not_hostile_mob","%s is not a hostile mob."),
    LASSO_ATTACKING("message","lasso.attacking","%s is too busy attacking someone."),
    LASSO_TOO_MANY_HEALTH("message","lasso.too_many_health","%s has too much health (%s hearts). Reduces to %s hearts."),

    FLAT_TRANSFER_NODE_TOOLTIP("tooltip","flat_transfer_node","Thinner than the thinnest of pancakes."),
    FLAT_TRANSFER_NODE_TOOLTIP_1("tooltip","flat_transfer_node.1","Small enough to fit between blocks"),
    FLAT_TRANSFER_NODE_TOOLTIP_2("tooltip","flat_transfer_node.2","Hold %s when placing to reverse pull/push."),

    FILTER_FLAG_COMPONENTS_OFF("item","filter.flag.ignore_components_off","Match Components"),
    FILTER_FLAG_COMPONENTS_ON("item","filter.flag.ignore_components_on","Ignore Components"),
    FILTER_FLAG_INVERTED_OFF("item","filter.flag.inverted_off","Whitelist"),
    FILTER_FLAG_INVERTED_ON("item","filter.flag.inverted_on","Blacklist"),

    RED_ORCHID_TOOLTIP("tooltip","red_orchid","A beautiful flower that seems to thrive on redstone energy."),
    RED_ORCHID_TOOLTIP_1("tooltip","red_orchid.1","Plant on any redstone ore."),
    RED_ORCHID_TOOLTIP_2("tooltip","red_orchid.2","Can be found in spawner and stronghold chests."),

    ENDER_LILLY_TOOLTIP("tooltip","ender_lilly","A beautiful flower that produces some teleportation pearls."),
    ENDER_LILLY_TOOLTIP_1("tooltip","ender_lilly.1","Plant on end stone."),
    ENDER_LILLY_TOOLTIP_2("tooltip","ender_lilly.2","Can be found in spawner and stronghold chests."),

    DEMON_INGOT_TOOLTIP("tooltip","demon_ingot","Legends tell of an ancient god of the underworld that appeared way before you."),
    DEMON_INGOT_TOOLTIP_1("tooltip","demon_ingot.1","After its death, his soul was trapped inside spooky scary creatures and fire weaved constructs."),
    DEMON_INGOT_TOOLTIP_2("tooltip","demon_ingot.2","They created a cult around this god trapped inside of them and made some kind of offering in a lava well that can be found in the ruins of a terrifying fortress."),
    DEMON_INGOT_TOOLTIP_3("tooltip","demon_ingot.3","These tributes of precious ingots were answered by some special ancient metals."),

    THICKENED_GLASS_TOOLTIP("tooltip","thickened_glass","A bit harder glass."),
    THICKENED_GLASS_BORDERED_TOOLTIP("tooltip","thickened_glass_bordered","A bit harder glass but with a border."),
    THICKENED_GLASS_PATTERNED_TOOLTIP("tooltip","thickened_glass_patterned","A bit harder glass but with patterns."),
    DARK_GLASS_TOOLTIP("tooltip","dark_glass","A glass that doesn't let light pass through."),
    GLOWING_GLASS_TOOLTIP("tooltip","glowing_glass","A glass that emits light."),
    REDSTONE_GLASS_TOOLTIP("tooltip","redstone_glass","A glass that emits redstone."),
    OBSIDIAN_GLASS_TOOLTIP("tooltip","obsidian_glass","A glass that is so solid that wither and ender dragon can't break it."),
    ETHEREAL_GLASS_TOOLTIP("tooltip","ethereal_glass","A glass that lets player pass through."),
    REVERSE_ETHEREAL_GLASS_TOOLTIP("tooltip","reverse_ethereal_glass","A glass that doesn't let player pass through."),
    INEFFABLE_GLASS_TOOLTIP("tooltip","ineffable_glass","A glass that lets player pass through."),
    DARK_INEFFABLE_GLASS_TOOLTIP("tooltip","dark_ineffable_glass","A glass that doesn't let player and light pass through."),

    WOODEN_SPIKE_TOOLTIP("tooltip","wooden_spike","Reduces health to 1 heart, but doesn't kill"),
    STONE_SPIKE_TOOLTIP("tooltip","stone_spike","Reduces health to half a heart, but doesn't kill"),
    GOLDEN_SPIKE_TOOLTIP("tooltip","golden_spike","Mobs drop experience"),
    DIAMOND_SPIKE_TOOLTIP("tooltip","diamond_spike","Mobs drop 'Player-Kill Only' items"),
    NETHERITE_SPIKE_TOOLTIP("tooltip","netherite_spike","Mobs drop experience, 'Player-Kill Only' and 'Fire Only' items"),
    CREATIVE_SPIKE_TOOLTIP("tooltip","creative_spike","Instantly kills any mob, even in creative mode"),

    LASSO_STORED_ENTITY_TOOLTIP("tooltip","lasso.stored_entity","Stored Entity : %s"),
    LASSO_HEALTH_TOOLTIP("tooltip","lasso.health","Health : %s/%s"),
    LASSO_PROFESSION_TOOLTIP("tooltip","lasso.profession","Profession : %s"),

    CURSED_EARTH_TOOLTIP("tooltip","cursed_earth","A corrupted dirt that spawns a lot of powerful mobs on it."),
    CURSED_EARTH_TOOLTIP_1("tooltip","cursed_earth.1","This block can burn if it is exposed too longer to sun."),
    DROP_OF_EVIL_TOOLTIP("tooltip","drop_of_evil","A corrupted shard dropped from wither skeletons."),
    DROP_OF_EVIL_TOOLTIP_1("tooltip","drop_of_evil.1","Produces a 5x5 of Cursed Earth when used on dirt/grass."),

    DOOM_DEATH("death.attack.doom","%s met their doom"),
    DOOM_DEATH_ITEM("death.attack.doom.item","%s met their doom"),
    DOOM_DEATH_PLAYER("death.attack.doom.player","%s met their doom whilst fighting %s"),

    UNSTABLE_DEATH("death.attack.unstable","%s suffered a fatal (java.lang.ArithmeticException : / by diamond)"),
    UNSTABLE_DEATH_ITEM("death.attack.unstable.item","%s suffered a fatal (java.lang.ArithmeticException : / by diamond)"),
    UNSTABLE_DEATH_PLAYER("death.attack.unstable.player","%s suffered a fatal (java.lang.ArithmeticException : / by diamond) whilst fighting %s"),

    SPIKE_DEATH("death.attack.spike","%s walked on a pointy spike (ouchies)"),
    SPIKE_DEATH_ITEM("death.attack.spike.item","%s walked on a pointy spike (ouchies)"),
    SPIKE_DEATH_PLAYER("death.attack.spike.player","%s walked on a pointy spike (ouchies)"),

    SPIKE_CREATIVE_DEATH("death.attack.creative_spike","%s failed to become the guy"),
    SPIKE_CREATIVE_DEATH_ITEM("death.attack.creative_spike.item","%s failed to become the guy"),
    SPIKE_CREATIVE_DEATH_PLAYER("death.attack.creative_spike.player","%s failed to become the guy")
    ;

    private final String key;
    private final String defaultTranslation;

    AULang(String category, String key,String defaultTranslation) {
        this.key = category + "." + AuxiliaUtilities.MODID + "." + key;
        this.defaultTranslation = defaultTranslation;
    }

    AULang(String key, String defaultTranslation) {
        this.key = key;
        this.defaultTranslation = defaultTranslation;
    }

    public String getDefaultTranslation() {
        return defaultTranslation;
    }

    public String getKey() {
        return key;
    }

    public MutableComponent get(){
        return Component.translatable(this.key);
    }

    public MutableComponent get(Object... args){
        return Component.translatable(this.key,args);
    }

    public void sendToPlayer(Player player,UUID signature){
        sendMessageToPlayer(player,get(),signature);
    }

    public void sendToPlayer(Player player,UUID signature,Object... args){
        sendMessageToPlayer(player,get(args),signature);
    }

    public static final int MAX_TOOLTIP = 10;

    public static List<Component> getTooltips(ItemStack stack){
        List<Component> tooltips = new ArrayList<>();
        String descriptionId = stack.getItem().getDescriptionId();
        String[] parts = descriptionId.split("\\.");
        if (parts.length < 3) return tooltips;
        String base = "tooltip."+parts[1]+"."+parts[2];
        for (int i = 0; i < MAX_TOOLTIP; i++) {
            String key = base + (i == 0 ? "" : "." + i);
            String translation = Component.translatable(key).getString();
            if (translation.equals(key)) break;
            tooltips.add(Component.translatable(key));
        }
        return tooltips;
    }

    public static MessageSignature createSignatureFromUUID(UUID uuid){
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.position(32);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return new MessageSignature(buffer.array());
    }

    public static void sendMessageToPlayer(Player player, @Nullable Component message, UUID signature){
        if (player.level().isClientSide){
            AUClient.sendPlayerMessage(player,message,createSignatureFromUUID(signature));
        } else {
            PacketDistributor.sendToPlayer((ServerPlayer) player,new SendChatMessagePacket(message,signature));
        }
    }

    public static String formatDurationSeconds(@Nonnegative long ticks, boolean incExtras) {
        long t = ticks % 20L;
        long s = (ticks % 1200L) / 20L;
        long m = (ticks % 72000L) / 1200L;
        long h = (ticks % 1728000L) / 72000L;
        long d = ticks / 1728000L;

        StringBuilder builder = new StringBuilder();
        boolean hasHigherUnit = false;

        if (d > 0L) {
            builder.append(d).append("d");
            hasHigherUnit = true;
        }

        if (h > 0L || (incExtras && hasHigherUnit)) {
            builder.append(h).append("h");
            hasHigherUnit = true;
        }

        if (m > 0L || (incExtras && hasHigherUnit)) {
            builder.append(m).append("m");
            hasHigherUnit = true;
        }

        if (s > 0L || t > 0L || incExtras) {
            if (t == 0L || !incExtras) {
                builder.append(s).append("s");
            } else {
                float seconds = s + (t / 20.0F);
                builder.append(String.format(java.util.Locale.ROOT, "%.2f", seconds)).append("s");
            }
        }

        return builder.toString();
    }

    @OnlyIn(Dist.CLIENT)
    private static int getTextSizeClient(Component text){
        return Minecraft.getInstance().font.width(text);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    private static int getTextSizeServer(Component text){
        return text.getString().length();
    }

    public static int getTextSize(Component text){
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return getTextSizeClient(text);
        } else {
            return getTextSizeServer(text);
        }
    }
}

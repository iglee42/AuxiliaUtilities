package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.ClientGPManager;
import fr.iglee42.auxiliautilities.interblocks.FlatTransferNodeHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AuxiliaUtilities.MODID);

    public static final DeferredItem<ItemBuildersWand> BUILDERS_WAND = ITEMS.register("builders_wand", ()->new ItemBuildersWand(
            new Item.Properties().stacksTo(1),
            9,
            new float[]{ 0.95686275F, 0.9019608F, 0.30588236F }
    ));

    public static final DeferredItem<ItemBuildersWand> CREATIVE_BUILDERS_WAND = ITEMS.register("creative_builders_wand", ()->new ItemBuildersWand(
            new Item.Properties().stacksTo(1),
            49,
            new float[]{ 0.7490196F, 0.29411766F, 0.95686275F }
    ));

    public static final DeferredItem<ItemDestructionWand> DESTRUCTION_WAND = ITEMS.register("destruction_wand", ()->new ItemDestructionWand(
            new Item.Properties().stacksTo(1),
            9,
            new float[]{ 0.95686275F, 0.9019608F, 0.30588236F }
    ));

    public static final DeferredItem<ItemDestructionWand> CREATIVE_DESTRUCTION_WAND = ITEMS.register("creative_destruction_wand", ()->new ItemDestructionWand(
            new Item.Properties().stacksTo(1),
            49,
            new float[]{ 0.7490196F, 0.29411766F, 0.95686275F }
    ));


    public static final DeferredItem<Item> GLASS_CUTTER = ITEMS.register("glass_cutter",()-> new ItemGlassCutter(new Item.Properties().durability(250)));
    public static final DeferredItem<Item> WATERING_CAN = ITEMS.register("watering_can",()-> new ItemWateringCan(new Item.Properties()));
    public static final DeferredItem<Item> LUX_SABER = ITEMS.register("lux_saber",()-> new ItemLuxSaber(new Item.Properties()));

    //CRAFTING INGREDIENTS
    public static final DeferredItem<Item> ENDER_SHARD = ITEMS.register("ender_shard",()-> new AUItem(new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> UNSTABLE_INGOT = ITEMS.register("unstable_ingot",()-> new ItemUnstableIngot(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> STABLE_UNSTABLE_INGOT = ITEMS.register("stable_unstable_ingot",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> STABLE_UNSTABLE_NUGGET = ITEMS.register("stable_unstable_nugget",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> SUN_CRYSTAL = ITEMS.register("sun_crystal",()-> new ItemSunCrystal(new Item.Properties()));
    public static final DeferredItem<Item> RESONATING_REDSTONE_CRYSTAL = ITEMS.register("resonating_redstone_crystal",()-> new ItemResonatingRedstoneCrystal(new Item.Properties()));
    public static final DeferredItem<Item> REDSTONE_GEAR = ITEMS.register("redstone_gear",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> EYE_OF_REDSTONE = ITEMS.register("eye_of_redstone",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> LUNAR_REACTIVE_DUST = ITEMS.register("lunar_reactive_dust",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> RED_COAL = ITEMS.register("red_coal",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> MOON_STONE = ITEMS.register("moon_stone",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> DEMON_INGOT = ITEMS.register("demon_ingot",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> ENCHANTED_INGOT = ITEMS.register("enchanted_ingot",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> EVIL_INFUSED_IRON_INGOT = ITEMS.register("evil_infused_iron_ingot",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> DEMON_NUGGET = ITEMS.register("demon_nugget",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> ENCHANTED_NUGGET = ITEMS.register("enchanted_nugget",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> EVIL_INFUSED_IRON_NUGGET = ITEMS.register("evil_infused_iron_nugget",()-> new AUItem(new Item.Properties()));

    public static final DeferredItem<Item> WOODEN_SICKLE = ITEMS.register("wooden_sickle",()-> new ItemSickle(Tiers.WOOD, new Item.Properties()));
    public static final DeferredItem<Item> STONE_SICKLE = ITEMS.register("stone_sickle",()-> new ItemSickle(Tiers.STONE, new Item.Properties()));
    public static final DeferredItem<Item> IRON_SICKLE = ITEMS.register("iron_sickle",()-> new ItemSickle(Tiers.IRON, new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_SICKLE = ITEMS.register("golden_sickle",()-> new ItemSickle(Tiers.GOLD, new Item.Properties()));
    public static final DeferredItem<Item> DIAMOND_SICKLE = ITEMS.register("diamond_sickle",()-> new ItemSickle(Tiers.DIAMOND, new Item.Properties()));
    public static final DeferredItem<Item> NETHERITE_SICKLE = ITEMS.register("netherite_sickle",()-> new ItemSickle(Tiers.NETHERITE, new Item.Properties()));

    public static final DeferredItem<Item> UPGRADE_BASE = ITEMS.register("upgrade_base",()-> new AUItem(new Item.Properties()));
    public static final DeferredItem<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade",()-> new ItemSpeedUpgrade(new Item.Properties(),4));
    public static final DeferredItem<Item> ENCHANTED_SPEED_UPGRADE = ITEMS.register("enchanted_speed_upgrade",()-> new ItemSpeedUpgrade(new Item.Properties(),16));
    public static final DeferredItem<Item> ULTIMATE_SPEED_UPGRADE = ITEMS.register("ultimate_speed_upgrade",()-> new ItemSpeedUpgrade(new Item.Properties(),64));

    public static final DeferredItem<Item> MAGICAL_APPLE = ITEMS.register("magical_apple",()->new ItemMagicalApple(new Item.Properties()));

    public static final DeferredItem<Item> RAINBOW_GENERATOR_BOTTOM = ITEMS.register("rainbow_generator_bottom",()-> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> RAINBOW_GENERATOR_TOP = ITEMS.register("rainbow_generator_top",()-> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BIOME_MARKER = ITEMS.register("biome_marker",()-> new ItemBiomeMarker(new Item.Properties()));
    public static final DeferredItem<Item> COMPOUND_BOW = ITEMS.register("compound_bow",()-> new ItemCompoundBow(new Item.Properties()));
    public static final DeferredItem<Item> KIKOKU = ITEMS.register("kikoku", ItemKikoku::new);

    public static final DeferredItem<Item> FLUID_DROPLET = ITEMS.register("fluid_droplet",()-> new ItemFluidDroplet(new Item.Properties()));
    public static final DeferredItem<Item> ENERGY_DROPLET = ITEMS.register("energy_droplet",()-> new ItemEnergyDroplet(new Item.Properties()));

    public static final DeferredItem<Item> FLAT_ITEM_TRANSFER_NODE = ITEMS.register("flat_item_transfer_node",()-> new ItemFlatTransferNode(new Item.Properties(), FlatTransferNodeHandler.Type.ITEM));
    public static final DeferredItem<Item> FLAT_FLUID_TRANSFER_NODE = ITEMS.register("flat_fluid_transfer_node",()-> new ItemFlatTransferNode(new Item.Properties(), FlatTransferNodeHandler.Type.FLUID));

    public static final DeferredItem<Item> ITEM_FILTER = ITEMS.register("item_filter",()-> new ItemFilterItem(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_FILTER = ITEMS.register("fluid_filter",()-> new ItemFilterFluid(new Item.Properties()));

    public static final DeferredItem<Item> POWER_MANAGER = ITEMS.register("power_manager",()-> new ItemPowerManager(new Item.Properties()));

    public static final DeferredItem<Item> DROP_OF_EVIL = ITEMS.register("drop_of_evil",()-> new ItemDropOfEvil(new Item.Properties()));

    public static final DeferredItem<Item> UNACTIVATED_DIVISION_SIGIL = ITEMS.register("unactivated_division_sigil",()-> new ItemDivisionSigilUnactivated(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DIVISION_SIGIL = ITEMS.register("division_sigil",()-> new ItemDivisionSigil(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> PSEUDO_INVERSION_SIGIL = ITEMS.register("pseudo_inversion_sigil",()-> new ItemPseudoInversionSigil(new Item.Properties().stacksTo(1)));

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerItem(Capabilities.EnergyStorage.ITEM, (it,v)-> new ItemLuxSaber.ItemEnergyStorage(it),AUItems.LUX_SABER.get());
        event.registerItem(Capabilities.FluidHandler.ITEM, (it,v)->{
            if (it.has(AUDataComponents.STORED_FLUID))
                return new ItemFluidDroplet.FluidTank(it);
            return null;
        },AUItems.FLUID_DROPLET.get());
        event.registerItem(Capabilities.EnergyStorage.ITEM, (it,v)->{
            if (it.has(AUDataComponents.STORED_ENERGY))
                return new ItemEnergyDroplet.EnergyTank(it);
            return null;
        },AUItems.ENERGY_DROPLET.get());
    }
}

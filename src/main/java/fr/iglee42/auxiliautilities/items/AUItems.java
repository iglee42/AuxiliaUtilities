package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("au");

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
    public static final DeferredItem<Item> RESONATING_REDSTONE_CRYSTAL = ITEMS.register("resonating_redstone_crystal",()-> new AUItem(new Item.Properties()));
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


    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerItem(Capabilities.EnergyStorage.ITEM, (it,v)-> new ItemLuxSaber.ItemEnergyStorage(it),LUX_SABER.get());
    }
}

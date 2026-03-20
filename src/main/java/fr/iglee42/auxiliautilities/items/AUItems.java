package fr.iglee42.auxiliautilities.items;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

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

    public static final DeferredItem<Item> ENDER_SHARD = ITEMS.register("ender_shard",()-> new Item(new Item.Properties().stacksTo(8)));
    public static final DeferredItem<Item> GLASS_CUTTER = ITEMS.register("glass_cutter",()-> new ItemGlassCutter(new Item.Properties().durability(250)));
    public static final DeferredItem<Item> WATERING_CAN = ITEMS.register("watering_can",()-> new ItemWateringCan(new Item.Properties()));
    public static final DeferredItem<Item> UNSTABLE_INGOT = ITEMS.register("unstable_ingot",()-> new ItemUnstableIngot(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> STABLE_UNSTABLE_INGOT = ITEMS.register("stable_unstable_ingot",()-> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STABLE_UNSTABLE_NUGGET = ITEMS.register("stable_unstable_nugget",()-> new Item(new Item.Properties()));
}

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
}

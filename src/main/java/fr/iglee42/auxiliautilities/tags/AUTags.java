package fr.iglee42.auxiliautilities.tags;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AUTags {

    public static class Items {

        public static final TagKey<Item> UNSTABLE_INGOTS = common("ingots/unstable");
        public static final TagKey<Item> UNSTABLE_NUGGETS = common("nuggets/unstable");

        public static final TagKey<Item> DEMON_BLOCKS = common("storage_blocks/demon");
        public static final TagKey<Item> DEMON_INGOTS = common("ingots/demon");
        public static final TagKey<Item> DEMON_NUGGETS = common("nuggets/demon");

        public static final TagKey<Item> ENCHANTED_BLOCKS = common("storage_blocks/enchanted");
        public static final TagKey<Item> ENCHANTED_INGOTS = common("ingots/enchanted");
        public static final TagKey<Item> ENCHANTED_NUGGETS = common("nuggets/enchanted");

        public static final TagKey<Item> EVIL_INFUSED_IRON_BLOCKS = common("storage_blocks/evil_infused_iron");
        public static final TagKey<Item> EVIL_INFUSED_IRON_INGOTS = common("ingots/evil_infused_iron");
        public static final TagKey<Item> EVIL_INFUSED_IRON_NUGGETS = common("nuggets/evil_infused_iron");

        public static final TagKey<Item> SICKLES = mod("sickles");

        private static TagKey<Item> common(String path){
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c",path));
        }

        private static TagKey<Item> mod(String path){
            return TagKey.create(Registries.ITEM, AuxiliaUtilities.id(path));
        }
    }

    public static class Blocks {

        public static final TagKey<Block> DEMON_BLOCKS = common("storage_blocks/demon");
        public static final TagKey<Block> ENCHANTED_BLOCKS = common("storage_blocks/enchanted");
        public static final TagKey<Block> EVIL_INFUSED_IRON_BLOCKS = common("storage_blocks/evil_infused_iron");

        private static TagKey<Block> common(String path){
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c",path));
        }

        private static TagKey<Block> mod(String path){
            return TagKey.create(Registries.BLOCK, AuxiliaUtilities.id(path));
        }
    }

}

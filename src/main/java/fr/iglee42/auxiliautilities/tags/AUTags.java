package fr.iglee42.auxiliautilities.tags;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class AUTags {

    public class Items {


        public static final TagKey<Item> UNSTABLE_INGOTS = common("ingots/unstable");
        public static final TagKey<Item> UNSTABLE_NUGGETS = common("nuggets/unstable");

        private static TagKey<Item> common(String path){
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c",path));
        }

        private static TagKey<Item> mod(String path){
            return TagKey.create(Registries.ITEM, AuxiliaUtilities.id(path));
        }
    }

}

package fr.iglee42.auxiliautilities.blockentities.generators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUGeneratorsDataMaps {


    public static final DataMapType<Item, SimpleMapItem> DEATH_ITEMS = DataMapType.builder(
            AuxiliaUtilities.id("death_generator_items"),
            Registries.ITEM,
            SimpleMapItem.CODEC
    ).synced(SimpleMapItem.CODEC,true).build();

    public static final DataMapType<Item, SimpleMapItem> ENDER_ITEMS = DataMapType.builder(
            AuxiliaUtilities.id("ender_generator_items"),
            Registries.ITEM,
            SimpleMapItem.CODEC
    ).synced(SimpleMapItem.CODEC,true).build();

    public static final DataMapType<Item, SimpleMapItem> EXPLOSIVE_ITEMS = DataMapType.builder(
            AuxiliaUtilities.id("explosive_generator_items"),
            Registries.ITEM,
            SimpleMapItem.CODEC
    ).synced(SimpleMapItem.CODEC,true).build();

    public static final DataMapType<Item, SimpleMapItem> NETHER_STAR_ITEMS = DataMapType.builder(
            AuxiliaUtilities.id("nether_star_generator_items"),
            Registries.ITEM,
            SimpleMapItem.CODEC
    ).synced(SimpleMapItem.CODEC,true).build();

    public static final DataMapType<Item, SimpleMapItem> HALITOSIS_ITEMS = DataMapType.builder(
            AuxiliaUtilities.id("halitosis_generator_items"),
            Registries.ITEM,
            SimpleMapItem.CODEC
    ).synced(SimpleMapItem.CODEC,true).build();

    public static final DataMapType<Item, SimpleMapItem> FROSTY_ITEMS = DataMapType.builder(
            AuxiliaUtilities.id("frosty_generator_items"),
            Registries.ITEM,
            SimpleMapItem.CODEC
    ).synced(SimpleMapItem.CODEC,true).build();


    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(DEATH_ITEMS);
        event.register(ENDER_ITEMS);
        event.register(EXPLOSIVE_ITEMS);
        event.register(NETHER_STAR_ITEMS);
        event.register(HALITOSIS_ITEMS);
        event.register(FROSTY_ITEMS);
    }

    public abstract static class MapItem {
        private final int time;
        private final int energyPerTick;

        public MapItem(int time, int energyPerTick) {
            this.time = time;
            this.energyPerTick = energyPerTick;
        }

        public int time() {
            return time;
        }

        public int energyPerTick() {
            return energyPerTick;
        }
    }

    public static class SimpleMapItem extends MapItem {
        public static final Codec<SimpleMapItem> CODEC = RecordCodecBuilder.create(instance->
                instance.group(
                        ExtraCodecs.POSITIVE_INT.fieldOf("time").forGetter(SimpleMapItem::time),
                        ExtraCodecs.POSITIVE_INT.fieldOf("energy_per_tick").forGetter(SimpleMapItem::energyPerTick)
                ).apply(instance, SimpleMapItem::new)
        );

        public SimpleMapItem(int time, int energyPerTick) {
            super(time, energyPerTick);
        }

    }
}

package fr.iglee42.auxiliautilities.blockentities.terraformer;

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

import java.util.ArrayList;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUTerraformerDataMaps {

    private static final ArrayList<DataMapType<?,?>> DATA_MAP_TYPES = new ArrayList<>();

    public static final DataMapType<Item, TerraformerItem> COOLER = createTerraformer("cooler");
    public static final DataMapType<Item, TerraformerItem> HEATER = createTerraformer("heater");
    public static final DataMapType<Item, TerraformerItem> HUMIDIFIER = createTerraformer("humidifier");
    public static final DataMapType<Item, TerraformerItem> DEHUMIDIFIER = createTerraformer("dehumidifier");
    public static final DataMapType<Item, TerraformerItem> MAGIC_ABSORBER = createTerraformer("magic_absorber");
    public static final DataMapType<Item, TerraformerItem> MAGIC_INFUSER = createTerraformer("magic_infuser");
    public static final DataMapType<Item, TerraformerItem> DESHOSTILIFIER = createTerraformer("deshostilifier");


    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        DATA_MAP_TYPES.forEach(event::register);
    }

    public static DataMapType<Item,TerraformerItem> createTerraformer(String name){
        DataMapType<Item,TerraformerItem> type =  DataMapType.builder(
                AuxiliaUtilities.id(name),
                Registries.ITEM,
                TerraformerItem.CODEC
        ).synced(TerraformerItem.CODEC,true).build();
        DATA_MAP_TYPES.add(type);
        return type;
    }

    public record TerraformerItem(int energyProvided) {
            public static final Codec<TerraformerItem> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            ExtraCodecs.POSITIVE_INT.fieldOf("energyProvided").forGetter(TerraformerItem::energyProvided)
                    ).apply(instance, TerraformerItem::new)
            );
    }

}

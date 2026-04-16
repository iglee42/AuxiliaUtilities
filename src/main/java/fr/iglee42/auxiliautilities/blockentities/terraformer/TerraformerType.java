package fr.iglee42.auxiliautilities.blockentities.terraformer;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.function.IntFunction;

public enum TerraformerType implements StringRepresentable{

    HEATER(new int[] { -11776, -26368, -38400 },AUTerraformerDataMaps.HEATER),
    COOLER(new int[] { -4336395, -7427371, -12820322 },AUTerraformerDataMaps.COOLER),
    HUMIDIFIER(new int[] { -16674305, -15829505, -15048961 },AUTerraformerDataMaps.HUMIDIFIER),
    DEHUMIDIFIER(new int[] { -1571, -2888, -5514 },AUTerraformerDataMaps.DEHUMIDIFIER),
    MAGIC_INFUSER(new int[] { -589601, -2096919, -5111553 },AUTerraformerDataMaps.MAGIC_INFUSER),
    MAGIC_ABSORBER(new int[] { -3473201, 14549162, -393098 },AUTerraformerDataMaps.MAGIC_ABSORBER),
    DESHOSTILIFIER(new int[] { -3542029, -5846326, -8545895, -9599354, -131160 },AUTerraformerDataMaps.DESHOSTILIFIER);

    public static final List<Pair<TerraformerType,TerraformerType>> OPPOSITES = List.of(
            new Pair<>(HEATER,COOLER),
            new Pair<>(HUMIDIFIER,DEHUMIDIFIER),
            new Pair<>(MAGIC_ABSORBER,MAGIC_INFUSER)
    );

    public static final Codec<TerraformerType> CODEC = StringRepresentable.fromEnum(TerraformerType::values);
    public static final IntFunction<TerraformerType> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, TerraformerType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);


    private final int[] colors;
    private final DataMapType<Item,AUTerraformerDataMaps.TerraformerItem> dataMapType;

    TerraformerType(int[] colors, DataMapType<Item, AUTerraformerDataMaps.TerraformerItem> dataMapType) {
        this.colors = colors;
        this.dataMapType = dataMapType;
    }

    public int[] getColors() {
        return colors;
    }

    public DataMapType<Item, AUTerraformerDataMaps.TerraformerItem> getDataMapType() {
        return dataMapType;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}

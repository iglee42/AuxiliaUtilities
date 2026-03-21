package fr.iglee42.auxiliautilities.items;

import com.mojang.serialization.Codec;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AUDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AuxiliaUtilities.MODID);

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Long>> TIME = DATA_COMPONENTS.register("time", ()-> DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> DIMENSION = DATA_COMPONENTS.register("dimension", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> CONTAINER_ID = DATA_COMPONENTS.register("container_id", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> STORED_ENERGY = DATA_COMPONENTS.register("stored_energy", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
}

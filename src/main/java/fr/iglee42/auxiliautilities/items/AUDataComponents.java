package fr.iglee42.auxiliautilities.items;

import com.mojang.serialization.Codec;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.utils.StoredFluidStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class AUDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AuxiliaUtilities.MODID);

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Long>> TIME = DATA_COMPONENTS.register("time", ()-> DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> DIMENSION = DATA_COMPONENTS.register("dimension", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> CONTAINER_ID = DATA_COMPONENTS.register("container_id", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> STORED_ENERGY = DATA_COMPONENTS.register("stored_energy", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Holder<Biome>>> STORED_BIOME = DATA_COMPONENTS.register("stored_biome", ()-> DataComponentType.<Holder<Biome>>builder().persistent(Biome.CODEC).networkSynchronized(ByteBufCodecs.holderRegistry(Registries.BIOME)).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<StoredFluidStack>> STORED_FLUID = DATA_COMPONENTS.register("stored_fluid", ()-> DataComponentType.<StoredFluidStack>builder().persistent(StoredFluidStack.CODEC).networkSynchronized(StoredFluidStack.STREAM_CODEC).build());
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> FILTER_FLAGS = DATA_COMPONENTS.register("filter_flags", ()-> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<List<ItemStack>>> FILTER_ITEMS=
            DATA_COMPONENTS.register("filter_items",
                    () -> DataComponentType.<List<ItemStack>>builder()
                            .persistent(ItemStack.OPTIONAL_CODEC.listOf(ItemFilterFluid.FLUID_SLOTS,ItemFilterFluid.FLUID_SLOTS))
                            .networkSynchronized(ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(ItemFilterFluid.FLUID_SLOTS)))
                            .build());
}

package fr.iglee42.auxiliautilities.utils;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record StoredFluidStack(@NotNull FluidStack stack) {

    public static final Codec<StoredFluidStack> CODEC = FluidStack.OPTIONAL_CODEC.xmap(StoredFluidStack::new, StoredFluidStack::stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, StoredFluidStack> STREAM_CODEC = StreamCodec.composite(
            FluidStack.OPTIONAL_STREAM_CODEC, StoredFluidStack::stack,
            StoredFluidStack::new
    );

    public static final StoredFluidStack EMPTY = new StoredFluidStack(FluidStack.EMPTY);
}

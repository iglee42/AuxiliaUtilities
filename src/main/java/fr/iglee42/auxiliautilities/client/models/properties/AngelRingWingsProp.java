package fr.iglee42.auxiliautilities.client.models.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.items.ItemAngelRing;
import fr.iglee42.auxiliautilities.items.ItemAngelRing.AngelRingWings;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record AngelRingWingsProp() implements SelectItemModelProperty<AngelRingWings> {

    public static final SelectItemModelProperty.Type<AngelRingWingsProp, AngelRingWings> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new AngelRingWingsProp()),
            AngelRingWings.CODEC
    );
    @Override
    public @Nullable ItemAngelRing.AngelRingWings get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return null;
    }

    @Override
    public Codec<AngelRingWings> valueCodec() {
        return AngelRingWings.CODEC;
    }

    @Override
    public Type<? extends SelectItemModelProperty<AngelRingWings>, AngelRingWings> type() {
        return TYPE;
    }
}

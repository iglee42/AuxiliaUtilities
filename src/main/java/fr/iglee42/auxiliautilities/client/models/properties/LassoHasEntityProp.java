package fr.iglee42.auxiliautilities.client.models.properties;

import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class LassoHasEntityProp implements ConditionalItemModelProperty {

    public static final MapCodec<LassoHasEntityProp> MAP_CODEC = MapCodec.unit(new LassoHasEntityProp());

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return stack.has(AUDataComponents.STORED_ENTITY);
    }
}

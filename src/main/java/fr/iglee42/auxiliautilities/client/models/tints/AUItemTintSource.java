package fr.iglee42.auxiliautilities.client.models.tints;

import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record AUItemTintSource(int index) implements ItemTintSource {

    public static final MapCodec<AUItemTintSource> MAP_CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("index").xmap(AUItemTintSource::new,AUItemTintSource::index);

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (!(itemStack.getItem() instanceof AUItemBase it)) return 0xffffff;
        return it.getColor(itemStack,index);
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}

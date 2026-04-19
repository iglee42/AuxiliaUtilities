package fr.iglee42.auxiliautilities.recipes.ingredients;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.auxiliautilities.items.ItemLasso;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.stream.Stream;

public record LassoIngredient(boolean golden, EntityType<?> entity) implements ICustomIngredient {

    public static final MapCodec<LassoIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("golden").forGetter(LassoIngredient::golden),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(LassoIngredient::entity)
            ).apply(instance, LassoIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LassoIngredient> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC.codec());

    @Override
    public boolean test(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemLasso)) return false;
        ItemStack reference = ItemLasso.getForCraft(golden, entity);
        if (!ItemStack.isSameItem(stack, reference)) return false;
        boolean referenceHasEntity = reference.has(AUDataComponents.STORED_ENTITY);
        boolean stackHasEntity = stack.has(AUDataComponents.STORED_ENTITY);
        if (!referenceHasEntity) return true;
        if (!stackHasEntity) return false;
        CompoundTag referenceEntityNbt = reference.get(AUDataComponents.STORED_ENTITY);
        CompoundTag stackEntityNbt = stack.get(AUDataComponents.STORED_ENTITY);
        if (referenceEntityNbt == null) return true;
        if (stackEntityNbt == null) return false;
        if (!referenceEntityNbt.contains("EntityId")) return true;
        if (!stackEntityNbt.contains("EntityId")) return false;
        String referenceEntityId = referenceEntityNbt.getStringOr("EntityId","");
        String stackEntityId = stackEntityNbt.getStringOr("EntityId","");
        return referenceEntityId.equals(stackEntityId);
    }

    @Override
    public Stream<Holder<Item>> items() {
        return Stream.of(ItemLasso.getForCraft(golden,entity).getItemHolder());
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return AUIngredients.LASSO.get();
    }
}

package fr.iglee42.auxiliautilities.jei.subtypes;

import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LassoSubtype implements ISubtypeInterpreter<ItemStack> {

    public static final LassoSubtype INSTANCE = new LassoSubtype();

    private LassoSubtype() {
    }

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        if (!ingredient.has(AUDataComponents.STORED_ENTITY)) return null;
        CompoundTag stored = ingredient.get(AUDataComponents.STORED_ENTITY);
        if (stored == null || !stored.contains("EntityId")) return null;
        return stored.getString("EntityId");
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        if (!ingredient.has(AUDataComponents.STORED_ENTITY)) return "";
        CompoundTag stored = ingredient.get(AUDataComponents.STORED_ENTITY);
        if (stored == null || !stored.contains("EntityId")) return "";
        return stored.getString("EntityId");
    }

}

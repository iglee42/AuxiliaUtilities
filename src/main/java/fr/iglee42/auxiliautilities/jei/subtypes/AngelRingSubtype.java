package fr.iglee42.auxiliautilities.jei.subtypes;

import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AngelRingSubtype implements ISubtypeInterpreter<ItemStack> {

    public static final AngelRingSubtype INSTANCE = new AngelRingSubtype();

    private AngelRingSubtype() {
    }

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(AUDataComponents.WINGS);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return Optional.ofNullable( ingredient.get(AUDataComponents.WINGS)).map(StringRepresentable::getSerializedName).orElse("");
    }
}

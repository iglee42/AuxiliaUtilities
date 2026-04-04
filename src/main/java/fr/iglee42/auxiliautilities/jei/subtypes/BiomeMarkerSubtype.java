package fr.iglee42.auxiliautilities.jei.subtypes;

import fr.iglee42.auxiliautilities.items.AUDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BiomeMarkerSubtype implements ISubtypeInterpreter<ItemStack> {

    public static final BiomeMarkerSubtype INSTANCE = new BiomeMarkerSubtype();

    private BiomeMarkerSubtype() {
    }

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(AUDataComponents.STORED_BIOME);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return Optional.ofNullable(ingredient.get(AUDataComponents.STORED_BIOME)).map(biomeHolder -> biomeHolder.unwrapKey().map(key -> key.location().toString()).orElse("")).orElse("");
    }

}

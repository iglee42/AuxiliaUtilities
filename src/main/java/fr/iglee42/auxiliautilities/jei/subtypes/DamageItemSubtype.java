package fr.iglee42.auxiliautilities.jei.subtypes;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DamageItemSubtype implements ISubtypeInterpreter<ItemStack> {

    public static final DamageItemSubtype INSTANCE = new DamageItemSubtype();

    private DamageItemSubtype() {
    }

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        if (context.equals(UidContext.Recipe)) return null;
        return ingredient.getDamageValue();
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        if (context.equals(UidContext.Recipe)) return "0";
        return ingredient.getDamageValue() + "";
    }
}

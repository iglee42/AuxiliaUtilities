package fr.iglee42.auxiliautilities.jei.subtypes;

import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LuxSaberSubtype implements ISubtypeInterpreter<ItemStack> {

    public static final LuxSaberSubtype INSTANCE = new LuxSaberSubtype();

    private LuxSaberSubtype() {
    }

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        DyeColor color = ingredient.get(DataComponents.BASE_COLOR);
        int energy = ingredient.getOrDefault(AUDataComponents.STORED_ENERGY,0);
        if (color == null) {
            color = DyeColor.WHITE;
        }
        if (context.equals(UidContext.Recipe)) return new LuxSaberData(DyeColor.WHITE,0);
        return new LuxSaberData(color, energy);
    }

    private record LuxSaberData(DyeColor color, int damage) {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof LuxSaberData(DyeColor color1, int damage1) && color1 == color && damage1 == damage;
        }

        @Override
        public @NotNull String toString() {
            return "[color=" + color.getName() + ",damage=" + damage + "]";
        }
    }
}

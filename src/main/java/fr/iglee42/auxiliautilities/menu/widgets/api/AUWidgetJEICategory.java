package fr.iglee42.auxiliautilities.menu.widgets.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public interface AUWidgetJEICategory {
    @OnlyIn(Dist.CLIENT)
    @Nullable
    ResourceLocation categoryId();

}

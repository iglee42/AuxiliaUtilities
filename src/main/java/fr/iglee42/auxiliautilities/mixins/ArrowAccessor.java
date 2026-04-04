package fr.iglee42.auxiliautilities.mixins;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractArrow.class)
public interface ArrowAccessor {

    @Accessor("inGround")
    boolean isInGround();
}

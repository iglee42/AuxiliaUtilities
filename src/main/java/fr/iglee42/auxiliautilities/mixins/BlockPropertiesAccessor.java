package fr.iglee42.auxiliautilities.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.Properties.class)
public interface BlockPropertiesAccessor {

    @Accessor("destroyTime")
    float getDestroyTime();

    @Accessor("explosionResistance")
    float getExplosionResistance();
}

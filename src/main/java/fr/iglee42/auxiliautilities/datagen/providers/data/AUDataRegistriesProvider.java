package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.items.ItemUnstableIngot;
import fr.iglee42.auxiliautilities.potions.effects.DoomEffect;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;

public class AUDataRegistriesProvider {

    public static RegistrySetBuilder build(){

        RegistrySetBuilder builder = new RegistrySetBuilder();


        builder.add(Registries.DAMAGE_TYPE,bootstrap-> {
            bootstrap.register(DoomEffect.DOOM_DAMAGE,
                    new DamageType(DoomEffect.DOOM_DAMAGE.location().getPath(),0.0f));
            bootstrap.register(ItemUnstableIngot.UNSTABLE_DAMAGE,
                    new DamageType(ItemUnstableIngot.UNSTABLE_DAMAGE.location().getPath(),0.0f));
        });

        return builder;
    }

}

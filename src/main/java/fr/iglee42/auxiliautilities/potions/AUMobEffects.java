package fr.iglee42.auxiliautilities.potions;

import fr.iglee42.auxiliautilities.potions.effects.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AUMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, "au");

    public static final Holder<MobEffect> DOOM = MOB_EFFECTS.register("doom", DoomEffect::new);
    public static final Holder<MobEffect> FIZZY_LIFTING = MOB_EFFECTS.register("fizzy_lifting", FizzyLiftingEffect::new);
    public static final Holder<MobEffect> LOVE = MOB_EFFECTS.register("love", LoveEffect::new);
    public static final Holder<MobEffect> GRAVITY = MOB_EFFECTS.register("gravity", GravityEffect::new);
    public static final Holder<MobEffect> SECOND_CHANCE = MOB_EFFECTS.register("second_chance", SecondChanceEffect::new);
    public static final Holder<MobEffect> PURGING = MOB_EFFECTS.register("purging", PurgeEffect::new);
    public static final Holder<MobEffect> GREEK_FIRE = MOB_EFFECTS.register("greek_fire", GreekFireEffect::new);
    public static final Holder<MobEffect> RELAPSE = MOB_EFFECTS.register("relapse", RelapseMobEffect::new);
}

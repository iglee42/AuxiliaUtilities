package fr.iglee42.auxiliautilities.potions;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AUPotions {

    public static final Map<String,String> TRANSLATIONS = new HashMap<>();

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, AuxiliaUtilities.MODID);

    public static final Holder<Potion> DOOM = register("doom",registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.DOOM, 1200)));
    public static final Holder<Potion> FIZZY_LIFTING = register("fizzy_lifting",registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.FIZZY_LIFTING, 600)));
    public static final Holder<Potion> LOVE = register("love",registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.LOVE, 1)));
    public static final Holder<Potion> GRAVITY = register("gravity",registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.GRAVITY, 1200)));
    public static final Holder<Potion> LONG_STRENGTH = POTIONS.register("long_gravity", ()->new Potion("gravity", new MobEffectInstance(AUMobEffects.GRAVITY, 9600)));
    public static final Holder<Potion> SECOND_CHANCE = register("second_chance",registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.SECOND_CHANCE, 2400)));
    public static final Holder<Potion> PURGING = register("purging", registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.PURGING, 1)));
    public static final Holder<Potion> GREEK_FIRE = register("greek_fire", registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.GREEK_FIRE, 2400)));
    public static final Holder<Potion> RELAPSE = register("relapse", registryName-> new Potion(registryName, new MobEffectInstance(AUMobEffects.RELAPSE, 9600)));

    public static final Holder<Potion> OILY = POTIONS.register("oily", ()-> new Potion());



    public static Holder<Potion> register(String name, Function<String,Potion> supplier){
        TRANSLATIONS.put(name, ModsUtils.getUpperName(name,"_"));
        return POTIONS.register(name, id->supplier.apply(id.getPath()));
    }

}

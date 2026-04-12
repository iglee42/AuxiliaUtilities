package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AUSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, AuxiliaUtilities.MODID);

    public static DeferredHolder<SoundEvent,SoundEvent> CREEPY_LAUGH = registerSoundEvent("creepy_laugh");

    private static DeferredHolder<SoundEvent,SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, name)));
    }
}
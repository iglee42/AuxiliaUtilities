package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.potions.effects.DoomEffect;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AUDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public AUDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registries, AuxiliaUtilities.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(DoomEffect.DOOM_DAMAGE.location());
        tag(DamageTypeTags.BYPASSES_INVULNERABILITY).addOptional(DoomEffect.DOOM_DAMAGE.location());
        tag(DamageTypeTags.BYPASSES_RESISTANCE).addOptional(DoomEffect.DOOM_DAMAGE.location());
        tag(DamageTypeTags.NO_KNOCKBACK).addOptional(DoomEffect.DOOM_DAMAGE.location());
    }
}

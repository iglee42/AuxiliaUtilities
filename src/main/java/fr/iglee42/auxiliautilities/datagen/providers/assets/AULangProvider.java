package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.potions.AUPotions;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class AULangProvider extends LanguageProvider {
    public AULangProvider(PackOutput output) {
        super(output, AuxiliaUtilities.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (AULang lang : AULang.values()) {
            add(lang.getKey(),lang.getDefaultTranslation());
        }

        AUPotions.TRANSLATIONS.forEach((key,value)->{
            add("item.minecraft.potion.effect."+key,"Potion of "+value);
            add("item.minecraft.splash_potion.effect."+key,"Splash Potion of "+value);
            add("item.minecraft.lingering_potion.effect."+key,"Lingering Potion of "+value);
            add("item.minecraft.tipped_arrow.effect."+key,"Arrow of "+value);
        });
    }
}

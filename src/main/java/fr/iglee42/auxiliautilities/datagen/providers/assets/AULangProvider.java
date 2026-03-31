package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.CompressedBlockSet;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.potions.AUPotions;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class AULangProvider extends LanguageProvider {
    private static final String[] COMPRESSED_PREFIX= {"", "Double ", "Triple ", "Quadruple ", "Quintuple ", "Sextuple ", "Septuple ", "Octuple ", "Nonuple ", "Decuple "};

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

        add("item.minecraft.potion.effect.oily","Oily Potion");
        add("item.minecraft.splash_potion.effect.oily","Oily Splash Potion");
        add("item.minecraft.lingering_potion.effect.oily","Oily Lingering Potion");

        for (DyeColor color : DyeColor.values()) {
            add(AULang.LUX_SABER.getKey() + "."+color.getSerializedName(), ModsUtils.getUpperName(color.getSerializedName(),"_") + " Lux Saber");
        }

        for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS) {
            add("block." + AuxiliaUtilities.MODID + "." + set.getName(), ModsUtils.getUpperName(set.getName(), "_"));
            add("block." + AuxiliaUtilities.MODID + "." + set.getName() + "_slab", ModsUtils.getUpperName(set.getName(), "_") + " Slab");
            add("block." + AuxiliaUtilities.MODID + "." + set.getName() + "_stairs", ModsUtils.getUpperName(set.getName(), "_") + " Stairs");
            add("block." + AuxiliaUtilities.MODID + "." + set.getName() + "_wall", ModsUtils.getUpperName(set.getName(), "_") + " Wall");
        }

        for (CompressedBlockSet set : CompressedBlockSet.ALL_SETS) {
            for (int i = 1; i <= set.getMaxTier(); i++) {
                add("block." + AuxiliaUtilities.MODID + ".compressed_" + set.getName() + "_" + i, COMPRESSED_PREFIX[i - 1] + "Compressed " +  ModsUtils.getUpperName(set.getName(), "_"));
            }
        }
    }
}

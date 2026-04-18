package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class AUCuriosProvider extends CuriosDataProvider {
    public AUCuriosProvider(PackOutput output,  CompletableFuture<HolderLookup.Provider> registries) {
        super(AuxiliaUtilities.MODID, output, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries) {
        this.createSlot("ring").size(1);
        this.createEntities("entities").addSlots("ring").addPlayer();
    }
}

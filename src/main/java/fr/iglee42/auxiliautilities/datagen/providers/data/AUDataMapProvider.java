package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class AUDataMapProvider extends DataMapProvider {

    public AUDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {

        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(AUBlocks.MAGICAL_WOOD.getId(),new FurnaceFuel(300),false)
                .add(AUBlocks.DIAGONAL_WOOD.getId(),new FurnaceFuel(300),false);
    }
}

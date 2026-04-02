package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.blockentities.generators.*;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
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

        for (BEGenDeath.DefaultDeathGeneratorItems item : BEGenDeath.DefaultDeathGeneratorItems.values()) {
            builder(AUGeneratorsDataMaps.DEATH_ITEMS)
                    .add(item.getItem().asItem().builtInRegistryHolder(),new AUGeneratorsDataMaps.SimpleMapItem(item.getTime(), item.getEnergyPerTick()),false);
        }

        for (BEGenEnder.DefaultEnderGeneratorItems item : BEGenEnder.DefaultEnderGeneratorItems.values()) {
            if (item.getItem() == null){
                builder(AUGeneratorsDataMaps.ENDER_ITEMS)
                        .add(item.getTag(),new AUGeneratorsDataMaps.SimpleMapItem(item.getProgress(), item.getEnergyPerProgress()),false);
            } else {
                builder(AUGeneratorsDataMaps.ENDER_ITEMS)
                        .add(item.getItem().asItem().builtInRegistryHolder(),new AUGeneratorsDataMaps.SimpleMapItem(item.getProgress(), item.getEnergyPerProgress()),false);
            }
        }

        for (BEGenExplosive.DefaultExplosiveGeneratorItems item : BEGenExplosive.DefaultExplosiveGeneratorItems.values()) {
            builder(AUGeneratorsDataMaps.EXPLOSIVE_ITEMS)
                    .add(item.getItem().asItem().builtInRegistryHolder(),new AUGeneratorsDataMaps.SimpleMapItem(item.getTime(), item.getEnergyPerTick()),false);
        }
        builder(AUGeneratorsDataMaps.NETHER_STAR_ITEMS)
                .add(Tags.Items.NETHER_STARS,new AUGeneratorsDataMaps.SimpleMapItem(2400,4000),false);

        builder(AUGeneratorsDataMaps.HALITOSIS_ITEMS)
                .add(Items.DRAGON_BREATH.builtInRegistryHolder(),new AUGeneratorsDataMaps.SimpleMapItem(12000,40),false);

        for (BEGenFrosty.DefaultFrostyGeneratorItems item : BEGenFrosty.DefaultFrostyGeneratorItems.values()) {
            builder(AUGeneratorsDataMaps.FROSTY_ITEMS)
                    .add(item.getItem().asItem().builtInRegistryHolder(),new AUGeneratorsDataMaps.SimpleMapItem(item.getTime(), item.getEnergyPerTick()),false);
        }
    }
}

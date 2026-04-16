package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.blockentities.generators.*;
import fr.iglee42.auxiliautilities.blockentities.terraformer.AUTerraformerDataMaps;
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

        builder(AUTerraformerDataMaps.COOLER)
                .add(Items.ICE.builtInRegistryHolder(), new AUTerraformerDataMaps.TerraformerItem(1),false)
                .add(Items.PACKED_ICE.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(8),false)
                .add(Items.BLUE_ICE.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(16),false)
                .add(Tags.Items.BUCKETS_WATER,new AUTerraformerDataMaps.TerraformerItem(1),false);

        builder(AUTerraformerDataMaps.HEATER)
                .add(Tags.Items.BUCKETS_LAVA,new AUTerraformerDataMaps.TerraformerItem(4),false)
                .add(Tags.Items.RODS_BLAZE,new AUTerraformerDataMaps.TerraformerItem(8),false)
                .add(Items.BLAZE_POWDER.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(1),false);

        builder(AUTerraformerDataMaps.HUMIDIFIER)
                .add(Items.LILY_PAD.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(8),false)
                .add(Tags.Items.CROPS_SUGAR_CANE,new AUTerraformerDataMaps.TerraformerItem(4),false)
                .add(Tags.Items.BUCKETS_WATER,new AUTerraformerDataMaps.TerraformerItem(1),false);

        builder(AUTerraformerDataMaps.DEHUMIDIFIER)
                .add(Tags.Items.SANDS,new AUTerraformerDataMaps.TerraformerItem(1),false)
                .add(Tags.Items.CROPS_CACTUS,new AUTerraformerDataMaps.TerraformerItem(4),false);

        builder(AUTerraformerDataMaps.MAGIC_INFUSER)
                .add(Items.ENCHANTED_BOOK.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(4),false)
                .add(Items.EXPERIENCE_BOTTLE.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(16),false)
                .add(Tags.Items.GEMS_LAPIS,new AUTerraformerDataMaps.TerraformerItem(1),false)
                .add(AUBlocks.MAGICAL_WOOD.getId(),new AUTerraformerDataMaps.TerraformerItem(16),false);

        builder(AUTerraformerDataMaps.MAGIC_ABSORBER)
                .add(Items.BOOK.builtInRegistryHolder(),new AUTerraformerDataMaps.TerraformerItem(2),false)
                .add(Tags.Items.INGOTS_GOLD,new AUTerraformerDataMaps.TerraformerItem(2),false);

        builder(AUTerraformerDataMaps.DESHOSTILIFIER)
                .add(Tags.Items.NETHER_STARS,new AUTerraformerDataMaps.TerraformerItem(16),false);
    }
}

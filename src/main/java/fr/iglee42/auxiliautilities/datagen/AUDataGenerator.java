package fr.iglee42.auxiliautilities.datagen;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.datagen.providers.assets.AUModelsProvider;
//import fr.iglee42.auxiliautilities.datagen.providers.assets.AUItemModelsProvider;
import fr.iglee42.auxiliautilities.datagen.providers.assets.AULangProvider;
import fr.iglee42.auxiliautilities.datagen.providers.data.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUDataGenerator {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent.Client event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        generator.addProvider(true,new AULangProvider(output));
        generator.addProvider(true,new AUModelsProvider(output));
        //generator.addProvider(true,new AUItemModelsProvider(output,fileHelper));
    }

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent.Server event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        event.createDatapackRegistryObjects(AUDataRegistriesProvider.build());

        var blocksTagsProvider = new AUBlocksTagsProvider(output,registries);
        generator.addProvider(true, blocksTagsProvider);
        generator.addProvider(true,new AUItemTagsProvider(output,registries,blocksTagsProvider.contentsGetter()));
        generator.addProvider(true, new AULootTablesProvider(output,registries));
        generator.addProvider(true, new AURecipesProvider.Runner(output,registries));
        generator.addProvider(true, new AUDamageTypeTagsProvider(output,registries));
        generator.addProvider(true, new AUDataMapProvider(output,registries));
        generator.addProvider(true, new AULootModifiersProvider(output,registries));
        generator.addProvider(true, new AUCuriosProvider(output,registries));
    }
}

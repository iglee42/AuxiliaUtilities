package fr.iglee42.auxiliautilities.datagen;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.datagen.providers.assets.AUItemModelsProvider;
import fr.iglee42.auxiliautilities.datagen.providers.assets.AULangProvider;
import fr.iglee42.auxiliautilities.datagen.providers.data.AUDamageTypeTagsProvider;
import fr.iglee42.auxiliautilities.datagen.providers.data.AUDataRegistriesProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUDataGenerator {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new AULangProvider(output));
        generator.addProvider(event.includeClient(), new AUItemModelsProvider(output,fileHelper));

        event.createDatapackRegistryObjects(AUDataRegistriesProvider.build());

        generator.addProvider(event.includeServer(), new AUDamageTypeTagsProvider(output,registries,fileHelper));
    }
}

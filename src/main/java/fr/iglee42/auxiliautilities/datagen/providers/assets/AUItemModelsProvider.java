package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.AUItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class AUItemModelsProvider extends ItemModelProvider {
    public AUItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AuxiliaUtilities.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldTool(AUItems.BUILDERS_WAND);
        handheldTool(AUItems.CREATIVE_BUILDERS_WAND);
        handheldTool(AUItems.DESTRUCTION_WAND);
        handheldTool(AUItems.CREATIVE_DESTRUCTION_WAND);


    }

    private void handheldTool(DeferredItem<? extends Item> item){
        getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/tools/"+item.getId().getPath()));
    }

}

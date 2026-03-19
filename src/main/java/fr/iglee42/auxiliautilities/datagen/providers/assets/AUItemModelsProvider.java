package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.items.AUItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
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
        handheldTool(AUItems.GLASS_CUTTER);
        handheldTool(AUItems.WATERING_CAN);

        makeEnderShards();
    }

    private void handheldTool(DeferredItem<? extends Item> item){
        getBuilder(item.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/tools/"+item.getId().getPath()));
    }

    private void makeEnderShards(){
        ItemModelBuilder builder = getBuilder(AUItems.ENDER_SHARD.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/ender_shards/ender_shard_1"));

        for (int i = 1; i < 9; i++) {
            builder.override()
                    .predicate(AuxiliaUtilities.id("shards"), i)
                    .model(new ModelFile.UncheckedModelFile(AuxiliaUtilities.id("item/ender_shards/ender_shard_"+i)));

            getBuilder("item/ender_shards/ender_shard_"+i)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(AuxiliaUtilities.MODID, "item/ender_shards/ender_shard_"+i));
        }
    }

}

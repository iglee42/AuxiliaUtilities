package fr.iglee42.auxiliautilities.datagen.providers.assets;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AUBlockStatesProvider extends BlockStateProvider {
    public AUBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AuxiliaUtilities.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(AUBlocks.ANGEL_BLOCK.get());
        simpleBlock(AUBlocks.DEMON_BLOCK.get(),models().cubeAll(AUBlocks.DEMON_BLOCK.getId().getPath(), modLoc("block/demon_metal_block")));
        simpleBlock(AUBlocks.ENCHANTED_BLOCK.get(),models().cubeAll(AUBlocks.ENCHANTED_BLOCK.getId().getPath(), modLoc("block/enchanted_metal_block")));
        simpleBlock(AUBlocks.EVIL_INFUSED_IRON_BLOCK.get());
    }
}

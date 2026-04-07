package fr.iglee42.auxiliautilities.blockentities.drums;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BENetheriteDrum extends BEDrum{
    public BENetheriteDrum(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.NETHERITE_DRUM.get(), pos, state, Type.NETHERITE);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,AUBlockEntityTypes.NETHERITE_DRUM.get(), (be,dir)->be.getTank());
    }
}

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
public class BEStoneDrum extends BEDrum{
    public BEStoneDrum(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.STONE_DRUM.get(), pos, state, Type.STONE);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,AUBlockEntityTypes.STONE_DRUM.get(), (be,dir)->be.getTank());
    }
}

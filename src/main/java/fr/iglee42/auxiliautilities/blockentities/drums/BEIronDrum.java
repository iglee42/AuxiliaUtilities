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
public class BEIronDrum extends BEDrum{
    public BEIronDrum(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.IRON_DRUM.get(), pos, state, Type.IRON);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,AUBlockEntityTypes.IRON_DRUM.get(), (be,dir)->be.getTank());
    }
}

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
public class BEDemonicallyGargantuanDrum extends BEDrum{
    public BEDemonicallyGargantuanDrum(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.DEMONICALLY_GARGANTUAN_DRUM.get(), pos, state, Type.DEMONICALLY_GARGANTUAN);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,AUBlockEntityTypes.DEMONICALLY_GARGANTUAN_DRUM.get(), (be,dir)->be.getTank());
    }
}

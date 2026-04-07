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
public class BEReinforcedLargeDrum extends BEDrum{
    public BEReinforcedLargeDrum(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.REINFORCED_LARGE_DRUM.get(), pos, state, Type.REINFORCED_LARGE);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,AUBlockEntityTypes.REINFORCED_LARGE_DRUM.get(), (be,dir)->be.getTank());
    }
}

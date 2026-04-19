package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BESolarPanel extends AUGPGeneratorBlockEntity {
    public BESolarPanel(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.SOLAR_PANEL.get(), pos, state);
    }

    @Override
    public int getGPGeneration() {

        if (level == null) return 0;
        boolean isDayTime = level.isBrightOutside();
        boolean hasSkyAccess = level.canSeeSky(getBlockPos().above());
        int gp = hasSkyAccess ? 8 : 0;
        gp *= (isDayTime) ? (level.isRaining() ? 0.75 : 1) : 0;
        return gp;
    }


}

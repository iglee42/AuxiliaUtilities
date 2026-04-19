package fr.iglee42.auxiliautilities.blockentities.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BEManualMill extends AUGPGeneratorBlockEntity {

    private static final double DELTA_OFFSET = Math.PI / 10.0;
    private float animationTime = 0F;
    private float renderOffset = 0F;

    public BEManualMill(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.MANUAL_MILL.get(), pos, state);
    }


    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (animationTime <= 0F) {
            return super.serverTick(level,pos,state);
        }

        float old = animationTime;

        animationTime -= 0.05F;

        if (animationTime < 0F) {
            animationTime = 0F;
        }

        return old != animationTime || super.serverTick(level, pos, state);
    }

    @Override
    protected void clientTick(Level level, BlockPos pos, BlockState state) {

        if (animationTime <= 0F) {
            return;
        }

        renderOffset += (float) (animationTime * DELTA_OFFSET);
    }

    @Override
    public int getGPGeneration() {
        return (int) (Math.min(animationTime,0.5) * 30);
    }

    public void triggerAnimation() {
        this.animationTime = 1F;
    }

    public float getRenderOffset() {
        return renderOffset;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    @Override
    protected void save(ValueOutput output, boolean forClient) {
        super.save(output, forClient);
        if (forClient){
            output.putFloat("animationTime",animationTime);
        }
    }

    @Override
    protected void load(ValueInput input) {
        super.load(input);
        animationTime = input.getFloatOr("animationTime",0);
    }
}

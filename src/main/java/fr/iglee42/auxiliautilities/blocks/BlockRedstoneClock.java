package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

public class BlockRedstoneClock extends Block implements AUBlockBase {

    public static final EnumProperty<PowerState> POWER_STATE = EnumProperty.create("power_state", PowerState.class);

    boolean canProvidePower = true;
    boolean changing = false;

    public static final int POWER_TIME = 2;

    public static final int TICK_TIME = 20;


    public BlockRedstoneClock(Properties props) {
        super(props);
        registerDefaultState(defaultBlockState().setValue(POWER_STATE, PowerState.ENABLED_NOT_POWERED));

        for (PowerState state : PowerState.values())
            state.state = defaultBlockState().setValue(POWER_STATE, state);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return (this.canProvidePower &&
                state.getValue(POWER_STATE) == PowerState.ENABLED_POWERED)
                ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {

        if (this.changing || level.isClientSide)
            return;

        boolean powered = level.hasNeighborSignal( pos);
        PowerState value = state.getValue(POWER_STATE);

        this.changing = true;

        if (powered && value != PowerState.DISABLED) {

            level.setBlock(pos, PowerState.DISABLED.state, 3);

        }
        else if (!powered && value == PowerState.DISABLED) {

            int l = (int) (level.getGameTime() % 20L);

            if (l < 2) {

                level.setBlock(pos, PowerState.ENABLED_POWERED.state, 3);
                level.scheduleTick(pos, this, 2 - l);

            }
            else {

                level.setBlock(pos, PowerState.ENABLED_NOT_POWERED.state, 3);
                level.scheduleTick(pos, this, 20 - l);

            }
        }

        this.changing = false;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        PowerState powerState = state.getValue(POWER_STATE);

        if (powerState == PowerState.DISABLED)
            return;

        int l = (int) (level.getGameTime() % 20L);

        this.changing = true;

        if (l < 2) {
            level.setBlock(pos, PowerState.ENABLED_POWERED.state, 1);
            level.scheduleTick(pos, this, 2 - l);
        } else {
            level.setBlock(pos, PowerState.ENABLED_NOT_POWERED.state, 1);

            if (level.hasNeighborSignal( pos)) {
                level.setBlock(pos, PowerState.DISABLED.state, 3);
            } else {
                level.scheduleTick(pos, this, 20 - l);
            }
        }

        this.changing = false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWER_STATE));
    }

    public enum PowerState implements StringRepresentable {
        ENABLED_NOT_POWERED, ENABLED_POWERED, DISABLED;

        public BlockState state;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}

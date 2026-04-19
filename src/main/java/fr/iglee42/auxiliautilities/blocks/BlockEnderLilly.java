package fr.iglee42.auxiliautilities.blocks;

import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class BlockEnderLilly extends VegetationBlock implements AUBlockBase {
    public static final MapCodec<BlockEnderLilly> CODEC = simpleCodec(BlockEnderLilly::new);

        public static final int MAX_AGE = 7;

        public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0, 0.0, 6.0, 10.0, 4.0, 10.0),
            Block.box(5.0, 0.0, 5.0, 11.0, 7.0, 11.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 7.0, 12.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 11.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0)
    };

    public BlockEnderLilly(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), Integer.valueOf(0)));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter p_52303_, BlockPos p_52304_) {
        return state.is(Tags.Blocks.END_STONES);
    }

    @Override
    protected VoxelShape getShape(BlockState p_52297_, BlockGetter p_52298_, BlockPos p_52299_, CollisionContext p_52300_) {
        return SHAPE_BY_AGE[getAge(p_52297_)];
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return MAX_AGE;
    }

    public int getAge(BlockState p_52306_) {
        return p_52306_.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int p_52290_) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(p_52290_));
    }

    public final boolean isMaxAge(BlockState p_52308_) {
        return this.getAge(p_52308_) >= this.getMaxAge();
    }


    @Override
    protected boolean isRandomlyTicking(BlockState p_52288_) {
        return !this.isMaxAge(p_52288_);
    }

    @Override
    protected void randomTick(BlockState p_221050_, ServerLevel p_221051_, BlockPos p_221052_, RandomSource p_221053_) {
        if (!p_221051_.isAreaLoaded(p_221052_, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (p_221051_.getRawBrightness(p_221052_, 0) >= 9) {
            int i = this.getAge(p_221050_);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(p_221050_, p_221051_, p_221052_);
                if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(p_221051_, p_221052_, p_221050_, p_221053_.nextInt((int)(25.0F / f) + 1) == 0)) {
                    p_221051_.setBlock(p_221052_, this.getStateForAge(i + 1), 2);
                    net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(p_221051_, p_221052_, p_221050_);
                }
            }
        }
    }

    protected static float getGrowthSpeed(BlockState blockState, BlockGetter p_52274_, BlockPos p_52275_) {
        Block p_52273_ = blockState.getBlock();
        float f = 1.0F;
        BlockPos blockpos = p_52275_.below();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                float f1 = 0.0F;
                BlockState blockstate = p_52274_.getBlockState(blockpos.offset(i, 0, j));
                TriState soilDecision = blockstate.canSustainPlant(p_52274_, blockpos.offset(i, 0, j), net.minecraft.core.Direction.UP, blockState);
                if (soilDecision.isDefault() ? blockstate.getBlock() instanceof net.minecraft.world.level.block.FarmBlock : soilDecision.isTrue()) {
                    f1 = 1.0F;
                    if (blockstate.isFertile(p_52274_, p_52275_.offset(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = p_52275_.north();
        BlockPos blockpos2 = p_52275_.south();
        BlockPos blockpos3 = p_52275_.west();
        BlockPos blockpos4 = p_52275_.east();
        boolean flag = p_52274_.getBlockState(blockpos3).is(p_52273_) || p_52274_.getBlockState(blockpos4).is(p_52273_);
        boolean flag1 = p_52274_.getBlockState(blockpos1).is(p_52273_) || p_52274_.getBlockState(blockpos2).is(p_52273_);
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = p_52274_.getBlockState(blockpos3.north()).is(p_52273_)
                    || p_52274_.getBlockState(blockpos4.north()).is(p_52273_)
                    || p_52274_.getBlockState(blockpos4.south()).is(p_52273_)
                    || p_52274_.getBlockState(blockpos3.south()).is(p_52273_);
            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }


    @Override
    public MapCodec<BlockEnderLilly> codec() {
        return CODEC;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos p_52279_, Entity entity,InsideBlockEffectApplier applier) {
        if (!level.isClientSide && state.getValue(AGE) > 1 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));
            if (level instanceof ServerLevel serverLevel) {
                Vec3 vec3 = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
                if (vec3.horizontalDistanceSqr() > 0.0) {
                    double d0 = Math.abs(vec3.x());
                    double d1 = Math.abs(vec3.z());
                    if (d0 >= 0.003F || d1 >= 0.003F) {
                        entity.hurtServer(serverLevel,level.damageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
        super.entityInside(state, level, p_52279_, entity,applier);
    }


    @Override
    protected boolean canSurvive(BlockState p_52282_, LevelReader p_52283_, BlockPos p_52284_) {
        TriState soilDecision = p_52283_.getBlockState(p_52284_.below()).canSustainPlant(p_52283_, p_52284_.below(), net.minecraft.core.Direction.UP, p_52282_);
        if (!soilDecision.isDefault()) return soilDecision.isTrue();
        return super.canSurvive(p_52282_, p_52283_, p_52284_);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide){
            if (isMaxAge(state)) {
                List<ItemStack> drops = getDrops(state, (ServerLevel) level,pos,null,player,player.getMainHandItem());
                drops.removeIf(stack->stack.is(AUBlocks.ENDER_LILLY.asItem()));
                drops.forEach(stack->popResource(level,pos,stack));
                level.setBlock(pos, getStateForAge(0), 2);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, level, pos, player, result);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        return new ItemStack(AUBlocks.ENDER_LILLY.asItem());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52286_) {
        p_52286_.add(AGE);
    }


}

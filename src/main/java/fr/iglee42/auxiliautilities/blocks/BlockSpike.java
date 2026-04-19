package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import org.jetbrains.annotations.Nullable;


public class BlockSpike extends AUBlock {

    public static final ResourceKey<DamageType> SPIKE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, AuxiliaUtilities.id("spike"));
    public static final ResourceKey<DamageType> CREATIVE_SPIKE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, AuxiliaUtilities.id("creative_spike"));

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    private final SpikeType type;

    public BlockSpike(Properties props, SpikeType type) {
        super(props);
        this.type = type;
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return super.rotate(state, rotation).setValue(FACING,rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return super.mirror(state, mirror).setValue(FACING,mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier applier) {
        if (level.isClientSide) return;
        if (isIgnored(entity)) return;
        if (entity instanceof LivingEntity) type.hurtEntity(level, pos, state, entity);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return Shapes.box(0.05,0.0,0.05,0.95,0.95,0.95);
    }

    public boolean isIgnored(Entity entity) {
        return type.isIgnored(entity);
    }

    public static enum SpikeType {
        WOOD(1.0F){
            @Override
            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
                if (entity instanceof LivingEntity lv && lv.getHealth() <= 2.0F)
                    return;
                super.hurtEntity(level, pos, state, entity);
            }
        },
        STONE(1.0F){
            @Override
            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
                if (entity instanceof LivingEntity lv && lv.getHealth() <= 1.0F)
                    return;
                super.hurtEntity(level, pos, state, entity);
            }
        },
        COPPER(3.0F),
        IRON(5.0F),
        GOLDEN(6.0F){
            @Override
            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
                entity.hurt(level.damageSources().source(SPIKE_DAMAGE),this.amount);
            }
        },
        DIAMOND(10.0F){
            @Override
            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
                if (!(entity instanceof LivingEntity lv)){
                    super.hurtEntity(level, pos, state, entity);
                    return;
                }
                float min = Math.min(this.amount, lv.getHealth() - 0.001F);
                entity.hurt(level.damageSources().source(SPIKE_DAMAGE),min);
                if (lv.getHealth() <= 0.01F){
                    if (level instanceof ServerLevel serverLevel){
                        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
                        entity.hurt(level.damageSources().source(SPIKE_DAMAGE,fakePlayer),this.amount * 1000);
                    } else {
                        super.hurtEntity(level, pos, state, entity);
                    }
                }
            }
        },
        NETHERITE(15.0F){
            @Override
            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
                if (!(entity instanceof LivingEntity lv)){
                    super.hurtEntity(level, pos, state, entity);
                    return;
                }
                float min = Math.min(this.amount, lv.getHealth() - 0.001F);
                entity.hurt(level.damageSources().source(SPIKE_DAMAGE),min);
                if (lv.getHealth() <= 0.01F){
                    if (level instanceof ServerLevel serverLevel){
                        FakePlayer fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
                        lv.igniteForTicks(100);
                        entity.hurt(level.damageSources().source(SPIKE_DAMAGE,fakePlayer),this.amount * 1000);
                    } else {
                        super.hurtEntity(level, pos, state, entity);
                    }
                }
            }
        },
        CREATIVE(Float.MAX_VALUE){
            @Override
            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity) {
                entity.hurt(level.damageSources().source(CREATIVE_SPIKE_DAMAGE),this.amount);
            }

            @Override
            public boolean isIgnored(Entity entity) {
                return false;
            }
        }
        ;

        final float amount;

            SpikeType(float amount) {
                this.amount = amount;
            }

            public void hurtEntity(Level level, BlockPos pos, BlockState state, Entity entity){
                if (entity instanceof LivingEntity lv && lv.getHealth() <= amount){
                    lv.skipDropExperience();
                }
                entity.hurt(level.damageSources().source(SPIKE_DAMAGE),amount);
            }

        public boolean isIgnored(Entity entity) {
            return (entity instanceof ItemEntity || entity instanceof ExperienceOrb);
        }
    }
}

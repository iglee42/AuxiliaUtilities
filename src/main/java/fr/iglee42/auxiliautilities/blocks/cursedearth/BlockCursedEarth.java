package fr.iglee42.auxiliautilities.blocks.cursedearth;

import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class BlockCursedEarth extends AUBlock {

    public static final int MAX_DECAY = 15;
    public static final IntegerProperty DECAY = IntegerProperty.create("decay", 0, MAX_DECAY);

    public BlockCursedEarth() {
        super(Properties.of()
                .mapColor(MapColor.DIRT)
                .strength(0.6F)
                .randomTicks()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.NORMAL));

        registerDefaultState(stateDefinition.any().setValue(DECAY, 0));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState p_60569_, boolean p_60570_) {
        super.onPlace(state, level, pos, p_60569_, p_60570_);
        level.scheduleTick(pos,state.getBlock(),20);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DECAY);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        performTick(level, pos, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        performTick(level, pos, rand);
    }


    private static boolean canSurvive(BlockState state, Level level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = level.getBlockState(blockpos);
        if (level.canSeeSky(blockpos) && level.isDay()) return false;
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(
                    level, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(level, blockpos)
            );
            return i < level.getMaxLightLevel();
        }
    }

    private void performTick(ServerLevel level, BlockPos pos, RandomSource rand) {

        boolean seeSun = !canSurvive(level.getBlockState(pos), level, pos);

        level.scheduleTick(pos,level.getBlockState(pos).getBlock(),20);
        if (seeSun) {
            if (level.getBlockState(pos.above()).canBeReplaced()) level.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
            if (rand.nextInt(5) == 0) {
                level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
            return;
        }

        boolean spread = false;

        for (int i = 0; i < 4; i++) {

            BlockPos target = pos.offset(
                    rand.nextInt(3) - 1,
                    rand.nextInt(1),
                    rand.nextInt(3) - 1
            );

            spread |= trySpread(level, pos, target, rand);
        }

        if (!spread && rand.nextInt(8) == 0) {
            spawnMob(level, pos);
        }

    }

    private boolean trySpread(ServerLevel level, BlockPos origin, BlockPos pos, RandomSource rand) {

        if (!level.isLoaded(pos)) return false;

        BlockState state = level.getBlockState(pos);

        if (state.getBlock() != Blocks.GRASS_BLOCK && state.getBlock() != Blocks.DIRT)
            return false;

        if (level.getBlockState(origin.above()).is(BlockTags.FIRE)) return false;
        if (level.getBlockState(pos.above()).is(BlockTags.FIRE)) return false;

        int decay = 16;

        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))) {

            BlockState other = level.getBlockState(p);

            if (other.getBlock() == this) {
                decay = Math.min(decay, other.getValue(DECAY) + 1);
            }
        }

        if (rand.nextBoolean())
            decay++;

        if (decay > MAX_DECAY)
            return false;

        level.setBlockAndUpdate(pos, defaultBlockState().setValue(DECAY, decay));
        return true;
    }

    private void spawnMob(ServerLevel level, BlockPos pos) {

        AABB box = new AABB(pos).inflate(7,4,7);

        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, box,
                e -> e.getType().getCategory() == MobCategory.MONSTER);

        if (mobs.size() >= 8)
            return;

        MobSpawnSettings.SpawnerData data = level.getBiome(pos).value().getMobSettings().getMobs(MobCategory.MONSTER)
                .getRandom(level.random)
                .orElse(null);

        if (data == null)
            return;

        Entity entity = data.type.create(level);
        if (!(entity instanceof Monster monster))
            return;

        monster.setPos(
                pos.getX() + 0.5,
                pos.getY() + 1,
                pos.getZ() + 0.5
        );

        if (!monster.checkSpawnRules(level, MobSpawnType.NATURAL)
                || !monster.checkSpawnObstruction(level)) {
            monster.discard();
            return;
        }

        spawnMobAsCursed(monster);
        level.addFreshEntity(monster);
    }

    public static void spawnMobAsCursed(Mob mob) {

        CompoundTag tag = mob.getPersistentData();
        tag.putInt("CursedEarth", 10);

        AttributeInstance attack = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attack != null)
            attack.setBaseValue(attack.getBaseValue()*1.5);

        AttributeInstance speed = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null)
            speed.setBaseValue(speed.getBaseValue()*1.2);

        mob.goalSelector.addGoal(0, new CursedGoal(mob, 60));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {

        int particles = 15 - state.getValue(DECAY);

        for (int i=0;i<particles;i++) {

            level.addParticle(
                    ParticleTypes.SMOKE,
                    pos.getX()+rand.nextDouble(),
                    pos.getY()+1.01,
                    pos.getZ()+rand.nextDouble(),
                    0,0,0
            );
        }
    }

    public static void startFastSpread(ServerLevel level, BlockPos origin, int radius) {
        
        level.setBlockAndUpdate(origin, AUBlocks.CURSED_EARTH.get().defaultBlockState().setValue(DECAY, 0));
        for (int i = 1; i <= radius; i++) {
            for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-i,0,-i), origin.offset(i,0,i))) {
                if ((level.getBlockState(pos).is(Blocks.GRASS_BLOCK) || level.getBlockState(pos).is(Blocks.DIRT))) {
                    level.setBlockAndUpdate(pos, AUBlocks.CURSED_EARTH.get().defaultBlockState().setValue(DECAY, 0));
                }
            }
        }


    }

}
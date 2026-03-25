package fr.iglee42.auxiliautilities.blocks.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.BELavaMill;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.BEWaterMill;
import fr.iglee42.auxiliautilities.blocks.AUEntityBlock;
import fr.iglee42.auxiliautilities.blocks.gp.AUGPEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLavaMill extends Block implements AUGPEntityBlock<BELavaMill> {


    public BlockLavaMill(Properties props) {
        super(props);
    }

    @Override
    public @NotNull BlockEntityType<BELavaMill> type() {
        return AUBlockEntityTypes.LAVA_MILL.get();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState p_49849_, @Nullable LivingEntity entity, ItemStack p_49851_) {
        onPlace(level,pos,entity);
        super.setPlacedBy(level, pos, p_49849_, entity, p_49851_);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_60519_) {
        AUEntityBlock.onRemove(state,level,pos,newState);
    }
}

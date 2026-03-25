package fr.iglee42.auxiliautilities.blocks.gp.generators;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.generators.BELunarPanel;
import fr.iglee42.auxiliautilities.blocks.AUEntityBlock;
import fr.iglee42.auxiliautilities.blocks.gp.AUGPEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLunarPanel extends Block implements AUGPEntityBlock<BELunarPanel> {


    public BlockLunarPanel(Properties props) {
        super(props);
    }

    @Override
    public @NotNull BlockEntityType<BELunarPanel> type() {
        return AUBlockEntityTypes.LUNAR_PANEL.get();
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

    @Override
    protected VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Shapes.box(0, 0, 0, 1, 0.25, 1);
    }
}

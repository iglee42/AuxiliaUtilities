package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.BEEnchanter;
import fr.iglee42.auxiliautilities.blockentities.gp.consumers.BEResonator;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import fr.iglee42.auxiliautilities.blocks.gp.AUGPEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEnchanter extends AUBlock implements AUEntityBlock<BEEnchanter> {

    public BlockEnchanter(Properties props) {
        super(props);
    }

    @Override
    public @NotNull BlockEntityType<BEEnchanter> type() {
        return AUBlockEntityTypes.ENCHANTER.get();
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult p_60508_) {
        return openMenu(level,pos,player);
    }

}

package fr.iglee42.auxiliautilities.blocks;

import com.mojang.serialization.MapCodec;
import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.blockentities.terraformer.BETerraformerExtension;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BlockTerraformerExtension extends HorizontalDirectionalBlock implements AUEntityBlock<BETerraformerExtension> {
    private final Supplier<BlockEntityType<BETerraformerExtension>> type;

    public BlockTerraformerExtension(Properties properties, Supplier<BlockEntityType<BETerraformerExtension>> type) {
        super(properties);
        this.type = type;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(BlockFurnace::new);
    }


    @Override
    public @NotNull BlockEntityType<BETerraformerExtension> type() {
        return type.get();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        onPlace(level, pos, entity);
        super.setPlacedBy(level, pos, state, entity, stack);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        AUEntityBlock.onRemove(state, level, pos, newState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return openMenu(level, pos, player);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public Component getTooltip(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag) {
        return AULang.CLIMOGRAPH_TOOLTIP.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        addTooltips(stack, tooltips, context, flag);
        super.appendHoverText(stack, context, tooltips, flag);
    }
}

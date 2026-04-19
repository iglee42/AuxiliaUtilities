package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.blockentities.drums.BEDrum;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.utils.FluidColorHelper;
import fr.iglee42.auxiliautilities.utils.StoredFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BlockDrum<T extends BEDrum> extends AUBlock implements AUEntityBlock<T> {
    private final Supplier<BlockEntityType<T>> type;

    public BlockDrum(Properties properties, Supplier<BlockEntityType<T>> type) {
        super(properties);
        this.type = type;
    }

    @Override
    public @NotNull BlockEntityType<T> type() {
        return type.get();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        onPlace(level, pos, entity);
        super.setPlacedBy(level, pos, state, entity, stack);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        withBlockEntityDo(level,pos, be->{
            if (!be.getTank().isEmpty())
                player.displayClientMessage(be.getTank().getFluid().getHoverName().copy().append(" : ").append(AULang.STORED_FLUID_TOOLTIP.get(formatInt(be.getTank().getFluidAmount()),formatInt(be.getTank().getCapacity()))),true);
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        if (!level.isClientSide){
            if (FluidUtil.interactWithFluidHandler(player,hand,level,pos,hitresult.getDirection())){
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitresult);
    }



    @Override
    public int getColor(BlockState state, BlockAndTintGetter getter, BlockPos pos, int tintIndex) {
        BlockEntity entity = getter.getBlockEntity(pos);
        if (entity instanceof BEDrum drum){
            if (tintIndex == 0 && drum.getTank().getFluidAmount() > 0){
                return FluidColorHelper.getColor(drum.getTank().getFluid());
            }
        }
        return super.getColor(state, getter, pos, tintIndex);
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, Item.TooltipContext ctx, TooltipFlag flag) {
        if (stack.has(AUDataComponents.STORED_FLUID)){
            StoredFluidStack fluid = stack.get(AUDataComponents.STORED_FLUID);
            if (fluid != null && !fluid.stack().isEmpty())
                return List.of(AULang.STORED_FLUID_TOOLTIP_ITEM.get(formatInt(fluid.stack().getAmount()),fluid.stack().getHoverName().getString()));
        }
        return super.getStorageTooltips(stack, ctx, flag);
    }

    @Override
    public int getItemColor(ItemStack stack, int tintIndex) {
        if (stack.has(AUDataComponents.STORED_FLUID)){
            StoredFluidStack fluid = stack.get(AUDataComponents.STORED_FLUID);
            if (fluid != null && !fluid.stack().isEmpty() && tintIndex == 0)
                return FluidColorHelper.getColor(fluid.stack());
        }
        return super.getItemColor(stack, tintIndex);
    }
}

package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.blocks.cursedearth.BlockCursedEarth;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemDropOfEvil extends AUItem{
    public ItemDropOfEvil(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = ctx.getItemInHand();
        if (!level.isClientSide){
            if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)){
                BlockCursedEarth.startFastSpread((ServerLevel) level, pos,2);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(ctx);
    }
}

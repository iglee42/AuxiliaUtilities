package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.items.api.AUBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ItemAngelBlock extends AUBlockItem {


    public ItemAngelBlock(Block block, Properties props) {
        super(block, props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        int x = (int) Math.floor(player.getX());
        int y = (int) Math.floor(player.getY() + player.getEyeHeight());
        int z = (int) Math.floor(player.getZ());
        Vec3 look = player.getLookAngle();
        Direction dir = Direction.getNearest((int) look.x(), (int) look.y(), (int) look.z(),Direction.UP);
        switch (dir){
            case DOWN -> y = (int) (Math.floor(player.getBoundingBox().minY) - 1);
            case UP -> y = (int) (Math.ceil(player.getBoundingBox().maxY) + 1);
            case NORTH -> z = (int) (Math.floor(player.getBoundingBox().minZ ) - 1);
            case SOUTH -> z = (int) (Math.ceil(player.getBoundingBox().maxZ ) + 1);
            case WEST -> x = (int) (Math.floor(player.getBoundingBox().minX ) - 1);
            case EAST -> x = (int) (Math.ceil(player.getBoundingBox().maxX ) + 1);
        }

        BlockPos pos = new BlockPos(x,y,z);
        if (level.getBlockState(pos).canBeReplaced()){
            player.getItemInHand(hand).useOn(new UseOnContext(level,player, InteractionHand.MAIN_HAND, player.getItemInHand(hand), new BlockHitResult(
                    pos.getCenter(),
                    dir,
                    pos,
                    false
            )));
        }
        return InteractionResult.CONSUME;
    }
}

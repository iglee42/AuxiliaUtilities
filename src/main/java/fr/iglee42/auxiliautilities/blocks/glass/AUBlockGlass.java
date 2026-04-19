package fr.iglee42.auxiliautilities.blocks.glass;

import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class AUBlockGlass extends TransparentBlock implements AUBlockBase {
    public AUBlockGlass(Properties props) {
        super(props);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext ctx) {
        if (ctx instanceof EntityCollisionContext entityCtx){
            Entity entity = entityCtx.getEntity();
            if (entity != null && !blockEntity(entity)){
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(p_60572_, p_60573_, p_60574_, ctx);
    }

    protected boolean blockEntity(Entity entity) { return true; };
}

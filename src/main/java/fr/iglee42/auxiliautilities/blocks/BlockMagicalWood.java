package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMagicalWood extends AUBlock implements AUBlockBase {
    public BlockMagicalWood(Properties props) {
        super(props);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 2.5F;
    }
}

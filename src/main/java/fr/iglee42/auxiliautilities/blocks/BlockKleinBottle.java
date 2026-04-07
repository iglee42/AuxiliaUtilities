package fr.iglee42.auxiliautilities.blocks;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.BEOpiniumCore;
import fr.iglee42.auxiliautilities.blockentities.klein.BEKleinBottle;
import fr.iglee42.auxiliautilities.blocks.api.AUBlock;
import fr.iglee42.auxiliautilities.blocks.api.AUEntityBlock;
import fr.iglee42.auxiliautilities.items.AUItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class BlockKleinBottle extends AUBlock implements AUEntityBlock<BEKleinBottle> {

    public BlockKleinBottle(Properties props) {
        super(props);
    }

    @Override
    public @NotNull BlockEntityType<BEKleinBottle> type() {
        return AUBlockEntityTypes.KLEIN_BOTTLE.get();
    }

}

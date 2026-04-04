package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class AUBlockItem extends BlockItem implements AUItemBase {
    public AUBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {
        if (getBlock() instanceof AUBlockBase blockBase)
            blockBase.addToCreativeTab(acceptor);
         else
            acceptor.accept(new ItemStack(this));
    }
}

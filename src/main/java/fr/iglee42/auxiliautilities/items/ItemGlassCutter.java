package fr.iglee42.auxiliautilities.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemGlassCutter extends AUItem {
    public ItemGlassCutter(Properties props) {
        super(props.component(DataComponents.TOOL, new Tool(
                List.of(Tool.Rule.minesAndDrops(Tags.Blocks.GLASS_BLOCKS,4),Tool.Rule.minesAndDrops(Tags.Blocks.GLASS_PANES,4)),1,1
        )));
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        stack.setDamageValue(stack.getDamageValue() + 1);
        return stack;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void blockBroken(BlockDropsEvent event){
        if (event.getTool().is(AUItems.GLASS_CUTTER.asItem()) && (event.getState().is(Tags.Blocks.GLASS_BLOCKS) || event.getState().is(Tags.Blocks.GLASS_PANES)) && !event.getLevel().isClientSide){
            event.getDrops().clear();
            BlockState state = event.getState();
            BlockPos pos = event.getPos();
            Level level = event.getLevel();
            ItemStack tool = event.getTool().copy();
            tool.enchant(level.registryAccess().holderOrThrow(Enchantments.SILK_TOUCH),1);
            Block.beginCapturingDrops();
            Block.getDrops(state, (ServerLevel) level,pos,event.getBlockEntity(),event.getBreaker(),tool).forEach(stack->Block.popResource(level,pos,stack));
            event.getDrops().addAll(Block.stopCapturingDrops());
        }
    }
}

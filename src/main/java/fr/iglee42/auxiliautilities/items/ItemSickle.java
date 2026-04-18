package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;
import java.util.Map;

public class ItemSickle extends DiggerItem implements AUItemBase {

    public static final Map<ToolMaterial,Integer> RANGES = Map.of(
            ToolMaterial.WOOD,1,
            ToolMaterial.STONE,2,
            ToolMaterial.IRON,3,
            ToolMaterial.GOLD,1,
            ToolMaterial.DIAMOND,4,
            ToolMaterial.NETHERITE,5
    );

    private final ToolMaterial material;

    private ThreadLocal<Boolean> propagate = ThreadLocal.withInitial(() -> false);

    public ItemSickle(ToolMaterial material, float attackDamage, float attackSpeed, Properties props) {
        super(material, BlockTags.CROPS, attackDamage, attackSpeed, props);
        this.material = material;
        NeoForge.EVENT_BUS.register(this);
    }

    public int getRange(ItemStack stack){
        if (!(stack.getItem() instanceof ItemSickle sickle)) return 0;
        return RANGES.getOrDefault(material, Mth.floor(material.attackDamageBonus() + 1));
    }

    public boolean isEffectiveOn(BlockState state) {
        return state.is(BlockTags.CROPS) || state.getBlock() instanceof BushBlock || state.getBlock() instanceof CaveVines;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return isEffectiveOn(state) ? material.speed() : super.getDestroySpeed(stack, state);
    }


    @SubscribeEvent
    public void mineBlock(BlockEvent.BreakEvent event){
        ItemStack item = event.getPlayer().getMainHandItem();
        if (item == null || item.isEmpty() || item.getItem() != this) return;
        if (propagate.get()) return;
        LevelAccessor level = event.getLevel();
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        if (level.isClientSide() || !isEffectiveOn(state)) return;
        int range = getRange(item);
        propagate.set(true);
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-range,0,-range), pos.offset(range,0,range))) {
            if (blockPos.equals(pos)) continue;
            BlockState blockState = level.getBlockState(blockPos);
            if (isEffectiveOn(blockState)) {
                ((ServerPlayer) player).gameMode.destroyBlock(blockPos);
            }
        }
        propagate.set(false);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack item = ctx.getItemInHand();
        if (item == null || item.isEmpty() || item.getItem() != this) return super.useOn(ctx);
        if (propagate.get()) return InteractionResult.FAIL;
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = ctx.getLevel().getBlockState(pos);
        if (level.isClientSide() || !isEffectiveOn(state)) return super.useOn(ctx);
        int range = getRange(item);
        propagate.set(true);
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-range,0,-range), pos.offset(range,0,range))) {
            if (blockPos.equals(pos)) continue;
            BlockState blockState = level.getBlockState(blockPos);
            if (isEffectiveOn(blockState)) {
                ((ServerPlayer)player).gameMode.useItemOn(((ServerPlayer) player),level,item,ctx.getHand(),new BlockHitResult(
                        Vec3.atCenterOf(blockPos),
                        ctx.getClickedFace(),
                        blockPos,
                        false
                ));
            }
        }
        propagate.set(false);
        return super.useOn(ctx);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltips, TooltipFlag flag) {
        addTooltips(stack, tooltips, ctx, flag);
        super.appendHoverText(stack, ctx, tooltips, flag);
    }

    @Override
    public List<Component> getAdvancedTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        return List.of(AULang.SICKLE_TOOLTIP.get(),AULang.SICKLE_TOOLTIP_1.get());
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        int range = 2*getRange(stack) +1;
        return List.of(AULang.AREA_TOOLTIP.get(range,range).withStyle(ChatFormatting.GRAY));
    }
}

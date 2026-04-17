package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import fr.iglee42.igleelib.api.utils.ModsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemLasso extends AUItem {

    private final TriPredicate<ItemStack, Player, LivingEntity> canCapture;

    public ItemLasso(Properties props, TriPredicate<ItemStack, Player, LivingEntity> canCapture) {
        super(props);
        this.canCapture = canCapture;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack) {
        if (!hasEntity(stack)) return ItemStack.EMPTY;
        ItemStack newStack = stack.copy();
        newStack.remove(AUDataComponents.STORED_ENTITY);
        return newStack;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return hasEntity(stack);
    }

    public static ItemStack getForCraft(boolean golden, EntityType<?> type){
        ItemStack stack = new ItemStack(golden ? AUItems.GOLDEN_LASSO.asItem() : AUItems.CURSED_LASSO.asItem());
        CompoundTag nbt = new CompoundTag();
        nbt.putString("EntityId", EntityType.getKey(type).toString());
        stack.set(AUDataComponents.STORED_ENTITY,nbt);
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        Direction facing = ctx.getClickedFace();
        Level worldIn = ctx.getLevel();
        ItemStack stack = ctx.getItemInHand();
        if (player == null) return InteractionResult.FAIL;
        if (!release(player,stack,pos,facing,worldIn)) return InteractionResult.FAIL;
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (!capture(player,entity,stack)) return InteractionResult.FAIL;
        player.swing(hand);
        player.setItemInHand(hand,stack);
        return InteractionResult.SUCCESS;
    }

    public boolean capture(Player player,LivingEntity target, ItemStack stack){
        if (target.level().isClientSide) return false;
        if (target instanceof Player || target.getType().is(Tags.EntityTypes.BOSSES) || !target.isAlive()) return false;
        if (hasEntity(stack)) return false;
        if (!canCapture.test(stack,player,target)) return false;
        CompoundTag nbt = new CompoundTag();
        nbt.putString("EntityId", EntityType.getKey(target.getType()).toString());
        target.saveWithoutId(nbt);
        stack.set(AUDataComponents.STORED_ENTITY,nbt);
        target.remove(Entity.RemovalReason.DISCARDED);
        return true;
    }

    public boolean release(Player player,ItemStack stack,BlockPos pos,Direction face,Level level){
        if (player.level().isClientSide) return false;
        if (!hasEntity(stack)) return false;
        Entity entity = getEntityInStack(stack,level);
        if (entity == null) return false;
        BlockPos releasePos = pos.relative(face);
        entity.absMoveTo(releasePos.getX() + 0.5, releasePos.getY(), releasePos.getZ() + 0.5,0,0);
        stack.remove(AUDataComponents.STORED_ENTITY);
        level.addFreshEntity(entity);
        return true;
    }

    public boolean hasEntity(ItemStack stack){
        return InventoryHelper.isStackNotEmpty(stack) && stack.has(AUDataComponents.STORED_ENTITY);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        if (!hasEntity(stack)) return super.getColor(stack, tintIndex);
        Entity entity = getEntityInStack(stack, Minecraft.getInstance().level);
        if (entity == null) return super.getColor(stack, tintIndex);
        SpawnEggItem item = SpawnEggItem.byId(entity.getType());
        if (item == null) return super.getColor(stack, tintIndex);
        if (tintIndex == 1) return FastColor.ARGB32.opaque(item.getColor(1));
        if (tintIndex == 2) return FastColor.ARGB32.opaque(item.getColor(0));
        return super.getColor(stack, tintIndex);
    }

    @Nullable
    public Entity getEntityInStack(ItemStack stack,Level level){
        if (!hasEntity(stack)) return null;
        CompoundTag nbt = stack.get(AUDataComponents.STORED_ENTITY);
        EntityType<?> type = EntityType.byString(nbt.getString("EntityId")).orElse(null);
        if (type == null) return null;
        if (!type.canSummon()) return null;
        Entity entity = type.create(level);
        if (entity == null) return null;
        entity.load(nbt);
        return entity;
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        if (!hasEntity(stack)) return List.of();
        if (ctx.level() == null) return List.of();
        Entity entity = getEntityInStack(stack,ctx.level());
        if (entity == null) return List.of();
        ArrayList<Component> tooltips = new ArrayList<>();

        tooltips.add(AULang.LASSO_STORED_ENTITY_TOOLTIP.get(entity.getDisplayName()));
        if (!(entity instanceof LivingEntity lv)) return tooltips;
        tooltips.add(AULang.LASSO_HEALTH_TOOLTIP.get(formatFloat(lv.getHealth()),formatFloat(lv.getMaxHealth())));
        if (entity instanceof Villager villager){
            tooltips.add(AULang.LASSO_PROFESSION_TOOLTIP.get(ModsUtils.getUpperName(villager.getVillagerData().getProfession().name(),"_")));
        }
        return tooltips;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}

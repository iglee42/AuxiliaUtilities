package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.network.PlaySoundPacket;
import fr.iglee42.auxiliautilities.utils.AUSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class ItemDivisionSigilUnactivated extends AUItem{

    private static final UUID messageUUID = UUID.fromString("d9b2d63d-a233-4123-847a-7e8c9b7620c0");

    public ItemDivisionSigilUnactivated(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        if (player == null) return super.useOn(ctx);
        if (level.isClientSide) return super.useOn(ctx);
        if (!isEnchantingTable(pos, level)) return super.useOn(ctx);
        List<Condition> failingConditions = getFailingConditions(level, pos);
        MutableComponent message =Component.empty().append( AULang.ACTIVATION_RITUAL.get().withStyle(ChatFormatting.UNDERLINE));
        List<Condition> validConditions = new ArrayList<>(List.of(Condition.values()));
        if (failingConditions.isEmpty()){
            if (!validConditions.isEmpty()){
                for (Condition condition : validConditions) {
                    message.append("\n- ").append(condition.getValidMessage().withStyle(ChatFormatting.GREEN));
                }
            }
            message.append("\n").append(AULang.ACTIVATION_RITUAL_VALID.get().withStyle(ChatFormatting.BOLD));
        } else {
            validConditions.removeAll(failingConditions);
            if (!validConditions.isEmpty()){
                for (Condition condition : validConditions) {
                    message.append("\n- ").append(condition.getValidMessage().withStyle(ChatFormatting.GREEN));
                }
            }
            if (!failingConditions.isEmpty()){
                for (Condition condition : failingConditions) {
                    message.append("\n! ").append(condition.getInvalidMessage().withStyle(ChatFormatting.RED));
                }
            }
        }
        AULang.sendMessageToPlayer(player, message, messageUUID);
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    private static List<Condition> getFailingConditions(Level level, BlockPos pos){
        boolean isMidnight = level.getDayTime() % 24000L >= 17500 && level.getDayTime() % 24000L <= 18500;
        boolean canSeeSky = level.canSeeSky(pos);
        boolean noLight = level.getMaxLocalRawBrightness(pos) < 8;
        boolean hasRedstoneCircle = true;
        x: for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                if (!level.getBlockState(pos.offset(x, 0, z)).is(Blocks.REDSTONE_WIRE)) {
                    hasRedstoneCircle = false;
                    break x;
                }
            }
        }
        boolean hasDirtUnder = true;
        xDirt: for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (!level.getBlockState(pos.offset(x, -1, z)).is(Blocks.DIRT) && !level.getBlockState(pos.offset(x, -1, z)).is(Blocks.GRASS_BLOCK)) {
                    hasDirtUnder = false;
                    break xDirt;
                }
            }
        }
        boolean hasEnoughNatural = true;
        xNatural: for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (!level.getBlockState(pos.offset(x, -1, z)).is(Blocks.DIRT) && !level.getBlockState(pos.offset(x, -1, z)).is(Blocks.GRASS_BLOCK)) {
                    hasEnoughNatural = false;
                    break xNatural;
                }
            }
        }
        List<Condition> failingConditions = new ArrayList<>();
        if (!isMidnight) failingConditions.add(Condition.MIDNIGHT);
        if (!canSeeSky) failingConditions.add(Condition.CAN_SEE_SKY);
        if (!hasRedstoneCircle) failingConditions.add(Condition.REDSTONE_CIRCLE);
        if (!hasDirtUnder) failingConditions.add(Condition.DIRT_UNDER);
        if (!hasEnoughNatural) failingConditions.add(Condition.ENOUGH_NATURAL);
        if (!noLight) failingConditions.add(Condition.NO_LIGHT);
        return failingConditions;
    }

    public static boolean isEnchantingTable(BlockPos pos, Level level){
        return level.getBlockState(pos).is(Blocks.ENCHANTING_TABLE);
    }

    private enum Condition{
        MIDNIGHT, CAN_SEE_SKY, REDSTONE_CIRCLE, DIRT_UNDER, ENOUGH_NATURAL, NO_LIGHT;

        public MutableComponent getValidMessage(){
            return Component.translatable("message."+ AuxiliaUtilities.MODID + ".activation_ritual."+name().toLowerCase()+".valid");
        }

        public MutableComponent getInvalidMessage(){
            return Component.translatable("message."+AuxiliaUtilities.MODID + ".activation_ritual."+name().toLowerCase()+".invalid");
        }
    }

    @SubscribeEvent
    public static void onLivingDie(LivingDeathEvent event){
        Level level = event.getEntity().level();
        if (level.isClientSide) return;
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (player.getInventory().countItem(AUItems.UNACTIVATED_DIVISION_SIGIL.asItem()) == 0) return;
        for (BlockPos testPos : BlockPos.betweenClosed(event.getEntity().blockPosition().offset(-1,-1,-1),event.getEntity().blockPosition().offset(1,1,1))){
            if (isEnchantingTable(testPos,level)){
                List<Condition> failingConditions = getFailingConditions(level, testPos);
                if (failingConditions.isEmpty()){
                    int count = player.getInventory().clearOrCountMatchingItems(stack->stack.is(AUItems.UNACTIVATED_DIVISION_SIGIL),-1,player.inventoryMenu.getCraftSlots());
                    player.addItem(new ItemStack(AUItems.DIVISION_SIGIL.asItem(),count));
                    PacketDistributor.sendToPlayer((ServerPlayer) player,new PlaySoundPacket(AUSounds.CREEPY_LAUGH.getId(), testPos.immutable()));
                    double x = testPos.getX();
                    double y = testPos.getY();
                    double z = testPos.getZ();

                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                    if (lightning != null) {
                        lightning.moveTo(x, y, z);
                        level.addFreshEntity(lightning);
                    }

                    int maxRange = 8;
                    for (int dx = -maxRange; dx <= maxRange; dx++) {
                        for (int dz = -maxRange; dz <= maxRange; dz++) {
                            if (dx * dx + dz * dz >= maxRange * maxRange)
                                continue;
                            for (int dy = maxRange; dy > -maxRange; dy--) {

                                if (dx * dx + dy * dy + dz * dz > maxRange * maxRange) {
                                    if (dy < 0) break;
                                    continue;
                                }

                                BlockPos pos = testPos.offset(dx, dy, dz);
                                BlockState state = level.getBlockState(pos);

                                if (state.isAir())
                                    continue;

                                Block block = state.getBlock();

                                if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK)) {

                                    level.setBlockAndUpdate(
                                            pos,
                                            AUBlocks.CURSED_EARTH.get().defaultBlockState()
                                    );
                                    break;
                                }

                                if (block instanceof LeavesBlock) {
                                    Block.dropResources(state, level, pos);
                                    level.removeBlock(pos, false);
                                }
                                else if (block instanceof SnowLayerBlock) {
                                    level.removeBlock(pos, false);
                                }
                                else if (state.getPistonPushReaction() == PushReaction.DESTROY && !state.is(Blocks.REDSTONE_WIRE)) {
                                    level.destroyBlock(pos, true);
                                }
                                else if (state.isSolidRender(level, pos)) {
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

}

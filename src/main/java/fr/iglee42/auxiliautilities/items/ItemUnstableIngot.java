package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.utils.GetterSetter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.LinkedList;
import java.util.List;

public class ItemUnstableIngot extends AUItem {
    public static final ResourceKey<DamageType> UNSTABLE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, AuxiliaUtilities.id("unstable"));

    public ItemUnstableIngot(Properties properties) {
        super(properties);
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        if (player != null && player.containerMenu != null && player.containerMenu != player.inventoryMenu){
            stack.set(DataComponents.PROFILE, new ResolvableProfile(player.getGameProfile()));
            stack.set(AUDataComponents.CONTAINER_ID, player.containerMenu.containerId);
            stack.set(AUDataComponents.DIMENSION, player.level().dimension().hashCode());
            stack.set(AUDataComponents.TIME, player.level().getGameTime());

        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor(ItemStack stack, int tintIndex) {
        var level = Minecraft.getInstance().level;

        if (level != null && stack.getDamageValue() == 0 && stack.has(AUDataComponents.TIME)) {

            long baseTime = stack.get(AUDataComponents.TIME);

            if (baseTime > 0L) {
                float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

                float time = (float) (AUConfig.UNSTABLE_TIME.get() + baseTime - level.getGameTime())
                        - partialTick
                        + (tintIndex * 3);

                if (time < 0.0F) {
                    return rgba(1.0F, 0.0F, 0.0F, 1f);
                }

                if (time > AUConfig.UNSTABLE_TIME.get()) {
                    return -1;
                }

                float v = time / AUConfig.UNSTABLE_TIME.get();
                float r = 1.0F;
                float g;
                float b;

                if (v > 0.66F) {
                    float t = (v - 0.66F) / 0.34F;
                    g = 1.0F;
                    b = t; // 1 → 0
                } else if (v > 0.33F) {
                    float t = (v - 0.33F) / 0.33F;
                    g = t; // 1 → ~0.5
                    b = 0.0F;
                } else {
                    float t = v / 0.33F;
                    g = 0.5F * t; // ~0.5 → 0
                    b = 0.0F;
                }

                return rgba(r, g, b, 1.0F);
            }
        }

        return -1;
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return super.getBarColor(p_150901_);
    }

    private static int rgba(float r, float g, float b, float a) {
        int ri = (int)(clamp(r, 0, 1) * 255);
        int gi = (int)(clamp(g, 0, 1) * 255);
        int bi = (int)(clamp(b, 0, 1) * 255);
        int ai = (int)(clamp(a, 0, 1) * 255);

        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }

    private static void explodePlayer(Player player){
        player.hurt(player.damageSources().source(UNSTABLE_DAMAGE), Float.MAX_VALUE);
        player.level().explode(player, player.getX(), player.getY(), player.getZ(), 6,true, Level.ExplosionInteraction.MOB);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public void onToss(ItemTossEvent event){
        ItemStack stack = event.getEntity().getItem();
        if (!(stack.getItem() instanceof ItemUnstableIngot))return;
        if (!event.getEntity().level().isClientSide){
            explodePlayer(event.getPlayer());
        }
        event.getEntity().setItem(ItemStack.EMPTY);
        event.getEntity().discard();
    }

    @SubscribeEvent
    public void checkForExplosion(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (level.isClientSide) return;

        AbstractContainerMenu container = player.containerMenu;

        List<GetterSetter<ItemStack>> itemRefs = new LinkedList<>();

        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            itemRefs.add(new GetterSetter.InvSlot(inventory, i));
        }

        itemRefs.add(new GetterSetter.PlayerHand(inventory));

        int windowId;

        if (container == null || container == player.inventoryMenu) {
            windowId = -1;
        } else {
            windowId = container.containerId;

            for (Slot slot : container.slots) {
                itemRefs.add(new GetterSetter.ContainerSlot(slot));
            }
        }

        List<GetterSetter<ItemStack>> toExplode = new LinkedList<>();

        for (GetterSetter<ItemStack> ref : itemRefs) {
            ItemStack stack = ref.get();

            if (stack.isEmpty() || stack.getItem() != this) continue;
            if (!stack.has(AUDataComponents.TIME)) continue;

            long time = stack.get(AUDataComponents.TIME);
            int dim = stack.get(AUDataComponents.DIMENSION);
            int storedContainer = stack.get(AUDataComponents.CONTAINER_ID);

            if (time + AUConfig.UNSTABLE_TIME.get() > level.getGameTime()
                    && dim == level.dimension().hashCode()
                    && storedContainer == windowId) {
                continue;
            }

            toExplode.add(ref);
        }

        if (toExplode.isEmpty()) return;

        for (GetterSetter<ItemStack> ref : toExplode) {
            ref.accept(ItemStack.EMPTY);
        }

        explodePlayer(player);

        if (windowId != -1) {
            player.closeContainer();
        }

        player.containerMenu.broadcastChanges();
    }

    @Override
    public List<Component> getTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        List<Component> tooltips = new LinkedList<>();
        tooltips.add(AULang.UNSTABLE_INGOT_TOOLTIP_ERROR.get());
        if (ctx.level() == null || !stack.has(AUDataComponents.TIME)) return tooltips;
        long time = stack.get(AUDataComponents.TIME);
        float remaining = (float) ((time + AUConfig.UNSTABLE_TIME.get()) - ctx.level().getGameTime()) / 20;
        if (remaining <= 0) return tooltips;
        tooltips.add(AULang.UNSTABLE_INGOT_TOOLTIP_EXPLOSION.get(formatFloat(remaining)).withColor(getColor(stack,0)));
        return tooltips;
    }
}

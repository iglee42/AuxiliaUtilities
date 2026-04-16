package fr.iglee42.auxiliautilities.items;

import com.mojang.serialization.Codec;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.terraformer.TerraformerType;
import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import fr.iglee42.auxiliautilities.items.api.gp.AUGPConsumerItem;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class ItemAngelRing extends AUGPConsumerItem {

    private static final ResourceLocation ATTRIBUTE_MODIFIER_ID = AuxiliaUtilities.id("angel_ring_flight");

    public ItemAngelRing(Properties props) {
        super(props.component(AUDataComponents.WINGS,AngelRingWings.NONE));
    }

    public static ItemStack getWithWings(AngelRingWings wings){
        ItemStack stack = new ItemStack(AUItems.ANGEL_RING.get());
        stack.set(AUDataComponents.WINGS,wings);
        return stack;
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {
        Arrays.stream(AngelRingWings.values()).forEach(wings -> acceptor.accept(getWithWings(wings)));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean offhand) {
        tick(stack,entity);
    }

    public void tick(ItemStack stack, Entity entity) {
        if (entity.level().isClientSide) return;
        if (!(entity instanceof Player player)) return;
        boolean enoughPower = GPNetworkManager.INSTANCE.hasEnoughPower(getNetworkId(stack, player));
        var attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        var modifier = attribute.getModifier(ATTRIBUTE_MODIFIER_ID);

        if (enoughPower) {
            if (modifier == null) {
                attribute.addTransientModifier(
                        new AttributeModifier(ATTRIBUTE_MODIFIER_ID, 1, AttributeModifier.Operation.ADD_VALUE)
                );
            }
        } else if (modifier != null) {
            attribute.removeModifier(ATTRIBUTE_MODIFIER_ID);
        }
    }

    @Override
    protected void onRemove(ItemStack stack, Player player) {
        var attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attribute.getModifier(ATTRIBUTE_MODIFIER_ID) != null) {
            attribute.removeModifier(ATTRIBUTE_MODIFIER_ID);
        }
        if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }

    @Override
    protected int getGPConsumption(ItemStack stack, Player player) {
        return player.getAbilities().flying ? 32 : 1;
    }

    public static enum AngelRingWings implements StringRepresentable{
        NONE,
        FEATHER,
        BAT,
        BUTTERFLY,
        GOLDEN,
        DEMON
        ;

        public static final Codec<AngelRingWings> CODEC = StringRepresentable.fromEnum(AngelRingWings::values);
        public static final IntFunction<AngelRingWings> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, AngelRingWings> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}

package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.items.SingleItemStackHandler;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ItemBiomeMarker extends AUItem {


    public ItemBiomeMarker(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        Holder<Biome> biome = stack.get(AUDataComponents.STORED_BIOME.get());
        if (biome != null && tintIndex > 0){
            return switch (tintIndex){
                case 1 -> biome.value().getGrassColor(0,0);
                case 2 -> biome.value().getSkyColor();
                case 3 -> biome.value().getFogColor();
                case 4 -> biome.value().getWaterColor();
                default -> super.getColor(stack,tintIndex);
            };
        }
        return super.getColor(stack, tintIndex);
    }

    @Override
    public void addToTab(Consumer<ItemStack> acceptor) {
        super.addToTab(acceptor);
        Minecraft.getInstance().level.registryAccess().lookup(Registries.BIOME).ifPresent(registry->{
            registry.stream().forEach(biome->{
                    ItemStack stack = new ItemStack(this);
                    stack.set(AUDataComponents.STORED_BIOME.get(), registry.wrapAsHolder(biome));
                    acceptor.accept(stack);
            });
        });
    }

    @Override
    public List<Component> getStorageTooltips(ItemStack stack, TooltipContext ctx, TooltipFlag flag) {
        Holder<Biome> biome = stack.get(AUDataComponents.STORED_BIOME.get());
        if (biome != null){
            ResourceLocation resourcelocation = biome.getKey().location();
            String s = resourcelocation.toLanguageKey("biome");
            Component name;
            if (Language.getInstance().has(s)) {
                name = Component.translatable(s);
            } else {
                name = Component.literal(resourcelocation.toString());
            }
            return List.of(AULang.STORED_BIOME_TOOLTIP.get(name.getString()));
        }
        return super.getStorageTooltips(stack, ctx, flag);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player.isCrouching()) {
            if (stack.has(AUDataComponents.STORED_BIOME)) {
                stack.remove(AUDataComponents.STORED_BIOME);
                return InteractionResult.SUCCESS_SERVER;
            }
        } else if (!level.isClientSide && !stack.has(AUDataComponents.STORED_BIOME)) {
            Holder<Biome> biome = level.getBiome(player.blockPosition());
            stack.set(AUDataComponents.STORED_BIOME.get(), biome);
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }

    public static class SingleBiomeStackHandler extends SingleItemStackHandler{
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return stack.is(AUItems.BIOME_MARKER) ? 1 : super.getStackLimit(slot, stack);
        }

        public SlotItemHandlerWidget getWidget(int x, int y){
            return new SlotItemHandlerWidget(this,0,x,y) {
                @Override
                public @NotNull ResourceLocation getNoItemIcon() {
                    return  AuxiliaUtilities.id("item/biome_marker_blank");
                }


                @Override
                public @NotNull List<Component> getTooltips() {
                    if (InventoryHelper.isStackEmpty(getStack())){
                        return List.of(AUItems.BIOME_MARKER.toStack().getDisplayName());
                    }
                    return super.getTooltips();
                }
            };
        }

        @Nullable
        public Holder<Biome> getBiome(){
            return getStack().get(AUDataComponents.STORED_BIOME.get());
        }
    }
}

package fr.iglee42.auxiliautilities.client;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.api.AUBlockBase;
import fr.iglee42.auxiliautilities.client.layers.AngelRingRenderer;
import fr.iglee42.auxiliautilities.client.layers.ChickenRingRenderer;
import fr.iglee42.auxiliautilities.client.layers.KikokuSheathLayer;
import fr.iglee42.auxiliautilities.client.models.FlatTransferNodeModelWrapper;
import fr.iglee42.auxiliautilities.client.models.SunCrystalModelWrapper;
import fr.iglee42.auxiliautilities.client.models.WandsModelWrapper;
import fr.iglee42.auxiliautilities.client.renderers.*;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.items.*;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID, value = Dist.CLIENT)
public class AUClient {

    public static void sendPlayerMessage(Player player, Component message, MessageSignature signature){
        Minecraft mc = Minecraft.getInstance();
        deleteMessage(signature);
        if (message == null) return;
        mc.gui.getChat().addMessage(message,signature,null);
    }

    private static void deleteMessage(MessageSignature signature) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.gui.getChat() == null) {
            return;
        }
        ChatComponent chat = mc.gui.getChat();
        chat.allMessages.removeIf(e -> Objects.equals(e.signature(), signature));

        chat.trimmedMessages.clear();
        for (int i = chat.allMessages.size() - 1; i >= 0; --i) {
            GuiMessage guiMessage = chat.allMessages.get(i);
            addMessageQuietly(chat, guiMessage.content(), guiMessage.signature(), guiMessage.addedTime(), guiMessage.tag(), true);
        }
    }

    private static void addMessageQuietly(ChatComponent chat, Component component, @Nullable MessageSignature messageSignature, int i, @Nullable GuiMessageTag guiMessageTag, boolean updateOnly) {
        int j = Mth.floor((double) chat.getWidth() / chat.getScale());
        if (guiMessageTag != null && guiMessageTag.icon() != null) {
            j -= guiMessageTag.icon().width + 4 + 2;
        }

        List<FormattedCharSequence> list = ComponentRenderUtils.wrapComponents(component, j, Minecraft.getInstance().font);
        boolean bl2 = chat.isChatFocused();

        for (int k = 0; k < list.size(); ++k) {
            FormattedCharSequence formattedCharSequence = list.get(k);
            if (bl2 && chat.chatScrollbarPos > 0) {
                chat.newMessageSinceScroll = true;
                chat.scrollChat(1);
            }

            boolean bl3 = k == list.size() - 1;
            chat.trimmedMessages.add(0, new GuiMessage.Line(i, formattedCharSequence, guiMessageTag, bl3));
        }

        while (chat.trimmedMessages.size() > 100) {
            chat.trimmedMessages.remove(chat.trimmedMessages.size() - 1);
        }

        if (!updateOnly) {
            chat.allMessages.add(0, new GuiMessage(i, component, messageSignature, guiMessageTag));

            while (chat.allMessages.size() > 100) {
                chat.allMessages.remove(chat.allMessages.size() - 1);
            }
        }
    }


    @SubscribeEvent
    public static void modifyBackingResult(ModelEvent.ModifyBakingResult event){
        event.getModels().computeIfPresent(new ModelResourceLocation(AUItems.CREATIVE_BUILDERS_WAND.getId(), "inventory"),
                (location,model)->new WandsModelWrapper(model,"builders"));
        event.getModels().computeIfPresent(new ModelResourceLocation(AUItems.CREATIVE_DESTRUCTION_WAND.getId(), "inventory"),
                (location,model)->new WandsModelWrapper(model,"destruction"));
        event.getModels().computeIfPresent(new ModelResourceLocation(AUItems.SUN_CRYSTAL.getId(), "inventory"),
                (location, model) -> new SunCrystalModelWrapper(model));
        event.getModels().computeIfPresent(new ModelResourceLocation(AUItems.FLAT_ITEM_TRANSFER_NODE.getId(), "inventory"),
                (location, model) -> new FlatTransferNodeModelWrapper(model));
        event.getModels().computeIfPresent(new ModelResourceLocation(AUItems.FLAT_FLUID_TRANSFER_NODE.getId(), "inventory"),
                (location, model) -> new FlatTransferNodeModelWrapper(model));
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        registerItemProperties();
    }

    @SubscribeEvent
    public static void registerColors(RegisterColorHandlersEvent.Item event){
        AUItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(AUItemBase.class::isInstance)
                .map(AUItemBase.class::cast)
                .forEach(i->
                    event.register((stack, tintIndex)->!stack.isEmpty() ? i.getColor(stack,tintIndex) : 0xFFFFFFFF, i.self()));
    }

    @SubscribeEvent
    public static void registerColors(RegisterColorHandlersEvent.Block event){
        AUBlocks.BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(AUBlockBase.class::isInstance)
                .map(AUBlockBase.class::cast)
                .forEach(i->
                        event.register((state,getter,pos, tintIndex)->getter != null && !state.isAir() ? i.getColor(state,getter,pos,tintIndex) : 0xFFFFFFFF, i.self()));
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event){
        event.register(ManualMillRenderer.GEAR_MODEL);
        event.register(KikokuSheathLayer.MODEL_LOCATION);
        event.register(KikokuSheathLayer.EMPTY_LOCATION);
        for (ItemAngelRing.AngelRingWings wing : ItemAngelRing.AngelRingWings.values()) {
            if (wing == ItemAngelRing.AngelRingWings.NONE) continue;
            event.register(AngelRingRenderer.getWingLocation(wing));
        }
    }

    private static void registerItemProperties() {
        ItemProperties.register(
                AUItems.ENDER_SHARD.asItem(),
                AuxiliaUtilities.id("shards"),
                (stack,level,entity,seed)-> (float) stack.getCount()
        );
        ItemProperties.register(
                AUItems.LUX_SABER.asItem(),
                AuxiliaUtilities.id("charged"),
                (stack,level,entity,seed)-> stack.getOrDefault(AUDataComponents.STORED_ENERGY,0) >= AUConfig.SABER_THRESHOLD.get() ? 1f : 0f
        );
        ItemProperties.register(
                AUItems.BIOME_MARKER.asItem(),
                AuxiliaUtilities.id("has_biome"),
                (stack,level,entity,seed)-> stack.has(AUDataComponents.STORED_BIOME) ? 1f : 0f
        );

        ItemProperties.register(AUItems.COMPOUND_BOW.asItem(), ResourceLocation.withDefaultNamespace("pull"), (p_344163_, p_344164_, p_344165_, p_344166_) -> {
            if (p_344165_ == null) {
                return 0.0F;
            } else if (p_344165_.getUseItem() != p_344163_) {
                return 0.0F;
            } else {
                float progress = (float)(p_344163_.getUseDuration(p_344165_) - p_344165_.getUseItemRemainingTicks()) / 20.0F;
                return  progress >= 1.0F ? 4F : (progress > 0.82F ? 3F : (progress > 0.4F ? 2F : 1F));
            }
        });
        ItemProperties.register(AUItems.COMPOUND_BOW.asItem(),
                ResourceLocation.withDefaultNamespace("pulling"),
                (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F
        );
        ItemProperties.register(AUItems.ANGEL_RING.asItem(),
                AuxiliaUtilities.id("wings"),
                (stack,level,entity,seed)->stack.getOrDefault(AUDataComponents.WINGS, ItemAngelRing.AngelRingWings.NONE).ordinal());

        ItemProperties.register(AUItems.GOLDEN_LASSO.asItem(),
                AuxiliaUtilities.id("has_entity"),
                (stack,level,entity,seed)->stack.has(AUDataComponents.STORED_ENTITY) ? 1f : 0f);

        ItemProperties.register(AUItems.CURSED_LASSO.asItem(),
                AuxiliaUtilities.id("has_entity"),
                (stack,level,entity,seed)->stack.has(AUDataComponents.STORED_ENTITY) ? 1f : 0f);
    }

    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event){
        event.registerAbove(VanillaGuiLayers.HOTBAR,AuxiliaUtilities.id("gp_informations"),ClientGPManager.HUD);
        event.registerAbove(VanillaGuiLayers.HOTBAR,AuxiliaUtilities.id("flying_ring_bar"),ClientFlyingRingManager.HUD);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(AUBlockEntityTypes.MANUAL_MILL.get(), ManualMillRenderer::new);
        event.registerBlockEntityRenderer(AUBlockEntityTypes.RAINBOW_GENERATOR.get(), RainbowGeneratorRenderer::new);
        event.registerBlockEntityRenderer(AUBlockEntityTypes.OPINIUM_CORE.get(), OpiniumCoreRenderer::new);
        event.registerBlockEntityRenderer(AUBlockEntityTypes.KLEIN_BOTTLE.get(), KleinBottleRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(new OpiniumCoreItemRenderer.Extension(),AUBlocks.MISERABLE_OPINIUM_CORE.asItem(),AUBlocks.PATHETIC_OPINIUM_CORE.asItem(),AUBlocks.MEDIOCRE_OPINIUM_CORE.asItem(),AUBlocks.PASSABLE_OPINIUM_CORE.asItem(),AUBlocks.DECENT_OPINIUM_CORE.asItem(),AUBlocks.SOLID_OPINIUM_CORE.asItem(),AUBlocks.GOOD_OPINIUM_CORE.asItem(),AUBlocks.DAMN_GOOD_OPINIUM_CORE.asItem(),AUBlocks.AMAZING_OPINIUM_CORE.asItem(),AUBlocks.INSPIRING_OPINIUM_CORE.asItem(),AUBlocks.PERFECTED_OPINIUM_CORE.asItem());
        event.registerItem(new KleinBottleItemRenderer.Extension(),AUBlocks.KLEIN_BOTTLE.asItem());
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event){
        AUMenus.MENU_TYPES.getEntries()
                .forEach(menu->event.register((MenuType<AUMenu>)menu.get(), AUContainerScreen::new));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.AddLayers event){
        for (PlayerSkin.Model type : event.getSkins()){
            PlayerRenderer renderer = event.getSkin(type);
            if (renderer != null){
                renderer.addLayer(new KikokuSheathLayer<>(renderer));
                renderer.addLayer(new AngelRingRenderer<>(renderer));
                renderer.addLayer(new ChickenRingRenderer<>(renderer));
            }
        }
    }
}

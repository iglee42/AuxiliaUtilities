package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.client.screen.AUContainerScreen;
import fr.iglee42.auxiliautilities.gp.GPHolder;
import fr.iglee42.auxiliautilities.gp.GPNetwork;
import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import fr.iglee42.auxiliautilities.items.api.AUItem;
import fr.iglee42.auxiliautilities.menu.AUMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUTextScrollWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUWidgetBase;
import fr.iglee42.auxiliautilities.network.AUPacket;
import fr.iglee42.auxiliautilities.network.AUPackets;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ItemPowerManager extends AUItem implements MenuProvider {

    public ItemPowerManager(Properties props) {
        super(props.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide){
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.getItem() == this){
                player.openMenu(this);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new PowerReportMenu(player,id);
    }


    public static class PowerReportMenu extends AUMenu {
        private static final NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);

        private final Player player;
        private final List<Component> generators = new ArrayList<>();
        private final List<Component> drainers = new ArrayList<>();
        private final List<Component> inactive = new ArrayList<>();
        public PowerReportMenu(int id, Inventory playerInv, RegistryFriendlyByteBuf buf) {
            this(playerInv.player, id);
        }

        protected PowerReportMenu(Player player, int id) {
            super(AUMenus.POWER_REPORT.get(), id);
            this.player = player;
            addWidget(new AUTextScrollWidget(4,24,176,176) {
                @Override
                protected List<Component> getMessages() {
                    List<Component> list = new ArrayList<>();
                    if (generators.isEmpty() && drainers.isEmpty() && inactive.isEmpty()){
                        list.add(AULang.NO_GP_HOLDERS.get());
                        return list;
                    }
                    if (!generators.isEmpty()){
                        list.add(AULang.GP_GENERATORS.get());
                        list.addAll(generators);
                    }

                    if (!drainers.isEmpty()){
                        if (!generators.isEmpty()) list.add(Component.empty());
                        list.add(AULang.GP_DRAINERS.get());
                        list.addAll(drainers);
                    }

                    if (!inactive.isEmpty()){
                        if (!generators.isEmpty() || !drainers.isEmpty()) list.add(Component.empty());
                        list.add(AULang.GP_INACTIVE.get());
                        list.addAll(inactive);
                    }
                    return list;
                }
            });
            crop(4);

            AUWidgetBase item = new AUWidgetBase(width / 2, 0, 0, 0) {
                @Override
                public void renderBackground(GuiGraphics graphics, AUContainerScreen gui, int guiLeft, int guiTop) {
                    ResourceLocation texture = AuxiliaUtilities.id("item/resonating_redstone_crystal");
                    TextureAtlasSprite sprite = Minecraft.getInstance()
                            .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                            .apply(texture);
                    graphics.blitSprite(
                            RenderType::guiTextured,
                            sprite,
                            guiLeft + x - 20,
                            guiTop + y - 20,
                            0,
                            40,
                            40
                    );
                }
            };
            addWidget(item);
            validate();
        }

        @Override
        public void broadcastChanges() {
            super.broadcastChanges();
            if (this.player instanceof ServerPlayer serverPlayer){
                syncPowerData(serverPlayer);
            }
        }

        public void updatePowerData(List<PowerEntry> entries) {

            generators.clear();
            drainers.clear();
            inactive.clear();

            for (PowerEntry entry : entries) {
                if (entry.power() > 0) {
                    generators.add(Component.literal(" ").append(Component.translatable(entry.name())).append(" x " + entry.count() + ": ").append(Component.literal(nf.format(entry.power()) + " GP").withStyle(ChatFormatting.DARK_GREEN)));
                } else if (entry.power() < 0) {
                    drainers.add(Component.literal(" ").append(Component.translatable(entry.name())).append(" x " + entry.count() + ": ").append(Component.literal(nf.format(-entry.power()) + " GP").withStyle(ChatFormatting.DARK_RED)));
                } else {
                    inactive.add(Component.literal(" ").append(Component.translatable(entry.name())).append(" x " + entry.count()));
                }
            }

        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return true;
        }

        public void syncPowerData(ServerPlayer player) {
            List<PowerEntry> list = new ArrayList<>();

            synchronized (new Object()) {

                HashMap<String,Integer> numPowers = new HashMap<>();
                HashMap<String,Float> powerOutput = new HashMap<>();

                GPNetwork playerNetwork = GPNetworkManager.INSTANCE.getNetwork(player.getUUID());
                if (playerNetwork == null) return;

                for (GPHolder holder : playerNetwork.getHolders()) {

                    String name = holder.name();
                    if (name == null || name.isBlank()) continue;

                    float f = holder.getGPBalance();
                    if (Float.isNaN(f)) continue;

                    numPowers.compute(name, (k, v) -> v == null ? 1 : v + 1);
                    powerOutput.compute(name, (k, v) -> v == null ? f : v + f);
                }

                for (String name : numPowers.keySet()) {

                    list.add(new PowerEntry(
                            name,
                            numPowers.get(name),
                            powerOutput.get(name)
                    ));
                }
            }

            PacketDistributor.sendToPlayer(player, new PowerDataPacket(list));
        }

        public static record PowerEntry(String name, int count, float power) {

            public static final StreamCodec<FriendlyByteBuf, PowerEntry> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, PowerEntry::name,
                    ByteBufCodecs.INT, PowerEntry::count,
                    ByteBufCodecs.FLOAT, PowerEntry::power,
                    PowerEntry::new
            );
        }
    }
    public static class PowerDataPacket extends AUPacket {

        public static final StreamCodec<RegistryFriendlyByteBuf, PowerDataPacket> STREAM_CODEC = StreamCodec.composite(
                ItemPowerManager.PowerReportMenu.PowerEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), PowerDataPacket::getEntries,
                PowerDataPacket::new
        );

        private final List<ItemPowerManager.PowerReportMenu.PowerEntry> entries;

        public PowerDataPacket(List<ItemPowerManager.PowerReportMenu.PowerEntry> entries) {
            super(AUPackets.POWER_DATA);
            this.entries = entries;
        }

        public List<ItemPowerManager.PowerReportMenu.PowerEntry> getEntries() {
            return entries;
        }

        @Override
        protected void handle(IPayloadContext context) {
            context.enqueueWork(()->{
                if (context.player().containerMenu instanceof PowerReportMenu menu){
                    menu.updatePowerData(entries);
                }
            });
        }
    }
}

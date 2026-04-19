package fr.iglee42.auxiliautilities.client;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPBlockEntity;
import fr.iglee42.auxiliautilities.network.AskCurrentBlockGPPacket;
import fr.iglee42.auxiliautilities.network.SyncGPNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AuxiliaUtilities.MODID,value = Dist.CLIENT)
public class ClientGPManager {

    private static int totalGeneration;
    private static int totalConsumption;
    private static int currentBlockGP;

    public static void loadFromPacket(SyncGPNetworkPacket packet){
        totalGeneration = packet.getTotalGeneration();
        totalConsumption = packet.getTotalConsumption();
    }

    public static void setCurrentBlockGP(int consumption) {
        currentBlockGP = consumption;
    }
    public static int getTotalGeneration() {
        return totalGeneration;
    }

    public static int getTotalConsumption() {
        return totalConsumption;
    }

    public static final GuiLayer HUD = (gui, tracker)->{
        if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult result)) return;
        if (Minecraft.getInstance().level == null) return;
        if (!(Minecraft.getInstance().level.getBlockEntity(result.getBlockPos()) instanceof AUGPBlockEntity)) return;
        gui.pose().pushMatrix();
        int y = gui.guiHeight() * 7/10;
        gui.drawCenteredString(Minecraft.getInstance().font,AULang.GP_TOOLTIP.get(getTotalConsumption(),getTotalGeneration()),gui.guiWidth()/2,y,0xFFFFFF);
        if (currentBlockGP != 0){
            Component text;
            if (currentBlockGP < 0) text = AULang.BLOCK_DRAIN_GP.get(-currentBlockGP);
            else text = AULang.BLOCK_GENERATE_GP.get(currentBlockGP);
            gui.drawCenteredString(Minecraft.getInstance().font,text,gui.guiWidth()/2,y + 10,0xFFFFFF);

        } else {
            gui.drawCenteredString(Minecraft.getInstance().font, AULang.BLOCK_NO_GP.get(),gui.guiWidth()/2,y + 10,0xFFFFFF);
        }
        gui.pose().popMatrix();
    };

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event){
        HitResult hit = Minecraft.getInstance().hitResult;
        if (!(hit instanceof BlockHitResult blockHit)) {
            setCurrentBlockGP(0);
            return;
        }
        if (Minecraft.getInstance().level == null) {
            setCurrentBlockGP(0);
            return;
        }
        if (!(Minecraft.getInstance().level.getBlockEntity(blockHit.getBlockPos()) instanceof AUGPBlockEntity)) {
            setCurrentBlockGP(0);
            return;
        }
        ClientPacketDistributor.sendToServer(new AskCurrentBlockGPPacket(blockHit.getBlockPos()));

    }

}

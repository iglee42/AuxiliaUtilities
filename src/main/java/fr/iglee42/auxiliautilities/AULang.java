package fr.iglee42.auxiliautilities;

import fr.iglee42.auxiliautilities.client.AUClient;
import fr.iglee42.auxiliautilities.network.SendChatMessagePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.UUID;

public enum AULang {

    DOOM_EFFECT("effect","doom","Doom"),
    FIZZY_LIFTING_EFFECT("effect","fizzy_lifting","Fizzy Lifting"),
    LOVE_EFFECT("effect","love","Love"),
    GRAVITY_EFFECT("effect","gravity","Gravity"),
    SECOND_CHANCE_EFFECT("effect","second_chance","Second Chance"),
    PURGING_EFFECT("effect","purging","Purging"),
    GREEK_FIRE_EFFECT("effect","greek_fire","Greek Fire"),
    RELAPSE_EFFECT("effect","relapse","Relapse"),

    BUILDERS_WAND_ITEM("item","builders_wand","Builder's Wand"),
    CREATIVE_BUILDERS_WAND_ITEM("item","creative_builders_wand","Creative Builder's Wand"),
    DESTRUCTION_WAND_ITEM("item","destruction_wand","Destruction Wand"),
    CREATIVE_DESTRUCTION_WAND_ITEM("item","creative_destruction_wand","Creative Destruction Wand"),
    ENDER_SHARD("item","ender_shard","Ender Shard"),
    GLASS_CUTTER("item","glass_cutter","Glass Cutter"),
    WATERING_CAN("item","watering_can","Watering Can"),
    UNSTABLE_INGOT("item","unstable_ingot","Unstable Ingot"),
    STABLE_UNSTABLE_INGOT("item","stable_unstable_ingot","Stable-\"Unstable Ingot\""),
    STABLE_UNSTABLE_NUGGET("item","stable_unstable_nugget","Stable-\"Unstable Nugget\""),

    DOOM_MESSAGE("message","doom_effect","The Specter of Death will arrive in %s seconds."),
    SECOND_CHANCE_MESSAGE("message","second_chance_effect","Second Chance !"),
    SECOND_CHANCE_ALREADY_USED_MESSAGE("message","second_chance_effect.already_used","Unfortunately we can't be lucky too many times !"),

    UNSTABLE_INGOT_TOOLTIP_0("tooltip","unstable_ingot.0","§cERROR : Divide by diamond"),
    UNSTABLE_INGOT_TOOLTIP_1("tooltip","unstable_ingot.1","This ingot is highly unstable and will explode after 10 seconds."),
    UNSTABLE_INGOT_TOOLTIP_2("tooltip","unstable_ingot.2","Will also explode if the crafting window is closed or the ingot is thrown on the ground."),
    UNSTABLE_INGOT_TOOLTIP_3("tooltip","unstable_ingot.3","Additionally these ingots do not stack."),
    UNSTABLE_INGOT_TOOLTIP_4("tooltip","unstable_ingot.4","§l - Do not craft unless ready -"),
    UNSTABLE_INGOT_TOOLTIP_5("tooltip","unstable_ingot.5","Must be crafted in a vanilla crafting table."),
    UNSTABLE_INGOT_TOOLTIP_EXPLOSION("tooltip","unstable_ingot.explosion","Explosion in %s"),

    DOOM_DEATH("death.attack.doom","%s met their doom"),
    DOOM_DEATH_ITEM("death.attack.doom.item","%s met their doom"),
    DOOM_DEATH_PLAYER("death.attack.doom.player","%s met their doom whilst fighting %s"),

    UNSTABLE_DEATH("death.attack.unstable","%s suffered a fatal (java.lang.ArithmeticException : / by diamond)"),
    UNSTABLE_DEATH_ITEM("death.attack.unstable.item","%s suffered a fatal (java.lang.ArithmeticException : / by diamond)"),
    UNSTABLE_DEATH_PLAYER("death.attack.unstable.player","%s suffered a fatal (java.lang.ArithmeticException : / by diamond) whilst fighting %s")
    ;

    private final String key;
    private final String defaultTranslation;

    AULang(String category, String key,String defaultTranslation) {
        this.key = category + "." + AuxiliaUtilities.MODID + "." + key;
        this.defaultTranslation = defaultTranslation;
    }

    AULang(String key, String defaultTranslation) {
        this.key = key;
        this.defaultTranslation = defaultTranslation;
    }

    public String getDefaultTranslation() {
        return defaultTranslation;
    }

    public String getKey() {
        return key;
    }

    public Component get(){
        return Component.translatable(this.key);
    }

    public Component get(Object... args){
        return Component.translatable(this.key,args);
    }

    public void sendToPlayer(Player player,UUID signature){
        sendMessageToPlayer(player,get(),signature);
    }

    public void sendToPlayer(Player player,UUID signature,Object... args){
        sendMessageToPlayer(player,get(args),signature);
    }


    public static MessageSignature createSignatureFromUUID(UUID uuid){
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.position(32);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return new MessageSignature(buffer.array());
    }

    public static void sendMessageToPlayer(Player player, @Nullable Component message, UUID signature){
        if (player.level().isClientSide){
            AUClient.sendPlayerMessage(player,message,createSignatureFromUUID(signature));
        } else {
            PacketDistributor.sendToPlayer((ServerPlayer) player,new SendChatMessagePacket(message,signature));
        }
    }

}

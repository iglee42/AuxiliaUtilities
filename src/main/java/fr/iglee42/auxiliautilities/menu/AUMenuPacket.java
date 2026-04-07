package fr.iglee42.auxiliautilities.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AUMenuPacket {

    private static int id = 0;
    private static int id(){
        return id++;
    }

    private static final Map<Integer, Supplier<? extends AUMenuPacket>> DECODERS = new HashMap<>();
    private static final Map<Class<? extends AUMenuPacket>, Integer> IDS = new HashMap<>();

    public static synchronized <T extends AUMenuPacket> void register(Class<T> type, Supplier<T> supplier) {
        int id = id();
        if (DECODERS.containsKey(id)) {
            throw new IllegalStateException("Duplicate AU menu packet id: " + id);
        }
        if (IDS.containsKey(type)) {
            throw new IllegalStateException("Duplicate AU menu packet class: " + type.getName());
        }
        DECODERS.put(id, supplier);
        IDS.put(type, id);
    }

    public static void encodeTyped(RegistryFriendlyByteBuf buffer, AUMenuPacket packet) {
        Integer id = IDS.get(packet.getClass());
        if (id == null) {
            throw new IllegalStateException("Unregistered AU menu packet class: " + packet.getClass().getName());
        }
        buffer.writeVarInt(id);
        packet.encode(buffer);
    }

    public static AUMenuPacket decodeTyped(RegistryFriendlyByteBuf buffer) {
        int id = buffer.readVarInt();
        Supplier<? extends AUMenuPacket> supplier = DECODERS.get(id);
        if (supplier == null) {
            throw new IllegalStateException("Unknown AU menu packet id: " + id);
        }

        AUMenuPacket packet = supplier.get();
        packet.decode(buffer);
        return packet;
    }

    public abstract void encode(RegistryFriendlyByteBuf buffer);
    protected abstract void decode(RegistryFriendlyByteBuf buffer);

}

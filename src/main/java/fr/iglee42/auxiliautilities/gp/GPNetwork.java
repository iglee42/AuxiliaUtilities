package fr.iglee42.auxiliautilities.gp;

import java.util.*;

public class GPNetwork {
    private final UUID id;
    private final Set<GPHolder> generators = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<GPHolder> consumers = Collections.newSetFromMap(new IdentityHashMap<>());

    private int cachedGeneration;
    private int cachedConsumption;
    private boolean generationDirty = true;
    private boolean consumptionDirty = true;

    public GPNetwork(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void registerGenerator(GPHolder generator) {
        if (generator == null || !id.equals(generator.getNetworkId())) return;
        if (generators.add(generator)) {
            markGeneratorDirty();
        }
    }

    public void unregisterGenerator(GPHolder generator) {
        if (generator == null) return;
        if (generators.remove(generator)) {
            if (generator instanceof GPItemHolder) ((GPItemHolder) generator).onRemoved();
            markGeneratorDirty();
        }
    }

    public void registerConsumer(GPHolder consumer) {
        if (consumer == null || !id.equals(consumer.getNetworkId())) return;
        if (consumers.add(consumer)) {
            markConsumerDirty();
        }
    }

    public void unregisterConsumer(GPHolder consumer) {
        if (consumer == null) return;
        if (consumers.remove(consumer)) {
            if (consumer instanceof GPItemHolder) ((GPItemHolder) consumer).onRemoved();
            markConsumerDirty();
        }
    }

    public void markGeneratorDirty() {
        generationDirty = true;
        GPNetworkManager.INSTANCE.syncToPlayer(this);
    }

    public void markConsumerDirty() {
        consumptionDirty = true;
        GPNetworkManager.INSTANCE.syncToPlayer(this);
    }

    public int getTotalGeneration() {
        if (generationDirty) {
            cachedGeneration = generators.stream().mapToInt(GPHolder::getGPGeneration).sum();
            generationDirty = false;
        }
        return cachedGeneration;
    }

    public int getTotalConsumption() {
        if (consumptionDirty) {
            cachedConsumption = consumers.stream().mapToInt(GPHolder::getGPConsumption).sum();
            consumptionDirty = false;
        }
        return cachedConsumption;
    }

    public List<GPHolder> getHolders(){
        List<GPHolder> list = new ArrayList<>();
        list.addAll(generators);
        list.addAll(consumers);
        return list;
    }

    public boolean hasEnoughPower() {
        return getTotalGeneration() >= getTotalConsumption();
    }

}


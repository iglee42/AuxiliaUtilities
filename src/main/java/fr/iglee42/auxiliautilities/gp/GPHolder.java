package fr.iglee42.auxiliautilities.gp;

import java.util.UUID;

/**
 * Shared GP capability contract used by generators and consumers.
 */
public interface GPHolder {
    int getGPGeneration();

    int getGPConsumption();

    UUID getNetworkId();

    String name();

    default int getGPBalance() {
        return getGPGeneration() - getGPConsumption();
    }
}


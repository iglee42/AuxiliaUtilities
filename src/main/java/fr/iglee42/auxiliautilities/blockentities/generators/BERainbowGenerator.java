package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.config.AUConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BERainbowGenerator extends AUBlockEntity {

    private int extractBuffer = 0;
    private boolean providing = false;

    private final IEnergyStorage energy = new IEnergyStorage() {

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {

            if (!providing || maxExtract <= 0)
                return 0;

            int extracted = Math.min(maxExtract, extractBuffer);

            if (!simulate)
                extractBuffer -= extracted;

            return extracted;
        }

        @Override public int receiveEnergy(int maxReceive, boolean simulate) { return 0; }

        @Override public int getEnergyStored() { return providing ? AUConfig.RAINBOW_RATE.get() : 0; }

        @Override public int getMaxEnergyStored() { return AUConfig.RAINBOW_RATE.get(); }

        @Override public boolean canExtract() { return true; }

        @Override public boolean canReceive() { return false; }
    };

    public BERainbowGenerator(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.RAINBOW_GENERATOR.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,AUBlockEntityTypes.RAINBOW_GENERATOR.get(),
                (be, side) -> be.energy);
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {

        extractBuffer = 0;

        // Machine inactive
        /*if (!active) {
            if (providing) setChanged();
            providing = false;
            return;
        }*/

        List<BlockPos> generators = new ArrayList<>();

        BlockPos.betweenClosedStream(new AABB(pos).inflate(AUConfig.RAINBOW_RANGE.get())).forEach(p -> {
            BlockEntity be = level.getBlockEntity(p);
            if (be instanceof AUGeneratorBlockEntity generator) {
               if (generator.getRainbowPos() == null)
                   generator.setRainbowPos(worldPosition);
               if (generator.getRainbowPos().equals(worldPosition))
                    generators.add(p.immutable());
            }
        });

        List<BlockEntityType<?>> remaining = new ArrayList<>(AUBlockEntityTypes.getGenerators().stream().map(DeferredHolder::get).toList());
        for (BlockPos genPos : generators) {
            if (!(level.getBlockEntity(genPos) instanceof AUGeneratorBlockEntity generator))
                continue;
            BlockEntityType<?> type = generator.getType();
            if (!remaining.contains(type)) continue;
            remaining.remove(type);
        }

        if (!remaining.isEmpty()){
            providing = false;
            return true;
        }

        List<BlockEntityType<?>> missingActives = new ArrayList<>(AUBlockEntityTypes.getGenerators().stream().map(DeferredHolder::get).toList());
        for (BlockPos genPos : generators) {
            if (!(level.getBlockEntity(genPos) instanceof AUGeneratorBlockEntity generator))
                continue;
            BlockEntityType<?> type = generator.getType();
            if (!missingActives.contains(type)) continue;
            if (!generator.isActive()) continue;
            missingActives.remove(type);
        }

        if (!missingActives.isEmpty()){
            providing = false;
            return true;
        }
        providing = true;

        extractBuffer = AUConfig.RAINBOW_RATE.get();

        distributeEnergy();
        return true;
    }

    private void distributeEnergy() {

        if (level == null)
            return;
        for (Direction dir : Direction.values()) {
            IEnergyStorage storage = level.getCapability(
                    Capabilities.EnergyStorage.BLOCK,
                    worldPosition.relative(dir),
                    dir.getOpposite()
            );

            if (storage == null || !storage.canReceive())
                continue;

            int extracted = storage.receiveEnergy(extractBuffer, false);

            extractBuffer -= extracted;

            if (extractBuffer <= 0) {
                extractBuffer = 0;
                break;
            }
        }
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        if (forClient) tag.putBoolean("Providing", providing);
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        if (tag.contains("Providing")) providing = tag.getBoolean("Providing");
    }

    public boolean isProviding() {
        return providing;
    }

}

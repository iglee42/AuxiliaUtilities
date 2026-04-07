package fr.iglee42.auxiliautilities.blockentities.drums;

import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.items.AUDataComponents;
import fr.iglee42.auxiliautilities.utils.StoredFluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class BEDrum extends AUBlockEntity {

    private final Type drumType;

    private final FluidTank tank;

    public BEDrum(BlockEntityType<?> type, BlockPos pos, BlockState state, Type drumType) {
        super(type, pos, state);
        this.drumType = drumType;
        this.tank = new FluidTank(drumType.getCapacity()){
            @Override
            public FluidStack getFluidInTank(int tank) {
                return drumType == Type.CREATIVE ? super.getFluidInTank(tank).copyWithAmount(Integer.MAX_VALUE): super.getFluidInTank(tank);
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                if (drumType == Type.CREATIVE) {
                    setFluid(resource.copyWithAmount(Integer.MAX_VALUE));
                    onContentsChanged();
                }
                return drumType == Type.CREATIVE ? resource.getAmount() : super.fill(resource, action);
            }

            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                return drumType == Type.CREATIVE ? super.getFluidInTank(0).copyWithAmount(maxDrain) : super.drain(maxDrain, action);
            }

            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                return drumType == Type.CREATIVE && FluidStack.isSameFluidSameComponents(getFluid(),resource) ? resource.copyWithAmount(resource.getAmount()) : super.drain(resource, action);
            }

            @Override
            protected void onContentsChanged() {
                super.onContentsChanged();
                setChanged();
            }
        };
    }

    public Type getDrumType() {
        return drumType;
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        tank.readFromNBT(registries, tag.getCompound("Tank"));
        if (EffectiveSide.get().isClient()) {
            Minecraft.getInstance().levelRenderer.setBlocksDirty(getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ(),getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ());
        }
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        tag.put("Tank", tank.writeToNBT(registries,new CompoundTag()));
    }

    @Override
    public void destroy() {}

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(AUDataComponents.STORED_FLUID, new StoredFluidStack(tank.getFluid()));
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput input) {
        super.applyImplicitComponents(input);
        StoredFluidStack stored = input.get(AUDataComponents.STORED_FLUID);
        if (stored != null){
            tank.setFluid(stored.stack());
        }
    }

    public static enum Type{
        STONE(16_000),
        COPPER(64_000),
        IRON(256_000),
        REINFORCED_LARGE(4_096_000),
        NETHERITE(16_384_000),
        DEMONICALLY_GARGANTUAN(65_536_000),
        CREATIVE(Integer.MAX_VALUE);

        private final int capacity;

        Type(int capacity) {
            this.capacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }
    }
}

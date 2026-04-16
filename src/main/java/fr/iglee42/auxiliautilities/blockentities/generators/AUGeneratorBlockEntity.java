package fr.iglee42.auxiliautilities.blockentities.generators;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blocks.BlockCrusher;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUEnergyWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUFluidTankWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUTimedProgressWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.recipes.CrusherRecipe;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public abstract class AUGeneratorBlockEntity extends AUBlockEntity {

    protected final ItemStackHandler itemHandler = new ItemStackHandler(requiredItems()){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return AUGeneratorBlockEntity.this.isItemValid(slot,stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return AUGeneratorBlockEntity.this.getSlotLimit(slot);
        }
    };
    protected final FluidTank fluidTank = new FluidTank(Math.max(4_000,requiredFluidAmount() * 4),this::isFluidValid);

    protected final EnergyStorage energyStorage = new EnergyStorage(getEnergyStorageCapacity(), getEnergyStorageCapacity() / 10) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    protected int maxBurnTime;
    protected int burnTime;
    protected boolean active;

    protected ItemStack usedItem = ItemStack.EMPTY;
    protected FluidStack usedFluid = FluidStack.EMPTY;

    private BlockPos rainbowPos = null;

    public AUGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        AUBlockEntityTypes.getGenerators()
                .stream()
                .filter(holder -> holder.get() instanceof BlockEntityType)
                .map(holder -> (BlockEntityType<?>) holder.get())
                .forEach(type->{
                    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,type,(be,dir)->{
                        if (be instanceof AUGeneratorBlockEntity genbe){
                            if (genbe.requiredItems() > 0)
                                return genbe.getItemHandler();
                        }
                        return null;
                    });

                    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,type,(be,dir)->{
                        if (be instanceof AUGeneratorBlockEntity genbe){
                            if (genbe.requiredFluidAmount() > 0)
                                return genbe.getFluidTank();
                        }
                        return null;
                    });

                    event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,type,(be,dir)->{
                        if (be instanceof AUGeneratorBlockEntity genbe){
                            return genbe.getEnergyStorage();
                        }
                        return null;
                    });
                });

    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        sendEnergy(level,pos);
        boolean isLit = state.getValue(BlockCrusher.LIT);
        if (isLit != active) {
            level.setBlock(pos, state.setValue(BlockCrusher.LIT, active), 3);
        }
        active = /*energyStorage.receiveEnergy(getEnergyPerProgress(usedItem,usedFluid),true) > 0 &&*/ burnTime > 0;
        if (active){
            energyStorage.receiveEnergy(getEnergyPerProgress(usedItem, usedFluid), false);
            burnTime--;
            burnTick(level,pos,state);
        }
        if (burnTime == 0){
            maxBurnTime = 0;
            if (requiredItems()>0)
            {
                for (int i = 0; i < requiredItems(); i++) {
                    ItemStack extracted = itemHandler.extractItem(i, getRequiredItemForSlot(i), true);
                    if (extracted.getCount() < getRequiredItemForSlot(i))
                        return true;
                }
            }

            if (requiredFluidAmount() > 0){
                if (fluidTank.drain(requiredFluidAmount(), IFluidHandler.FluidAction.SIMULATE).isEmpty())
                    return true;
            }

            int itemsBurnTime = 0;
            for (int i = 0; i < requiredItems(); i++) {
                itemsBurnTime += getProgressPerItem(i,itemHandler.getStackInSlot(i));
            }
            maxBurnTime = itemsBurnTime + getProgressPerFluid(fluidTank.getFluid());
            if (maxBurnTime > 0){
                if (requiredItems() > 0)
                {
                    usedItem = itemHandler.getStackInSlot(0).copy();
                    for (int i = 0; i < requiredItems(); i++) {
                        itemHandler.extractItem(i,getRequiredItemForSlot(i),false);
                        if (!getReturnItem(i,usedItem).isEmpty()){
                            itemHandler.setStackInSlot(i,getReturnItem(i,usedItem));
                        }
                    }
                }
                if (requiredFluidAmount() > 0){
                    usedFluid = fluidTank.getFluid().copy();
                    fluidTank.drain(requiredFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
                }
                burnTime = maxBurnTime;
            }
        }
        return true;
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        if (requiredItems() > 0)
            tag.put("DeathItems", itemHandler.serializeNBT(registries));
        if (requiredFluidAmount() > 0)
            tag.put("Fluid", fluidTank.writeToNBT(registries,new CompoundTag()));
        tag.put("Energy", energyStorage.serializeNBT(registries));
        if (forClient) tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putInt("BurnTime", burnTime);
        tag.putBoolean("Active", active);

        tag.put("UsedItem", usedItem.saveOptional(registries));
        tag.put("UsedFluid", usedFluid.saveOptional(registries));
        if (rainbowPos != null) tag.put("RainbowPos", NbtUtils.writeBlockPos(rainbowPos));
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        if (tag.contains("DeathItems"))
            itemHandler.deserializeNBT(registries,tag.getCompound("DeathItems"));
        if (tag.contains("Fluid"))
            fluidTank.readFromNBT(registries,tag.getCompound("Fluid"));
        if (tag.contains("Energy"))
            energyStorage.deserializeNBT(registries,tag.get("Energy"));
        if (tag.contains("MaxBurnTime"))
            maxBurnTime = tag.getInt("MaxBurnTime");
        burnTime = tag.getInt("BurnTime");
        active = tag.getBoolean("Active");

        if (tag.contains("UsedItem"))
            usedItem = ItemStack.parseOptional(registries,tag.getCompound("UsedItem"));
        if (tag.contains("UsedFluid"))
            usedFluid = FluidStack.parseOptional(registries,tag.getCompound("UsedFluid"));
        NbtUtils.readBlockPos(tag,"RainbowPos").ifPresent(pos->rainbowPos = pos);
    }

    protected boolean isItemValid(int slot, ItemStack stack){
        if (getItemDataMapType() != null){
            Holder<Item> itemHolder = stack.getItemHolder();
            DataMapType<Item, ? extends AUGeneratorsDataMaps.MapItem> mapType = getItemDataMapType();
            AUGeneratorsDataMaps.MapItem data = itemHolder.getData(mapType);
            return data != null;
        }
        return false;
    }
    protected int requiredItems() {
        return 0;
    }
    protected int getSlotLimit(int slot){
        return Item.ABSOLUTE_MAX_STACK_SIZE;
    }

    protected ItemStack getReturnItem(int slot, ItemStack stack) {
        return ItemStack.EMPTY;
    }

    protected boolean isFluidValid(FluidStack stack){
        return false;
    }
    protected int requiredFluidAmount() {
        return 0;
    }

    protected int getRequiredItemForSlot(int slot){
        return Math.min(getSlotLimit(slot),1);
    }

    protected int getEnergyStorageCapacity() {
        return 100_000;
    }

    protected int getProgressPerItem(int slot,ItemStack stack) {
        if (getItemDataMapType() != null){
            Holder<Item> itemHolder = stack.getItemHolder();
            DataMapType<Item, ? extends AUGeneratorsDataMaps.MapItem> mapType = getItemDataMapType();
            AUGeneratorsDataMaps.MapItem data = itemHolder.getData(mapType);
            if (data != null) return data.time();
        }
        return 0;
    }

    protected int getProgressPerFluid(FluidStack stack) {
        return 0;
    }

    protected int getEnergyPerProgress(ItemStack item, FluidStack fluid) {
        if (!item.isEmpty() && getItemDataMapType() != null){
            Holder<Item> itemHolder = item.getItemHolder();
            DataMapType<Item, ? extends AUGeneratorsDataMaps.MapItem> mapType = getItemDataMapType();
            AUGeneratorsDataMaps.MapItem data = itemHolder.getData(mapType);
            if (data != null) return data.energyPerTick();
        }
        return 10;
    }

    protected void burnTick(ServerLevel level, BlockPos pos, BlockState state) {}

    public boolean isActive() {
        return active;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    protected <T extends AUGeneratorsDataMaps.MapItem> DataMapType<Item,T> getItemDataMapType(){
        return null;
    }

    void setRainbowPos(BlockPos pos) {
        this.rainbowPos = pos;
        setChanged();
    }

    public BlockPos getRainbowPos() {
        return rainbowPos;
    }

    public void sendEnergy(ServerLevel level,BlockPos pos){
        if (energyStorage.getEnergyStored() <= 0) return;
        int maxSend = energyStorage.getEnergyStored();
        LinkedHashSet<IEnergyStorage> receivers = new LinkedHashSet<>();
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            IEnergyStorage neighborEnergy = level.getCapability( Capabilities.EnergyStorage.BLOCK,neighborPos, dir.getOpposite());
            if (neighborEnergy != null){
                receivers.add(neighborEnergy);
            }
        }
        if (receivers.isEmpty()) return;
        int toSend = maxSend / receivers.size();
        if (toSend > 0)
            for (IEnergyStorage receiver : receivers){
                int energy = receiver.receiveEnergy(toSend,false);
                maxSend -= energy;
                energyStorage.setEnergy(energyStorage.getEnergyStored() - energy);
                if (maxSend <= 0) break;
            }
        for (IEnergyStorage receiver : receivers){
            int energy = receiver.receiveEnergy(maxSend,false);
            maxSend -= energy;
            energyStorage.setEnergy(energyStorage.getEnergyStored() - energy);
            if (maxSend <= 0) break;
        }
        setChanged();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new GeneratorMenu(id, playerInv, this);
    }

    public class GeneratorMenu extends AUBEMenu{

        protected GeneratorMenu(int id,Inventory playerInv, AUGeneratorBlockEntity blockEntity) {
            super(AUMenus.GENERATORS.get(), id, blockEntity);


            addTitle();
            AUTimedProgressWidget progressWidget = new AUTimedProgressWidget(94,42) {
                @Override
                protected float getTime() {
                    return maxBurnTime - burnTime;
                }

                @Override
                protected float getMaxTime() {
                    return maxBurnTime;
                }

            };
            progressWidget.jeiCategory(BuiltInRegistries.BLOCK.getKey(blockEntity.getBlockState().getBlock()));
            addWidget(progressWidget);
            addWidget(new AUEnergyWidget(120, 24,energyStorage));

            int count = blockEntity.requiredItems() + (blockEntity.requiredFluidAmount() > 0 ? 1 : 0);
            if (count > 0) {
                int inputStartX = 70 - (count - 1) * 20;
                int inputIndex = 0;

                for (int slot = 0; slot < blockEntity.requiredItems(); slot++) {
                    addWidget(new SlotItemHandlerWidget(itemHandler, slot, inputStartX + inputIndex * 20, 42));
                    inputIndex++;
                }

                if (blockEntity.requiredFluidAmount() > 0) {
                    addWidget(new AUFluidTankWidget(inputStartX + inputIndex * 20, 34, fluidTank));
                }
            }

            cropAndAddPlayerSlots(playerInv);
            validate();
        }
    }
}

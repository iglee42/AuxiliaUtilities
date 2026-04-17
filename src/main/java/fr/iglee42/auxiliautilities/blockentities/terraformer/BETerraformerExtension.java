package fr.iglee42.auxiliautilities.blockentities.terraformer;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.items.IItemHandlerFilterInsertion;
import fr.iglee42.auxiliautilities.blockentities.items.SingleItemStackHandler;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUEnergyWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUProgressWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUTextWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BETerraformerExtension extends AUBlockEntity {

    private final EnergyStorage energyStorage = new EnergyStorage(20000,2000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    public static final int INCREASE_MULTIPLIER = 40;

    static final int POWER_PER_OPERATION = 1000;

    public Boolean hasAntenna;
    int sprinkerActive = 0;

    int tfEnergy = 0;
    private final TerraformerType terraformerType;

    private IItemHandlerFilterInsertion<SingleItemStackHandler> itemHandler = new IItemHandlerFilterInsertion<>(new SingleItemStackHandler()) {
        @Override
        public boolean isValid(@NotNull ItemStack stack) {
            if (stack.isEmpty()) return true;
            Holder<Item> itemHolder = stack.getItemHolder();
            return itemHolder.getData(terraformerType.getDataMapType()) != null;
        }
    };

    public BETerraformerExtension(BlockEntityType<?> type, BlockPos pos, BlockState state, TerraformerType terraformerType) {
        super(type, pos, state);
        this.terraformerType = terraformerType;
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        beCapability(event, AUBlockEntityTypes.COOLER.get());
        beCapability(event, AUBlockEntityTypes.HEATER.get());
        beCapability(event, AUBlockEntityTypes.HUMIDIFIER.get());
        beCapability(event, AUBlockEntityTypes.DEHUMIDIFIER.get());
        beCapability(event, AUBlockEntityTypes.MAGIC_INFUSER.get());
        beCapability(event, AUBlockEntityTypes.MAGIC_ABSORBER.get());
        beCapability(event, AUBlockEntityTypes.DESHOSTILIFIER.get());
    }

    private static void beCapability(RegisterCapabilitiesEvent event,BlockEntityType<BETerraformerExtension> type){
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,type,(be,dir)->be.getItemHandler());
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK,type,(be,dir)->be.getEnergyStorage());
    }

    public IItemHandlerFilterInsertion<SingleItemStackHandler> getItemHandler() {
        return itemHandler;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        tag.putInt("Level", tfEnergy);
        tag.put("Energy", energyStorage.serializeNBT(registries));
        tag.put("Inventory", itemHandler.original.serializeNBT(registries));
        if (forClient)
            tag.putInt("SprinkerActive", sprinkerActive);
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        tfEnergy = tag.getInt("Level");
        energyStorage.deserializeNBT(registries,tag.get("Energy"));
        itemHandler.original.deserializeNBT(registries,tag.getCompound("Inventory"));
        if (tag.contains("SprinkerActive"))
            sprinkerActive = tag.getInt("SprinkerActive");
    }

    public TerraformerType getTerraformerType() {
        return terraformerType;
    }

    private boolean hasAntennaAbove(){
        return this.getLevel().getBlockState(this.getBlockPos().above()).is(AUBlocks.ANTENNA);
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (this.sprinkerActive > 0){
            sprinkerActive--;
            setChanged();
        }
        if (this.hasAntenna == null)
            this.hasAntenna = hasAntennaAbove();
        else if (this.hasAntenna != hasAntennaAbove())
            this.hasAntenna = hasAntennaAbove();
        if (!hasAntenna)
            return true;
        ItemStack stack = this.itemHandler.original.extractItem(0,1,true);
        if (InventoryHelper.isStackNotEmpty(stack)){
            int increase = 0;
            Holder<Item> itemHolder = stack.getItemHolder();
            if (itemHolder.getData(terraformerType.getDataMapType()) != null){
                increase = itemHolder.getData(terraformerType.getDataMapType()).energyProvided();
            }
            if (increase > 0 ){
                increase *= INCREASE_MULTIPLIER;
                if (this.tfEnergy > increase / 2)
                    return true;
                if (this.energyStorage.extractEnergy(POWER_PER_OPERATION,true) < POWER_PER_OPERATION)
                    return true;
                this.energyStorage.extractEnergy(POWER_PER_OPERATION,false);
                this.itemHandler.original.extractItem(0,1,false);
                this.tfEnergy += increase;
            }
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void clientTick(Level level, BlockPos pos, BlockState state) {
        if (sprinkerActive > 0 && hasAntennaAbove()){
            int[] colors = this.terraformerType.getColors();
            for (int i = 0; i < 10; i++){
                RandomSource rand = level.getRandom();
                Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
                double x = getBlockPos().getX() + 0.5 + ((dir.getStepX() * 5) + rand.nextFloat() * 2 - 1) / 16;
                double y = (getBlockPos().getY() + 1) + ((5*rand.nextFloat() * 11.5D) / 16);
                double z = getBlockPos().getZ() + 0.5 + ((dir.getStepZ() * 5) + rand.nextFloat() * 2 - 1) / 16;
                Vector3f color = Vec3.fromRGB24(colors[rand.nextInt(colors.length)]).toVector3f();
                level.addParticle(new DustParticleOptions(color,1f),x,y,z,0,0,0);
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MenuTerraformerExtension(id, playerInv, this);
    }

    public class MenuTerraformerExtension extends AUBEMenu {

        public MenuTerraformerExtension(int id, Inventory playerInv, AUBlockEntity be) {
            super(AUMenus.TERRAFORMER_EXTENSION.get(), id, be);

            addTitle();
            AUProgressWidget w = new AUProgressWidget(74,30,(byte)0);
            addWidget(w);
            addWidget(new SlotItemHandlerWidget(itemHandler.getGUIVariant(),0,w.getX() -22,29));
            addWidget(new AUTextWidget(w.getX() + w.getWidth()+ 6,34,1) {
                @Override
                protected Component getMessage() {
                    return Component.literal(tfEnergy + "TF");
                }
            });
            addWidget(new AUTextWidget(50,56,1) {
                @Override
                protected Component getMessage() {
                    if (!hasAntennaAbove())
                        return AULang.MISSING_ANTENNA_GUI_MESSAGE.get().withStyle(ChatFormatting.RED);
                    return Component.empty();
                }
            });
            addWidget(new AUEnergyWidget(this.width - 4 -18,17,energyStorage));
            cropAndAddPlayerSlots(playerInv);
            validate();
        }
    }

}

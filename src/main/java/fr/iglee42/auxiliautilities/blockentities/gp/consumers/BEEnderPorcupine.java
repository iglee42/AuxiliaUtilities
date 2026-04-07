package fr.iglee42.auxiliautilities.blockentities.gp.consumers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUTextWidget;
import fr.iglee42.auxiliautilities.menu.widgets.UpDownIntSelectorWidgets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BEEnderPorcupine extends AUGPConsumerBlockEntity {

    private static final Function<BlockPos, Integer> GET_X = BlockPos::getX;
    private static final Function<BlockPos, Integer> GET_Y = BlockPos::getY;
    private static final Function<BlockPos, Integer> GET_Z = BlockPos::getZ;
    private static final BiConsumer<BlockPos.MutableBlockPos, Integer> SET_POS_X = BlockPos.MutableBlockPos::setX;
    private static final BiConsumer<BlockPos.MutableBlockPos, Integer> SET_POS_Y = BlockPos.MutableBlockPos::setY;
    private static final BiConsumer<BlockPos.MutableBlockPos, Integer> SET_POS_Z = BlockPos.MutableBlockPos::setZ;

    private static final Set<BlockCapability<?, Direction>> PROXIED_CAPABILITIES = Set.of(
            Capabilities.ItemHandler.BLOCK,
            Capabilities.FluidHandler.BLOCK,
            Capabilities.EnergyStorage.BLOCK
    );
    private BlockPos.MutableBlockPos targetA = new BlockPos.MutableBlockPos();
    private BlockPos.MutableBlockPos targetB = new BlockPos.MutableBlockPos();
    private BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos();

    private int consumption = 0;

    public BEEnderPorcupine(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.ENDER_PORCUPINE.get(), pos, state);
    }

    @SubscribeEvent
    public static <T> void onCapabilitiesRegister(RegisterCapabilitiesEvent event){
        PROXIED_CAPABILITIES.forEach(cap->{
            BlockCapability<T, Direction> capability = (BlockCapability<T, Direction>) cap;
            event.registerBlockEntity(
                    capability,
                    AUBlockEntityTypes.ENDER_PORCUPINE.get(),
                    (be,ctx)-> {
                        Optional<Pair<Level,BlockPos>> targetOpt = be.getTargetPos();
                        if (!targetOpt.isPresent()) return null;
                        return targetOpt.get().getFirst().getCapability(capability,targetOpt.get().getSecond(),ctx);
                    }
            );
        });
    }

    @Override
    public int getGPConsumption() {
        return consumption;
    }

    @Override
    public void destroy() {}

    public Optional<Pair<Level, BlockPos>> getTargetPos() {
        verifyPos();
        if (this.target.equals(BlockPos.ZERO))
            return Optional.empty();
        return Optional.of(Pair.of(this.level, getBlockPos().offset(target.immutable())));
    }

    public BlockPos getTargetOffset() {
        return target.immutable();
    }

    private void verifyPos(){
        this.target.set(
                Mth.clamp(target.getX(),Math.min(targetA.getX(), targetB.getX()),Math.max(targetA.getX(), targetB.getX())),
                Mth.clamp(target.getY(),Math.min(targetA.getY(), targetB.getY()),Math.max(targetA.getY(), targetB.getY())),
                Mth.clamp(target.getZ(),Math.min(targetA.getZ(), targetB.getZ()),Math.max(targetA.getZ(), targetB.getZ()))
        );
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        tag.put("TargetA", NbtUtils.writeBlockPos(targetA));
        tag.put("TargetB", NbtUtils.writeBlockPos(targetB));
        tag.put("Target", NbtUtils.writeBlockPos(target));
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        this.targetA = NbtUtils.readBlockPos(tag,"TargetA").orElse(BlockPos.ZERO).mutable();
        this.targetB = NbtUtils.readBlockPos(tag,"TargetB").orElse(BlockPos.ZERO).mutable();
        this.target = NbtUtils.readBlockPos(tag,"Target").orElse(BlockPos.ZERO).mutable();
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        if (level.getGameTime() % 20 == 0){
            nextPos();
            consumption = Math.abs(target.getX()) + Math.abs(target.getY()) + Math.abs(target.getZ());
            level.updateNeighborsAt(getBlockPos(),getBlockState().getBlock());
        }
        return true;
    }

    public BlockPos getTargetA() {
        return targetA.immutable();
    }

    public BlockPos getTargetB() {
        return targetB.immutable();
    }

    public void nextPos() {
        verifyPos();
        if (tryAdvance(GET_X, SET_POS_X) &&
                tryAdvance(GET_Z, SET_POS_Z))
                tryAdvance(GET_Y, SET_POS_Y);
    }

    protected boolean tryAdvance(Function<BlockPos, Integer> getter, BiConsumer<BlockPos.MutableBlockPos, Integer> setter){
        int current = getter.apply(target);
        current--;
        if (current < getter.apply(targetA) && current < getter.apply(targetB)){
            current = Math.max(getter.apply(targetA), getter.apply(targetB));
            setter.accept(target,current);
            return true;
        }
        setter.accept(target,current);
        return false;
    }


    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new PorcupineMenu(id, this);
    }

    public class PorcupineMenu extends AUBEMenu{

        private static final NumberFormat nf = NumberFormat.getIntegerInstance(Locale.UK);

        protected PorcupineMenu(int id, AUBlockEntity blockEntity) {
            super(AUMenus.ENDER_PORCUPINE.get(), id, blockEntity);

            addTitle();
            crop();
            addWidget(new AUTextWidget(5,this.height,1) {
                @Override
                protected Component getMessage() {
                    return AULang.ORIGIN_GUI_MESSAGE.get(
                            nf.format(getBlockPos().getX()),
                            nf.format(getBlockPos().getY()),
                            nf.format(getBlockPos().getZ())
                    );
                }
            });
            crop();
            addWidget(new AUTextWidget(5,this.height,1) {
                @Override
                protected Component getMessage() {
                    BlockPos target = getBlockPos().offset(BEEnderPorcupine.this.target);
                    return AULang.SCANNING_GUI_MESSAGE.get(
                            nf.format(target.getX()),
                            nf.format(target.getY()),
                            nf.format(target.getZ())
                    );
                }
            });
            crop(5);
            int w = 45;
            for (int i = 0; i<2; i++){
                AtomicInteger x = new AtomicInteger(4);
                final int finalI = i;
                Supplier<BlockPos.MutableBlockPos> pos = new Supplier[]{()->BEEnderPorcupine.this.targetA, ()->BEEnderPorcupine.this.targetB}[finalI];
                addWidget(new AUTextWidget(x.get(),this.height,1) {
                    @Override
                    protected Component getMessage() {
                        return finalI == 0 ? AULang.RANGE_START_GUI_MESSAGE.get(): AULang.RANGE_END_GUI_MESSAGE.get();
                    }
                });
                crop();
                ImmutableList.of(Pair.of(GET_X, SET_POS_X), Pair.of(GET_Y, SET_POS_Y), Pair.of(GET_Z, SET_POS_Z)).forEach(pair->{
                    (new UpDownIntSelectorWidgets(x.get(),this.height,w){

                        @Override
                        public int getValue() {
                            return pair.getFirst().apply(pos.get());
                        }

                        @Override
                        public void setValue(int value) {
                            pair.getSecond().accept(pos.get(), value);
                        }
                    }).forEach(this::addWidget);
                    x.addAndGet(w+4);
                });
                crop();
                this.height += 8;
            }
            crop();
            validate();
        }
    }

}

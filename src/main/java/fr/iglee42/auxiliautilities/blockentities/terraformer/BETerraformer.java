package fr.iglee42.auxiliautilities.blockentities.terraformer;

import com.mojang.serialization.Codec;
import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.*;
import fr.iglee42.auxiliautilities.utils.AUExtraCodecs;
import fr.iglee42.auxiliautilities.utils.BiomeHelper;
import fr.iglee42.igleelib.api.blockentities.EnergyStorage;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.ImmutableList;
import oshi.util.tuples.Pair;
import fr.iglee42.auxiliautilities.items.ItemBiomeMarker.SingleBiomeStackHandler;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BETerraformer extends AUBlockEntity {

    private static final Codec<Object2IntOpenHashMap<TerraformerType>> TF_ENERGY_CODEC = Codec.unboundedMap(TerraformerType.CODEC,ExtraCodecs.POSITIVE_INT).xmap(Object2IntOpenHashMap::new, Function.identity());

    static final int MAX_CONTAINER_RANGE = 4;
    static final int MAX_TRANSFORMATION_RANGE = 64;
    static final int POWER_PER_TICK = 80;

    private BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
    private final EnergyStorage energyStorage = new EnergyStorage(16000,16000) {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };
    private final SingleBiomeStackHandler biomeHandler = new SingleBiomeStackHandler();
    private int range = 0;
    private int transformTime = 0;
    private int findingCooldown = 0;
    Object2IntOpenHashMap<TerraformerType> energyPerType = new Object2IntOpenHashMap<>();
    EnumSet<TerraformerType> presentTypes = EnumSet.noneOf(TerraformerType.class);

    public BETerraformer(BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.TERRAFORMER.get(), pos, state);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AUBlockEntityTypes.TERRAFORMER.get(), (be, dir) -> be.getEnergyStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AUBlockEntityTypes.TERRAFORMER.get(), (be, dir) -> be.getBiomeHandler());
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public SingleBiomeStackHandler getBiomeHandler() {
        return biomeHandler;
    }

    private static boolean isHostile(Biome biome){
        List<MobSpawnSettings.SpawnerData> spawns = biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap().stream().map(Weighted::value).toList();
        return !spawns.isEmpty();
    }

    public static Object2IntOpenHashMap<TerraformerType> getTransformationRequirements(Holder<Biome> current, Holder<Biome> target){
        Object2IntOpenHashMap<TerraformerType> map = new Object2IntOpenHashMap<>(10,0.5f);
        float tempDiff = target.value().getModifiedClimateSettings().temperature() - current.value().getModifiedClimateSettings().temperature();
        float rainDiff = target.value().getModifiedClimateSettings().downfall() - current.value().getModifiedClimateSettings().downfall();
        if (tempDiff > 0.0f){
          int v = (int) Math.floor((tempDiff * 15));
          map.addTo(TerraformerType.HEATER, v);
        } else if (tempDiff < 0.0f){
            int v = (int) Math.floor((-tempDiff * 15));
            map.addTo(TerraformerType.COOLER, v);
        }
        if (rainDiff > 0.0f){
            int v = (int) Math.floor((rainDiff * 20));
            map.addTo(TerraformerType.HUMIDIFIER, v);
        } else if (rainDiff < 0.0f){
            int v = (int) Math.floor((-rainDiff * 20));
            map.addTo(TerraformerType.DEHUMIDIFIER, v);
        }
        Set<TagKey<Biome>> removedTraits = getTraitsDiff(target, current);
        Set<TagKey<Biome>> addedTraits = getTraitsDiff(current, target);
        alter(map,addedTraits,removedTraits,TerraformerType.HUMIDIFIER,TerraformerType.DEHUMIDIFIER,4,4, Tags.Biomes.IS_JUNGLE, Tags.Biomes.IS_WET,Tags.Biomes.IS_LUSH);
        alter(map,addedTraits,removedTraits,TerraformerType.DEHUMIDIFIER,TerraformerType.HUMIDIFIER,4,4, Tags.Biomes.IS_DEAD, Tags.Biomes.IS_DRY,Tags.Biomes.IS_SAVANNA);
        alter(map,addedTraits,removedTraits,TerraformerType.MAGIC_INFUSER,TerraformerType.MAGIC_INFUSER,10,10, Tags.Biomes.IS_MAGICAL, Tags.Biomes.IS_END,Tags.Biomes.IS_NETHER);
        for (Pair<TerraformerType, TerraformerType> opposite : TerraformerType.OPPOSITES) {
            int valA = map.getOrDefault(opposite.getA(), 0);
            int valB = map.getOrDefault(opposite.getB(), 0);
            if (valA == 0 && valB == 0) continue;
            if (valA == valB){
                map.removeInt(opposite.getA());
                continue;
            }
            if (valA < valB){
                map.removeInt(opposite.getA());
                continue;
            }
            if (valB < valA){
                map.removeInt(opposite.getB());
            }
        }
        if(!isHostile(target.value())&&isHostile(current.value())) {
            map.addTo(TerraformerType.DESHOSTILIFIER, 10);
        }
        return map;
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean oldReturn = super.serverTick(level, pos, state);
        Holder<Biome> target = biomeHandler.getBiome();
        if (target == null || !target.isBound()) return oldReturn;
        if (range < 0 ) return oldReturn;
        if (targetPos.equals(BlockPos.ZERO)){
            if (findingCooldown > 0){
                findingCooldown--;
                return true;
            }
            BlockPos.MutableBlockPos tempPos = new BlockPos.MutableBlockPos();
            int range = Mth.clamp(this.range,0,AUConfig.TERRAFORMER_RANGE.get());
            RandomSource random = level.getRandom();
            if (range == 0){
                Holder<Biome> blockBiome = level.getBiome(pos);
                if (!blockBiome.is(target)){
                    this.targetPos.set(this.getBlockPos());
                }
            } else {
                x: for (int x = -range; x <= range; x++) {
                    for (int y = -range; y <= range; y++) {
                        for (int z = -range; z <= range; z++) {
                            tempPos.set(pos).move(x, y, z);
                            if (!level.isLoaded(tempPos)) continue;
                            Holder<Biome> blockBiome = BiomeHelper.getBiomeAt(level, tempPos);
                            if (!blockBiome.is(target)){
                                this.targetPos.set(tempPos);
                                break x;
                            }
                        }
                    }
                }
            }
            if (this.targetPos.equals(BlockPos.ZERO)){
                findingCooldown = 20;
                return true;
            }
        }
        if (this.targetPos.equals(BlockPos.ZERO)) return true;
        if (!level.isLoaded(this.targetPos)) {
            this.targetPos.set(BlockPos.ZERO);
            this.transformTime = 0;
            return true;
        }
        Holder<Biome> current = BiomeHelper.getBiomeAt(level, targetPos);
        if (current.is(target)){
            this.targetPos.set(BlockPos.ZERO);
            this.transformTime = 0;
            return true;
        }
        Object2IntOpenHashMap<TerraformerType> requirements = getTransformationRequirements(current, target);
        if (transformTime > 0){
            List<BETerraformerExtension> extensions = getExtensions(level, pos, MAX_CONTAINER_RANGE);
            this.presentTypes.clear();
            for (BETerraformerExtension extension : extensions) {
                if (requirements.containsKey(extension.getTerraformerType())){
                    presentTypes.add(extension.getTerraformerType());
                    if (presentTypes.size() >= requirements.size()) break;
                }
            }
            if (this.presentTypes.size() < requirements.size()) return true;
            for (BETerraformerExtension extension : extensions) {
                if (requirements.containsKey(extension.getTerraformerType())){
                    if (extension.sprinkerActive == 0){
                        extension.setChanged();
                    }
                    extension.sprinkerActive = 200;
                }
            }
            if (energyStorage.extractEnergy(POWER_PER_TICK,true) < POWER_PER_TICK){
                return true;
            }
            energyStorage.extractEnergy(POWER_PER_TICK,false);
            transformTime--;
            if (transformTime == 0){
                BiomeHelper.setBiome(level, targetPos, target);
                this.targetPos.set(BlockPos.ZERO);
                transformTime = 0;
                this.energyPerType.clear();
                setChanged();
            }
            return true;
        }
        boolean needsEnergy = false;
        for (TerraformerType type : requirements.keySet()){
            if (this.energyPerType.getOrDefault(type,0) < requirements.getInt(type)){
                needsEnergy = true;
                break;
            }
        }

        if (!needsEnergy){
            transformTime = AUConfig.TERRAFORMER_TIME.get();
        } else {
            List<BETerraformerExtension> extensions = getExtensions(level,pos,MAX_CONTAINER_RANGE);
            presentTypes.clear();
            for (BETerraformerExtension extension : extensions){
                if (extension.hasAntenna != Boolean.TRUE || extension.tfEnergy <= 0)continue;
                int toAdd = requirements.getInt(extension.getTerraformerType()) - this.energyPerType.getOrDefault(extension.getTerraformerType(),0);
                if (toAdd > 0){
                    extension.tfEnergy -= toAdd;
                    this.energyPerType.addTo(extension.getTerraformerType(), toAdd);
                    extension.setChanged();
                    return true;
                }
            }
        }
        return oldReturn;
    }

    @SafeVarargs
    public static void alter(Object2IntOpenHashMap<TerraformerType> map, Set<TagKey<Biome>> addedTraits, Set<TagKey<Biome>> removedTraits, TerraformerType plusType, TerraformerType removeType, int addAmount, int removeAmount, TagKey<Biome>... biomeTypes) {
        for (TagKey<Biome> type : biomeTypes) {
            if (addedTraits.contains(type))
                map.addTo(plusType, addAmount);
            if (removedTraits.contains(type))
                map.addTo(removeType, removeAmount);
        }
    }

    public static Set<TagKey<Biome>> getTraitsDiff(Holder<Biome> a, Holder<Biome> b) {
        Set<TagKey<Biome>> diff = new HashSet<>(b.tags().toList());
        a.tags().toList().forEach(diff::remove);
        return diff;
    }

    private static List<BETerraformerExtension> getExtensions(Level level, BlockPos pos, int range) {
        ImmutableList.Builder<BETerraformerExtension> builder = new ImmutableList.Builder<>();
        BlockPos.betweenClosedStream(new AABB(pos).inflate(range)).forEach(blockPos->{
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof BETerraformerExtension extension){
                builder.add(extension);
            }
        });
        return builder.build();
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        tag.put("BiomeHandler", biomeHandler.serializeNBT(registries));
        tag.putInt("Range", range);
        tag.putInt("TransformTime", transformTime);
        TF_ENERGY_CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE),energyPerType).result().ifPresent(nbt->tag.put("LoadedTFEnergy", nbt));
        tag.put("Energy", energyStorage.serializeNBT(registries));
        if (forClient)
            tag.store("TargetPos", AUExtraCodecs.BLOCK_POS,registries.createSerializationContext(NbtOps.INSTANCE),targetPos);
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        this.biomeHandler.deserializeNBT(registries,tag.getCompoundOrEmpty("BiomeHandler"));
        this.range = tag.getIntOr("Range",0);
        this.transformTime = tag.getIntOr("TransformTime",0);
        if (tag.contains("LoadedTFEnergy")) {
            TF_ENERGY_CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("LoadedTFEnergy")).result().ifPresent(map -> this.energyPerType = map);
        }
        energyStorage.deserializeNBT(registries,tag.get("Energy"));
        if (tag.contains("TargetPos"))
            tag.read("TargetPos",AUExtraCodecs.BLOCK_POS,registries.createSerializationContext(NbtOps.INSTANCE)).ifPresent(targetPos::set);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MenuTerraformer(id, playerInv, this);
    }

    public class MenuTerraformer extends AUBEMenu {

        public MenuTerraformer(int id, Inventory playerInv, AUBlockEntity be) {
            super(AUMenus.TERRAFORMER_EXTENSION.get(), id, be);

            addTitle();
            addWidget(biomeHandler.getWidget(4,15));
            addWidget(new AUTextWidget(28,18,1) {
                @Override
                protected Component getMessage() {
                    Holder<Biome> biome = biomeHandler.getBiome();
                    if (targetPos.equals(BlockPos.ZERO) || biome == null || !biome.isBound() || level == null) {
                        return Component.empty();
                    }
                    Holder<Biome> currentBiome = level.getBiome(targetPos);
                    if (currentBiome == null || !currentBiome.isBound()) {
                        return Component.empty();
                    }

                    return getBiomeName(currentBiome).append(" -> ").append(getBiomeName(biome));
                }
            });
            addWidget(new AUBackgroundWidget(22,35,148,70, AUBEMenu.texBackgroundBorder));
            int border = 6;
            AUTextScrollWidget textScroll = new AUTextScrollWidget(22+border,35+border,148-border*2,70-border*2) {
                @Override
                protected List<Component> getMessages() {
                    Holder<Biome> targetBiome = biomeHandler.getBiome() != null && biomeHandler.getBiome().isBound() ? biomeHandler.getBiome() : null;
                    if (targetBiome == null){
                        return List.of(AULang.NO_BIOME_MARKER_GUI_MESSAGE.get());
                    }
                    if (targetPos.equals(BlockPos.ZERO)){
                        return List.of(AULang.SEARCHING_GUI_MESSAGE.get());
                    }
                    MutableComponent builder = Component.empty();
                    builder.append(AULang.PROCESS_POS_GUI_MESSAGE.get(targetPos.getX(),targetPos.getY(),targetPos.getZ()));
                    builder.append("\n");
                    builder.append(AULang.TRANSFORM_TIME_GUI_MESSAGE.get(transformTime));
                    builder.append("\n\n");
                    Holder<Biome> currentBiome = level.getBiome(targetPos);
                    if (currentBiome == null || !currentBiome.isBound()) {
                        return List.of(builder);
                    }
                    Object2IntOpenHashMap<TerraformerType> requirements = getTransformationRequirements(currentBiome, targetBiome);
                    for (TerraformerType type : requirements.keySet()){
                        int required = requirements.getInt(type);
                        int present = energyPerType.getOrDefault(type,0);
                        builder.append("\n").append(Component.translatable("block."+AuxiliaUtilities.MODID+"."+type.getSerializedName()).append("("+present+"/"+required+")").withStyle(present >= required ? ChatFormatting.GREEN : ChatFormatting.RED));
                    }
                    List<TerraformerType> missingTypes = requirements.keySet().stream().filter(type -> energyPerType.getOrDefault(type,0) < requirements.getInt(type)).toList();
                    if (missingTypes.isEmpty()) return List.of(builder);
                    builder.append("\n\n").append(AULang.MISSING_CLIMOGRAPHS_GUI_MESSAGE.get().withStyle(ChatFormatting.RED));
                    for (TerraformerType type :missingTypes){
                        builder.append("- ").append(Component.translatable("block."+AuxiliaUtilities.MODID+"."+type.getSerializedName()).withStyle(ChatFormatting.RED)).append("\n");
                    }
                    builder.append(AULang.PLACE_IN_RANGE_GUI_MESSAGE.get(MAX_CONTAINER_RANGE));
                    return List.of(builder);
                }
            };
            addWidget(textScroll);
            AUScrollbarNetworkWidget scrollbar = new AUScrollbarNetworkWidget(4,35,70,0,AUConfig.TERRAFORMER_RANGE.get()) {


                @Override
                public boolean allowScroll(double mouseX, double mouseY) {
                    return !textScroll.getScrollbar().allowScroll(mouseX,mouseY);
                }

                @Override
                public int getValueServer() {
                    return range;
                }

                @Override
                public void setValueServer(int value) {
                    if (range > value){
                        BlockPos substract = targetPos.subtract(getBlockPos());
                        if (Mth.abs(substract.getX()) > value || Mth.abs(substract.getY()) > value || Mth.abs(substract.getZ()) > value){
                            targetPos.set(BlockPos.ZERO);
                            transformTime = 0;
                        }
                    }
                    range = value;
                    setChanged();
                }

                @Override
                public @NotNull List<Component> getTooltips() {
                    return List.of(AULang.RANGE_TOOLTIP.get(scrollValue));
                }
            };
            scrollbar.setValueNoUpdate(range);
            addWidget(scrollbar);
            crop();
            addWidget(new AUEnergyWidget(this.width,(this.height -54) / 2,energyStorage));
            cropAndAddPlayerSlots(playerInv);
            validate();
        }

        private static MutableComponent getBiomeName(Holder<Biome> biome){
            if (biome == null || !biome.isBound() || biome.getKey() == null) return Component.empty();
            ResourceLocation loc = biome.getKey().location();
            String key = loc.toLanguageKey("biome");
            Component name;
            if (Language.getInstance().has(key)) {
                name = Component.translatable(key);
            } else {
                name = Component.literal(loc.toString());
            }
            return name.copy();
        }
    }
}

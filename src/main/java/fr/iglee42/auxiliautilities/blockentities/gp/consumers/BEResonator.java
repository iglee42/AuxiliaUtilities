package fr.iglee42.auxiliautilities.blockentities.gp.consumers;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blockentities.generators.BERainbowGenerator;
import fr.iglee42.auxiliautilities.blockentities.gp.AUGPConsumerBlockEntity;
import fr.iglee42.auxiliautilities.blockentities.items.SingleUpgradeStackHandler;
import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.gp.GPNetworkManager;
import fr.iglee42.auxiliautilities.menu.AUBEMenu;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.menu.widgets.AUTextWidget;
import fr.iglee42.auxiliautilities.menu.widgets.AUTimedProgressWidget;
import fr.iglee42.auxiliautilities.menu.widgets.slots.SlotItemHandlerWidget;
import fr.iglee42.auxiliautilities.recipes.ResonatorRecipe;
import fr.iglee42.auxiliautilities.utils.Upgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class BEResonator extends AUGPConsumerBlockEntity implements MenuProvider {

    private final ItemStackHandler inventory = new ItemStackHandler(2);
    private final SingleUpgradeStackHandler upgrades = new SingleUpgradeStackHandler(Upgrade.SPEED){
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (currentRecipe != null){
                updateNetwork();
            }
        }
    };

    private RecipeHolder<ResonatorRecipe> currentRecipe;
    private int progress;

    public BEResonator( BlockPos pos, BlockState state) {
        super(AUBlockEntityTypes.RESONATOR.get(), pos, state);
    }


    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                AUBlockEntityTypes.RESONATOR.get(),
                (be,ctx)->be.inventory
        );
    }
    @Override
    public int getGPConsumption() {
        return currentRecipe != null && progress > 0 ? (int) (currentRecipe.value().getRequiredGP() + (1 + Upgrade.SPEED.getPowerUse(upgrades.getLevel(Upgrade.SPEED)))) : 0;
    }

    public boolean canWork(){
        if (currentRecipe == null) return false;
        boolean work = GPNetworkManager.INSTANCE.hasEnoughPower(getNetworkId()) && inventory.insertItem(1,currentRecipe.value().assemble(new SingleRecipeInput(inventory.getStackInSlot(0)),level.registryAccess()),true).isEmpty();
        if (currentRecipe.value().doesRequiresRainbowGenerator()){
            MutableBoolean hasRainbow = new MutableBoolean(false);
            BlockPos.betweenClosedStream(new AABB(getBlockPos()).inflate(AUConfig.RAINBOW_RANGE.get())).forEach(
                    pos-> level.getBlockEntity(pos, AUBlockEntityTypes.RAINBOW_GENERATOR.get()).ifPresent(be -> {
                        if (be.isProviding()) hasRainbow.setTrue();
                    })
            );
            work = work && hasRainbow.getValue();
        }
        return work;
    }

    @Override
    protected boolean serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (inventory.getStackInSlot(0).isEmpty()){
            currentRecipe = null;
            progress = 0;
            updateNetwork();
            return super.serverTick(level, pos, state);
        }
        if (currentRecipe != null) {
            if (canWork()){
                if (progress >= currentRecipe.value().getRequiredGP() * 100){
                    if (inventory.insertItem(1,currentRecipe.value().assemble(new SingleRecipeInput(inventory.getStackInSlot(0)),level.registryAccess()),false).isEmpty()){
                        inventory.extractItem(0,1,false);
                        this.progress = 0;
                        this.currentRecipe = null;
                        updateNetwork();
                    }
                } else {
                    this.progress += 4 * (1 + upgrades.getLevel(Upgrade.SPEED));
                    this.progress = Math.clamp(progress,0,currentRecipe.value().getRequiredGP() * 100);
                }
            }
            return true;
        }
        Optional<RecipeHolder<ResonatorRecipe>> optional = level.recipeAccess().getRecipeFor(ResonatorRecipe.Type.INSTANCE, new SingleRecipeInput(inventory.getStackInSlot(0)), level);
        if (optional.isPresent()){
            currentRecipe = optional.get();
            progress = 0;
            updateNetwork();
            return true;
        }


        return super.serverTick(level, pos, state);
    }

    @Override
    protected void save(CompoundTag tag, HolderLookup.Provider registries, boolean forClient) {
        super.save(tag, registries, forClient);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.put("Upgrades", upgrades.serializeNBT(registries));
        if (forClient) tag.putBoolean("HasRecipe", currentRecipe != null);
    }

    @Override
    protected void load(CompoundTag tag, HolderLookup.Provider registries) {
        super.load(tag, registries);
        inventory.deserializeNBT(registries,tag.getCompoundOrEmpty("Inventory"));
        progress = tag.getIntOr("Progress",0);
        upgrades.deserializeNBT(registries,tag.getCompoundOrEmpty("Upgrades"));
        if (tag.contains("HasRecipe") && tag.getBooleanOr("HasRecipe",false)){
            /*currentRecipe = level.getRecipeManager().getAllRecipesFor(ResonatorRecipe.Type.INSTANCE).stream()
                    .filter(r -> r.value().matches(new SingleRecipeInput(inventory.getStackInSlot(0)), level))
                    .findFirst().orElse(null);*/
        } else {
            currentRecipe = null;
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new MenuResonator(id, playerInv,this);
    }

    public class MenuResonator extends AUBEMenu {

        public MenuResonator(int id, Inventory playerInv, AUBlockEntity be) {
            super(AUMenus.RESONATOR.get(), id, be);

            addTitle();
            AUTimedProgressWidget progressWidget = new AUTimedProgressWidget(74,42) {
                @Override
                protected float getTime() {
                    return (float) progress / (1 + upgrades.getLevel(Upgrade.SPEED));
                }

                @Override
                protected float getMaxTime() {
                    if (!GPNetworkManager.INSTANCE.hasEnoughPower(getNetworkId())) return -1;
                    if (currentRecipe == null) return 0;
                    return currentRecipe.value().getRequiredGP() * 100f / (1 + upgrades.getLevel(Upgrade.SPEED));
                }
            };
            progressWidget.jeiCategory(AuxiliaUtilities.id(ResonatorRecipe.Type.ID));
            addWidget(progressWidget);
            addWidget(new SlotItemHandlerWidget(inventory,0, 50, 42));
            addWidget(new SlotItemHandlerWidget(inventory,1, 102, 42){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
            addWidget(upgrades.getSpeedSlot(148,42));
            addWidget(new AUTextWidget(0,20,0) {
                @Override
                protected Component getMessage() {
                    if (currentRecipe == null) return Component.empty();
                    int level = upgrades.getLevel(Upgrade.SPEED);
                    return AULang.GP_TOOLTIP.get( NumberFormat.getInstance(Locale.UK).format(progress * 0.01 * (1+level)), NumberFormat.getInstance(Locale.UK).format((long) currentRecipe.value().getRequiredGP() * (1 + level)));
                }
            });
            cropAndAddPlayerSlots(playerInv);
            validate();
        }
    }
}

package fr.iglee42.auxiliautilities.menu;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntity;
import fr.iglee42.auxiliautilities.items.ItemFilterFluid;
import fr.iglee42.auxiliautilities.items.ItemFilterItem;
import fr.iglee42.auxiliautilities.items.ItemFlatTransferNode;
import fr.iglee42.auxiliautilities.items.ItemPowerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AUMenus {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, AuxiliaUtilities.MODID);

    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> RESONATOR = registerBEMenuType("resonator");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> ENCHANTER = registerBEMenuType("enchanter");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> FURNACE = registerBEMenuType("furnace");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> CRUSHER = registerBEMenuType("crusher");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> GENERATORS = registerBEMenuType("generators");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> ENDER_PORCUPINE = registerBEMenuType("ender_porcupine");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> TERRAFORMER_EXTENSION = registerBEMenuType("terraformer_extension");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> TERRAFORMER = registerBEMenuType("terraformer");

    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> FLUID_FILTER = registerMenuType(ItemFilterFluid.FilterConfigContainer::new, "fluid_filter");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> ITEM_FILTER = registerMenuType(ItemFilterItem.FilterConfigContainer::new, "item_filter");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> FLAT_TRANSFER_NODE = registerMenuType(ItemFlatTransferNode.FlatTransferNodeMenu::new, "flat_transfer_node");
    public static final DeferredHolder<MenuType<?>,MenuType<AUMenu>> POWER_REPORT = registerMenuType(ItemPowerManager.PowerReportMenu::new, "power_report");

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>,MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }

    private static DeferredHolder<MenuType<?>,MenuType<AUMenu>> registerBEMenuType(String name) {
        return MENU_TYPES.register(name, () -> IMenuTypeExtension.create(AUMenus::createMenu));
    }


    public static AUMenu createMenu(int id, Inventory inv, RegistryFriendlyByteBuf buffer){
        BlockPos pos = buffer.readBlockPos();
        if (inv.player.level().getBlockEntity(pos) instanceof AUBlockEntity be){
            return (AUMenu) be.createMenu(id,inv, inv.player);
        }
        return null;
    }

}

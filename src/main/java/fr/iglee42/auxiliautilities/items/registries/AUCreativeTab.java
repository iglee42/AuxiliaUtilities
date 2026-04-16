package fr.iglee42.auxiliautilities.items.registries;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.items.api.AUItemBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class AUCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,AuxiliaUtilities.MODID);

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> TAB = CREATIVE_TABS.register("tab",()->
            CreativeModeTab.builder()
                    .icon(()-> AUBlocks.ANGEL_BLOCK.toStack())
                    .title(AULang.TAB.get())
                    .build());

    @SubscribeEvent
    public static void registerItems(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey().equals(TAB.getKey())){
            AUItems.ITEMS.getEntries().stream().map(DeferredHolder::get).filter(AUItemBase.class::isInstance).map(AUItemBase.class::cast).forEach(b->b.addToTab(event::accept));
        }
    }
}


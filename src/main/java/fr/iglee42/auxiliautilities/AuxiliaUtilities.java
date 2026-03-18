package fr.iglee42.auxiliautilities;

import fr.iglee42.auxiliautilities.items.AUItems;
import fr.iglee42.auxiliautilities.potions.AUMobEffects;
import fr.iglee42.auxiliautilities.potions.AUPotions;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(AuxiliaUtilities.MODID)
public class AuxiliaUtilities {
    public static final String MODID = "au";

    public AuxiliaUtilities(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        AUItems.ITEMS.register(modEventBus);
        AUMobEffects.MOB_EFFECTS.register(modEventBus);
        AUPotions.POTIONS.register(modEventBus);

        //modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }

}

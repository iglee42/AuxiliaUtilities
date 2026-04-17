package fr.iglee42.auxiliautilities;

import com.mojang.logging.LogUtils;
import fr.iglee42.auxiliautilities.blockentities.AUBlockEntityTypes;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.items.registries.AUCreativeTab;
import fr.iglee42.auxiliautilities.items.registries.AUDataComponents;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.menu.AUMenus;
import fr.iglee42.auxiliautilities.potions.AUMobEffects;
import fr.iglee42.auxiliautilities.potions.AUPotions;
import fr.iglee42.auxiliautilities.recipes.AURecipes;
import fr.iglee42.auxiliautilities.recipes.ingredients.AUIngredients;
import fr.iglee42.auxiliautilities.utils.AUCuriosHelper;
import fr.iglee42.auxiliautilities.utils.AUSounds;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.LoadingModList;
import org.slf4j.Logger;


@Mod(AuxiliaUtilities.MODID)
public class AuxiliaUtilities {
    public static final String MODID = "au";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AuxiliaUtilities(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        AUBlocks.BLOCKS.register(modEventBus);
        AUItems.ITEMS.register(modEventBus);
        AUBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        AUDataComponents.DATA_COMPONENTS.register(modEventBus);
        AUMobEffects.MOB_EFFECTS.register(modEventBus);
        AUPotions.POTIONS.register(modEventBus);
        AUCreativeTab.CREATIVE_TABS.register(modEventBus);
        AURecipes.register(modEventBus);
        AUMenus.MENU_TYPES.register(modEventBus);
        AUSounds.SOUND_EVENTS.register(modEventBus);
        AUIngredients.INGREDIENT_TYPES.register(modEventBus);
        if (LoadingModList.get().getModFileById("curios") != null) new AUCuriosHelper(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, AUConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }

}

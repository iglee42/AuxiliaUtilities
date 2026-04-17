package fr.iglee42.auxiliautilities.client;

import com.mojang.blaze3d.platform.InputConstants;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;

import static fr.iglee42.auxiliautilities.utils.CommonKeysHandler.getKeyName;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID, value = Dist.CLIENT)
public class AUKeymappings {

    public static final String AUXILIA_CATEGORY = "key."+ AuxiliaUtilities.MODID+".category";

    public static final KeyMapping SHOW_DESCRIPTION = new KeyMapping(getKeyName("show_description"), KeyConflictContext.GUI, InputConstants.Type.KEYSYM, InputConstants.KEY_LSHIFT, AUXILIA_CATEGORY);
    public static final KeyMapping SHOW_DETAILS = new KeyMapping(getKeyName("show_details"),KeyConflictContext.GUI, InputConstants.Type.KEYSYM, InputConstants.KEY_LCONTROL, AUXILIA_CATEGORY);
    public static final KeyMapping INVERT_FLAT_TRANSFER_NODE = new KeyMapping(getKeyName("invert_flat_transfer_node"),KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_LCONTROL, AUXILIA_CATEGORY);
    public static final KeyMapping WAND_FIX_VERTICAL_AXIS = new KeyMapping(getKeyName("wand_fix_vertical_axis"),KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_LCONTROL, AUXILIA_CATEGORY);
    public static final KeyMapping WAND_FIX_HORIZONTAL_AXIS = new KeyMapping(getKeyName("wand_fix_horizontal_axis"),KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_LSHIFT, AUXILIA_CATEGORY);

    @SubscribeEvent
    public static void registerKeyBind(RegisterKeyMappingsEvent event){
        event.register(SHOW_DESCRIPTION);
        event.register(SHOW_DETAILS);
        event.register(INVERT_FLAT_TRANSFER_NODE);
        event.register(WAND_FIX_VERTICAL_AXIS);
        event.register(WAND_FIX_HORIZONTAL_AXIS);
    }


}

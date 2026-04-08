package fr.iglee42.auxiliautilities.blocks.cursedearth;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = AuxiliaUtilities.MODID)
public class CursedEarthEventHandler {

    @SubscribeEvent
    public static void entityJoin(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof Mob mob))
            return;

        CompoundTag tag = mob.getPersistentData();

        if (tag.contains("CursedEarth")) {

            int timer = tag.getInt("CursedEarth");

            if (timer <= 0) {
                mob.discard();
                event.setCanceled(true);
            } else {
                mob.goalSelector.addGoal(0, new CursedGoal(mob, timer));
            }
        }
    }
}
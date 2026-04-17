package fr.iglee42.auxiliautilities.items;

import fr.iglee42.auxiliautilities.AULang;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.config.AUConfig;
import fr.iglee42.auxiliautilities.items.api.AUFoilItem;
import fr.iglee42.auxiliautilities.items.registries.AUItems;
import fr.iglee42.auxiliautilities.utils.InventoryHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class ItemDivisionSigil extends AUFoilItem {

    public static final int[] ddx = new int[]{-1, 0, 1, 0};
    public static final int[] ddz = new int[]{0, -1, 0, 1};
    private static final UUID messageUUID = UUID.fromString("d1c9e8b0-5f1a-4c3b-9a1e-2f3b6c7d8e9f");

    public ItemDivisionSigil(Properties props) {
        super(props.durability(256));
    }

    public static boolean isBeacon(BlockPos pos, Level level) {
        return level.getBlockState(pos).is(Blocks.BEACON);
    }

    public static int[] getRitualStrength(Level level, BlockPos center) {
        List<BlockPos> redPos = new ArrayList<>();
        List<BlockPos> strPos = new ArrayList<>();
        redPos.add(center);
        strPos.add(center);
        int maxDist = 0;
        Block redstone = Blocks.REDSTONE_WIRE;
        Block string = Blocks.TRIPWIRE;
        for (int i = 0; i < redPos.size(); i++) {
            for (int dir = 0; dir < 4; dir++) {
                BlockPos pos = new BlockPos(redPos.get(i).getX() + ddx[dir], center.getY(), redPos.get(i).getZ() + ddz[dir]);
                int dist = mDist(pos.getX() - center.getX(), pos.getZ() - center.getZ());
                if (dist < 16 && level.getBlockState(pos).is(redstone) && !redPos.contains(pos)) {
                    redPos.add(pos);
                    if (dist > maxDist)
                        maxDist = dist;

                }
            }
        }
        redPos.remove(center);
        int sStrength = 0;
        for (int i = 0; i < strPos.size(); i++) {
            for (int dir = 0; dir < 4; dir++) {
                BlockPos pos = new BlockPos(strPos.get(i).getX() + ddx[dir], center.getY(), strPos.get(i).getZ() + ddz[dir]);
                int dist = mDist(pos.getX() - center.getX(), pos.getZ() - center.getZ());
                if (dist < 16) {
                    if (level.getBlockState(pos).is(string) && !strPos.contains(pos)) {
                        strPos.add(pos);
                        if (dist > maxDist)
                            maxDist = dist;
                    } else if (i != 0 && level.getBlockState(pos).is(redstone) && redPos.contains(pos)) {
                        sStrength++;
                    }
                }
            }
        }
        return new int[]{sStrength, maxDist * maxDist * 4};
    }

    public static int mDist(int x, int z) {
        if (x < 0)
            x *= -1;
        if (z < 0)
            z *= -1;
        return (x > z) ? x : z;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        stack.setDamageValue(stack.getDamageValue() + 1);
        if (stack.getDamageValue() == stack.getMaxDamage()) {
            return new ItemStack(AUItems.UNACTIVATED_DIVISION_SIGIL.get());
        }
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        if (player == null) return super.useOn(ctx);
        if (level.isClientSide) return super.useOn(ctx);
        if (!isBeacon(pos, level)) return super.useOn(ctx);
        MutableComponent message = Component.empty().append(AULang.STABILIZATION_RITUAL.get().withStyle(ChatFormatting.UNDERLINE)).append("\n");

        if (level.dimension().equals(Level.END)){
            if (!level.getBiome(pos).is(Biomes.THE_END))
                message.append("\n ! ").append(AULang.STABILIZATION_RITUAL_NOT_MAIN_END.get().withStyle(ChatFormatting.RED)).append("\n");
        } else {
            MutableComponent dimComponent = Component.literal("\n ! ").withStyle(ChatFormatting.RED);
            if (level.dimension().equals(Level.OVERWORLD)) {
                dimComponent.append(AULang.STABILIZATION_RITUAL_OVERWORLD.get());
            } else if (level.dimension().equals(Level.NETHER)) {
                dimComponent.append(AULang.STABILIZATION_RITUAL_NETHER.get());
            } else {
                dimComponent.append(AULang.STABILIZATION_RITUAL_OTHER.get());
            }
            dimComponent.append("\n");
            message.append(dimComponent);
        }

        IItemHandler northHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.north(5), Direction.SOUTH);
        IItemHandler southHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.south(5), Direction.NORTH);
        IItemHandler eastHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.east(5), Direction.WEST);
        IItemHandler westHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.west(5), Direction.EAST);

        int north = 0, south = 0, east = 0, west = 0;
        if (northHandler != null) {
            north = SiegeHandler.checkNorthChest(northHandler, false);
            message.append("\n - ").append(AULang.STABILIZATION_RITUAL_NORTH_CHEST.get(Math.min(north, AUConfig.END_SIEGE_ITEMS_CHESTS.get()), AUConfig.END_SIEGE_ITEMS_CHESTS.get()).withStyle(north >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() ? ChatFormatting.GREEN : ChatFormatting.RED));
        } else {
            message.append("\n ! ").append(AULang.STABILIZATION_RITUAL_MISSING_CHEST.get(AULang.NORTHERN.get()).withStyle(ChatFormatting.RED));
        }

        if (southHandler != null) {
            south = SiegeHandler.checkSouthChest(southHandler, false);
            message.append("\n - ").append(AULang.STABILIZATION_RITUAL_SOUTH_CHEST.get(Math.min(south, AUConfig.END_SIEGE_ITEMS_CHESTS.get()), AUConfig.END_SIEGE_ITEMS_CHESTS.get()).withStyle(south >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() ? ChatFormatting.GREEN : ChatFormatting.RED));
        } else {
            message.append("\n ! ").append(AULang.STABILIZATION_RITUAL_MISSING_CHEST.get(AULang.SOUTHERN.get()).withStyle(ChatFormatting.RED));
        }

        if (eastHandler != null) {
            east = SiegeHandler.checkEastChest(eastHandler, false);
            message.append("\n - ").append(AULang.STABILIZATION_RITUAL_EAST_CHEST.get(Math.min(east, AUConfig.END_SIEGE_ITEMS_CHESTS.get()), AUConfig.END_SIEGE_ITEMS_CHESTS.get()).withStyle(east >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() ? ChatFormatting.GREEN : ChatFormatting.RED));
        } else {
            message.append("\n ! ").append(AULang.STABILIZATION_RITUAL_MISSING_CHEST.get(AULang.EASTERN.get()).withStyle(ChatFormatting.RED));
        }

        if (westHandler != null) {
            west = SiegeHandler.checkWestChest(westHandler, false);
            message.append("\n - ").append(AULang.STABILIZATION_RITUAL_WEST_CHEST.get(Math.min(west, AUConfig.END_SIEGE_ITEMS_CHESTS.get()), AUConfig.END_SIEGE_ITEMS_CHESTS.get()).withStyle(west >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() ? ChatFormatting.GREEN : ChatFormatting.RED));
        } else {
            message.append("\n ! ").append(AULang.STABILIZATION_RITUAL_MISSING_CHEST.get(AULang.WESTERN.get()).withStyle(ChatFormatting.RED));
        }

        // Check Markings
        int[] strength = getRitualStrength(level, pos);
        if (strength[1] == 0) {
            message.append("\n\n").append(AULang.STABILIZATION_RITUAL_NO_MARKINGS.get().withStyle(ChatFormatting.RED));
        } else if (strength[0] == 0) {
            message.append("\n\n").append(AULang.STABILIZATION_RITUAL_ONE_MARKING.get().withStyle(ChatFormatting.RED));
        } else {
            StringBuilder strString = new StringBuilder(strength[0] + "");
            for (int i = 1; i < strength.length; i++) {
                strString.append(" / ").append(strength[i]).append(" / 64");
            }
            message.append("\n\n").append(AULang.STABILIZATION_RITUAL_STRENGTH.get(strString.toString()).withStyle(strength[0] >= 64 ? ChatFormatting.GREEN : ChatFormatting.RED));
        }

        if (north >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() && south >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() && east >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() && west >= AUConfig.END_SIEGE_ITEMS_CHESTS.get() && strength[0] >= 64 && level.getBiome(pos).is(Biomes.THE_END)) {
            message.append("\n\n").append(AULang.STABILIZATION_RITUAL_READY.get().withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
            message.append("\n\n").append(AULang.STABILIZATION_RITUAL_SACRIFICE.get().withStyle(ChatFormatting.GREEN, ChatFormatting.UNDERLINE));
        }

        AULang.sendMessageToPlayer(player, message, messageUUID);
        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @EventBusSubscriber(modid = AuxiliaUtilities.MODID)
    private static class SiegeHandler {
        
        private static final UUID messageSignature = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6");
        private static final List<UUID> siegeParticipants = new ArrayList<>();
        private static final List<MobSpawnSettings.SpawnerData> mobSpawns = List.of(
                new MobSpawnSettings.SpawnerData(EntityType.SPIDER,200,3,3),
                new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER,40,4,4),
                new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE,200,4,4),
                new MobSpawnSettings.SpawnerData(EntityType.SKELETON,200,4,4),
                new MobSpawnSettings.SpawnerData(EntityType.CREEPER,120,4,4),
                new MobSpawnSettings.SpawnerData(EntityType.BLAZE,80,2,4),
                new MobSpawnSettings.SpawnerData(EntityType.BREEZE,60,2,4),
                new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN,40,4,4),
                new MobSpawnSettings.SpawnerData(EntityType.WITCH,40,1,3),
                new MobSpawnSettings.SpawnerData(EntityType.SILVERFISH,40,3,3),
                new MobSpawnSettings.SpawnerData(EntityType.GIANT,15,1,1),
                new MobSpawnSettings.SpawnerData(EntityType.RAVAGER,20,1,1),
                new MobSpawnSettings.SpawnerData(EntityType.PILLAGER,40,1,3),
                new MobSpawnSettings.SpawnerData(EntityType.EVOKER,40,1,2),
                new MobSpawnSettings.SpawnerData(EntityType.VINDICATOR,40,1,2)
        );

        private static final AABB endZone = new AABB(BlockPos.ZERO).inflate(1024);


        static void endSiege(Level level,boolean announce){
            if (level.isClientSide) return;
            if (!level.dimension().equals(Level.END)) return;
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, endZone)){
                if (entity instanceof Mob && entity.getPersistentData().contains("Siege")) entity.discard();
            }
            for (UUID uuid : siegeParticipants){
                removePlayerFromSiege(level,uuid);
            }
            if (announce) {
                level.getServer().getPlayerList().broadcastSystemMessage(AULang.END_SIEGE_END.get(), false);
            }
        }

        static void removePlayerFromSiege(Level level,UUID playerId){
            if (level.isClientSide) return;
            Player player = level.getPlayerByUUID(playerId);
            if (player != null) {
                if (player.getPersistentData().contains("Siege")) {
                    player.getPersistentData().remove("Siege");
                }
            }
            siegeParticipants.remove(playerId);
            CustomBossEvents events = level.getServer().getCustomBossEvents();
            ResourceLocation barId = AuxiliaUtilities.id("siege_" + playerId);
            CustomBossEvent bar = events.get(barId);
            if (bar != null) {
                bar.removeAllPlayers();
                events.remove(bar);
            }
        }

        static void addPlayerToSiege(ServerLevel level,Player player){
            if (level.isClientSide) return;
            if (!level.getBiome(player.blockPosition()).is(Biomes.THE_END)) return;
            if (!siegeParticipants.contains(player.getGameProfile().getId())){
                siegeParticipants.add(player.getGameProfile().getId());
                player.getPersistentData().putInt("SiegeKills",0);
                CustomBossEvents events = level.getServer().getCustomBossEvents();
                ResourceLocation barId = AuxiliaUtilities.id("siege_"+player.getGameProfile().getId());
                if (events.get(barId) == null){
                    CustomBossEvent bar = events.create(barId,getKillMessage(0));
                    bar.setColor(CustomBossEvent.BossBarColor.GREEN);
                    bar.setOverlay(CustomBossEvent.BossBarOverlay.PROGRESS);
                    bar.addPlayer((ServerPlayer) player);
                    bar.setVisible(true);
                    bar.setMax(AUConfig.END_SIEGE_KILLS.get());
                    bar.setProgress(0);
                }

            }
        }

        static MutableComponent getKillMessage(int kills){
            return AULang.END_SIEGE.get().append(" : ").append(AULang.KILLS.get(kills)).append(" / "+AUConfig.END_SIEGE_KILLS.get());
        }

        static void startSiege(Level level){
            if (level.isClientSide) return;
            if (!level.dimension().equals(Level.END)) return;
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, endZone)){
                if (entity instanceof Mob) entity.discard();
                else if (entity instanceof Player player){
                    addPlayerToSiege((ServerLevel) level,player);
                }
            }
            if (!siegeParticipants.isEmpty()){
                level.getServer().getPlayerList().broadcastSystemMessage(AULang.END_SIEGE_BEGIN.get(),false);
            } else {
                endSiege(level,false);
            }
        }
        static boolean hasSigil(Player player){
            return player.getInventory().contains(new ItemStack(AUItems.DIVISION_SIGIL.get()));
        }

        static void checkPlayer(MinecraftServer server){
            ServerLevel level = server.getLevel(Level.END);
            if (level == null || level.isClientSide){
                siegeParticipants.clear();
                return;
            }

            if (!siegeParticipants.isEmpty()){
                for (int i = 0; i < siegeParticipants.size(); i++) {
                    if (level.getPlayerByUUID(siegeParticipants.get(i)) == null){
                        UUID id = siegeParticipants.get(i);
                        removePlayerFromSiege(level, id);
                        i--;
                    }
                }
                if (siegeParticipants.isEmpty()){
                    endSiege(level,true);
                }
            }
        }

        @SubscribeEvent
        static void playerJoin(EntityJoinLevelEvent event){
            if (event.getLevel().isClientSide) return;
            ServerLevel level = (ServerLevel) event.getLevel();
            checkPlayer(level.getServer());
            if (event.getEntity() instanceof Player player){
                if (!level.getBiome(player.blockPosition()).is(Biomes.THE_END)){
                    if (event.getEntity().getPersistentData().contains("Siege")){
                        event.getEntity().getPersistentData().remove("Siege");
                        removePlayerFromSiege(level,player.getGameProfile().getId());
                    }
                } else if (event.getEntity().getPersistentData().contains("Siege") && !siegeParticipants.contains(player.getGameProfile().getId())){
                    addPlayerToSiege(level,player);
                }

            }
        }
        static double sq(double x, double y, double z) {
            return x * x + z * z + y * y;
        }

        @SubscribeEvent
        static void golemDeath(LivingDeathEvent event){
            if (event.getEntity().level().isClientSide) return;
            if (!event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.THE_END)) return;
            if (!(event.getEntity() instanceof IronGolem)) return;
            if (!(event.getSource().getEntity() instanceof Player player)) return;
            if (!hasSigil(player)) return;
            ServerLevel level = (ServerLevel) event.getEntity().level();
            Collection<BlockEntity> bes = level.getChunkAt(event.getEntity().blockPosition()).getBlockEntities().values();
            for (BeaconBlockEntity beacon : bes.stream().filter(BeaconBlockEntity.class::isInstance).map(BeaconBlockEntity.class::cast).toList()){
                BlockPos pos = beacon.getBlockPos();
                if (sq( event.getEntity().getX()-pos.getX() -0.5D, event.getEntity().getY()-pos.getY(),event.getEntity().getZ()- pos.getZ() - 0.5D) < 300){
                    int[] strength = getRitualStrength(event.getEntity().level(), pos);
                    if (strength[0] >= 64){
                        boolean valid = true;
                        if (checkNorthChest(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.north(5), Direction.SOUTH), true) < AUConfig.END_SIEGE_ITEMS_CHESTS.get() )
                            valid = false;
                        if (valid && checkSouthChest(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.south(5), Direction.NORTH), true) < AUConfig.END_SIEGE_ITEMS_CHESTS.get() )
                            valid = false;
                        if (valid && checkEastChest(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.east(5), Direction.WEST), true) < AUConfig.END_SIEGE_ITEMS_CHESTS.get() )
                            valid = false;
                        if (valid && checkWestChest(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.west(5), Direction.EAST), true) < AUConfig.END_SIEGE_ITEMS_CHESTS.get() )
                            valid = false;
                        if (valid){
                            /*level.destroyBlock(pos, false);
                            for (int j = 0; j < 4; j++) {
                                level.destroyBlock(pos.offset(ddx[j] * 5, 0, ddz[j] * 5), false);
                            }
                            level.explode(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 6, Level.ExplosionInteraction.BLOCK);
                            for (int j = 0; j < 4; j++) {
                                BlockPos exPos =pos.offset(ddx[j] * 5, 0, ddz[j] * 5);
                                level.explode(null,exPos.getX() + 0.5D,exPos.getY() + 0.5D,exPos.getZ()+0.5D,6, Level.ExplosionInteraction.BLOCK);
                            }*/
                            startSiege(level);
                            return;
                        }
                    }
                }
            }
        }

        static void upgradeSigil(Player player){
            for (int i = 0; i < player.getInventory().getContainerSize(); i++){
                if (ItemStack.isSameItem(player.getInventory().getItem(i),AUItems.DIVISION_SIGIL.toStack())){
                    player.getInventory().setItem(i, new ItemStack(AUItems.PSEUDO_INVERSION_SIGIL.get()));
                    return;
                }
            }
        }

        @SubscribeEvent
        static void siegeMobDeath(LivingDeathEvent event){
            if (siegeParticipants.isEmpty()) return;
            if (event.getEntity().level().isClientSide) return;
            if (!event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.THE_END)) return;
            ServerLevel level = (ServerLevel) event.getEntity().level();
            if (event.getEntity() instanceof Player) checkPlayer(level.getServer());
            if (event.getEntity() instanceof LivingEntity && event.getSource().getEntity() instanceof Player player && event.getEntity().getPersistentData().contains("Siege")){
                if (siegeParticipants.contains(player.getGameProfile().getId())){
                    if (player.getPersistentData().contains("SiegeKills")){
                        player.getPersistentData().putInt("SiegeKills", player.getPersistentData().getInt("SiegeKills") + 1);
                    } else {
                        player.getPersistentData().putInt("SiegeKills", 1);
                    }
                    int kills = player.getPersistentData().getInt("SiegeKills");

                    CustomBossEvents events = level.getServer().getCustomBossEvents();
                    ResourceLocation barId = AuxiliaUtilities.id("siege_" + player.getGameProfile().getId());
                    CustomBossEvent bar = events.get(barId);
                    if (bar != null){
                        bar.setName(getKillMessage(kills));
                        bar.setProgress((float) kills / AUConfig.END_SIEGE_KILLS.get());
                    }
                    if (kills >= AUConfig.END_SIEGE_KILLS.get()){
                        upgradeSigil(player);
                        removePlayerFromSiege(level,player.getGameProfile().getId());
                        AULang.SIGIL_UPGRADED.sendToPlayer(player,messageSignature);
                    } else {
                        AULang.KILLS.sendToPlayer(player,messageSignature,kills);
                    }
                }

            }
        }

        @SubscribeEvent
        static void siegePotentialSpawns(LevelEvent.PotentialSpawns event){
            if (event.getLevel().isClientSide()) return;
            if (!event.getLevel().getBiome(event.getPos()).is(Biomes.THE_END)) return;
            if (event.getMobCategory() != MobCategory.MONSTER) return;
            checkPlayer(event.getLevel().getServer());
            if (siegeParticipants.isEmpty()) {
                mobSpawns.forEach(event::removeSpawnerData);
            } else if (event.getSpawnerDataList().size() < mobSpawns.size()){
                mobSpawns.forEach(event::addSpawnerData);
            }
        }

        @SubscribeEvent
        static void siegeLiving(EntityTickEvent.Post event){
            if (event.getEntity().level().isClientSide) return;
            ServerLevel level = (ServerLevel) event.getEntity().level();
            if (siegeParticipants.isEmpty()) {
                if (event.getEntity().getPersistentData().contains("Siege")){
                    event.getEntity().discard();
                    endSiege(level,true);
                }
                return;
            }
            if (level.random.nextInt(1000) == 0)checkPlayer(level.getServer());
            if (!event.getEntity().level().getBiome(event.getEntity().blockPosition()).is(Biomes.THE_END)) return;
            if (event.getEntity() instanceof Mob mob && mob.getTarget() == null && mob.getPersistentData().contains("Siege")){
                int playerIndex = level.random.nextInt(siegeParticipants.size());
                Player player = level.getPlayerByUUID(siegeParticipants.get(playerIndex));
                if (player != null){
                    mob.setTarget(player);
                } else {
                    siegeParticipants.remove(playerIndex);
                }
            }
            if (event.getEntity() instanceof Player player){
                if (Math.abs(player.getDeltaMovement().y) < 1e-5
                        && player.fallDistance == 0.0F
                        && !player.onGround()
                        && !player.onClimbable()
                        && !player.isInWater()
                        && player.getVehicle() == null) {

                    player.hurt(player.level().damageSources().fall(), 0.5F);
                }
            }
        }

        @SubscribeEvent
        static void siegeSpawn(EntityJoinLevelEvent event){
            if (event.getLevel().isClientSide) return;
            if (!event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.THE_END)) return;
            if (siegeParticipants.isEmpty()) return;
            if (event.getEntity() instanceof Mob mob && mob.level().isEmptyBlock(mob.blockPosition())){
                mob.getPersistentData().putBoolean("Siege", true);
                Holder<MobEffect>[] possibleEffects = new Holder[]{
                        MobEffects.MOVEMENT_SPEED,
                        MobEffects.ABSORPTION,
                        MobEffects.DAMAGE_BOOST,
                        MobEffects.FIRE_RESISTANCE,
                        MobEffects.REGENERATION,
                        MobEffects.DAMAGE_RESISTANCE
                };
                mob.addEffect(new MobEffectInstance(possibleEffects[event.getLevel().random.nextInt(possibleEffects.length)],7200,1));
            }
        }

        static int checkNorthChest(IItemHandler handler, boolean destroy){
            ItemStack[] stacks = AUConfig.NORTH_CHEST_ITEMS.get().stream().map(String.class::cast)
                    .map(ResourceLocation::tryParse)
                    .filter(Objects::nonNull)
                    .map(BuiltInRegistries.ITEM::get)
                    .map(ItemStack::new)
                    .toArray(ItemStack[]::new);
            return checkItemHandler(handler, stacks, destroy);
        }
        static int checkSouthChest(IItemHandler handler, boolean destroy){
            ItemStack[] stacks = AUConfig.SOUTH_CHEST_ITEMS.get().stream().map(String.class::cast)
                    .map(ResourceLocation::tryParse)
                    .filter(Objects::nonNull)
                    .map(BuiltInRegistries.ITEM::get)
                    .map(ItemStack::new)
                    .toArray(ItemStack[]::new);
            return checkItemHandler(handler, stacks, destroy);
        }
        static int checkEastChest(IItemHandler handler, boolean destroy){
            ItemStack[] stacks;
            if (AUConfig.DEFAULT_EAST_CHEST.get()){
                stacks = BuiltInRegistries.POTION.holders()
                        .filter(h -> h.getKey() != null && h.getKey().location().getNamespace().equals("minecraft"))
                        .map(potionHolder -> {
                                    ItemStack potionStack = new ItemStack(Items.POTION);
                                    potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionHolder));
                                    return potionStack;
                                }
                        ).toArray(ItemStack[]::new);;
            } else {
                stacks = AUConfig.EAST_CHEST_ITEMS.get().stream().map(String.class::cast)
                        .map(ResourceLocation::tryParse)
                        .filter(Objects::nonNull)
                        .map(BuiltInRegistries.ITEM::get)
                        .map(ItemStack::new)
                        .toArray(ItemStack[]::new);
            }
            return checkItemHandler(handler, stacks, destroy);
        }

        static int checkWestChest(IItemHandler handler, boolean destroy){
            ItemStack[] stacks;
            if (AUConfig.DEFAULT_WEST_CHEST.get()){
                stacks = BuiltInRegistries.ITEM.holders()
                        .filter(h -> h.getKey() != null && h.getKey().location().getPath().startsWith("music_disc_"))
                        .map(ItemStack::new)
                        .toArray(ItemStack[]::new);
            } else {
                stacks = AUConfig.WEST_CHEST_ITEMS.get().stream().map(String.class::cast)
                        .map(ResourceLocation::tryParse)
                        .filter(Objects::nonNull)
                        .map(BuiltInRegistries.ITEM::get)
                        .map(ItemStack::new)
                        .toArray(ItemStack[]::new);
            }
            return checkItemHandler(handler, stacks, destroy);
        }


        static int checkItemHandler(IItemHandler handler, ItemStack[] stacks, boolean destroy) {
            if (handler == null) return 0;
            boolean[] checked = new boolean[stacks.length];
            int found = 0;
            for (int i = 0; i < handler.getSlots(); i++) {
                if (InventoryHelper.isStackNotEmpty(handler.getStackInSlot(i))) {
                    for (int j = 0; j < stacks.length && (!destroy || InventoryHelper.isStackNotEmpty(handler.getStackInSlot(i))); j++) {
                        if (!checked[j] && ItemStack.isSameItemSameComponents(handler.getStackInSlot(i), stacks[j])) {
                            if (destroy)
                                handler.extractItem(i, stacks[j].getCount(), false);
                            checked[j] = true;
                            found++;
                            break;
                        }
                    }
                }
            }
            return found;
        }
    }

}

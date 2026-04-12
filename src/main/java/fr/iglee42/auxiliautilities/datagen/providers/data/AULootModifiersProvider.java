package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AULootModifiersProvider extends GlobalLootModifierProvider {
    public AULootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AuxiliaUtilities.MODID);
    }

    @Override
    protected void start() {
        add(
                "add_ender_lilly",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).or(LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("chests/stronghold_corridor"))).build()
                },ResourceKey.create(Registries.LOOT_TABLE,AuxiliaUtilities.id("chests/plants")))
        );

        add(
                "add_drop_of_evil",
                new AddTableLootModifier(new LootItemCondition[]{
                        LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/wither_skeleton")).build()
                },ResourceKey.create(Registries.LOOT_TABLE,AuxiliaUtilities.id("entities/wither_skeleton")))
        );
    }
}

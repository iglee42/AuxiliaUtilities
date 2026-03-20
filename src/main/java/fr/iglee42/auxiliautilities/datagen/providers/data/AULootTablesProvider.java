package fr.iglee42.auxiliautilities.datagen.providers.data;

import com.google.common.collect.ImmutableMap;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AULootTablesProvider extends LootTableProvider {
    public AULootTablesProvider(PackOutput p_254123_, CompletableFuture<HolderLookup.Provider> p_323798_) {
        super(p_254123_, Set.of(), List.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)), p_323798_);
    }

    public static class Blocks extends BlockLootSubProvider {
        private final Map<Block, Function<Block, LootTable.Builder>> overrides = createOverrides();

        @NotNull
        private ImmutableMap<Block, Function<Block, LootTable.Builder>> createOverrides() {
            ImmutableMap.Builder<Block, Function<Block, LootTable.Builder>> builder = ImmutableMap.builder();
            builder.put(AUBlocks.ANGEL_BLOCK.get(), block->noDrop());
            return builder.build();
        }

        public Blocks(HolderLookup.Provider providers) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), providers);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BuiltInRegistries.BLOCK
                    .stream()
                    .filter(entry -> entry.getLootTable().location().getNamespace().equals(AuxiliaUtilities.MODID))
                    .toList();
        }

        @Override
        public void generate() {
            for (var block : getKnownBlocks()) {
                add(block, overrides.getOrDefault(block, this::defaultBuilder).apply(block));
            }
        }

        private LootTable.Builder defaultBuilder(Block block) {
            LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(block);
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
                    .when(ExplosionCondition.survivesExplosion());

            return LootTable.lootTable().withPool(pool);
        }

        protected final Holder<Enchantment> getEnchantment(ResourceKey<Enchantment> key) {
            return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
        }
    }
}
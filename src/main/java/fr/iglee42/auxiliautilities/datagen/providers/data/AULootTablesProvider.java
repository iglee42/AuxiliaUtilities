package fr.iglee42.auxiliautilities.datagen.providers.data;

import com.google.common.collect.ImmutableMap;
import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockEnderLilly;
import fr.iglee42.auxiliautilities.blocks.BlockRedOrchid;
import fr.iglee42.auxiliautilities.blocks.DecorativeBlockSet;
import fr.iglee42.auxiliautilities.items.AUDataComponents;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PotatoBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
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
            for (DecorativeBlockSet set : DecorativeBlockSet.ALL_SETS) {
                builder.put(set.getSlab().get(), this::createSlabItemTable);
            }
            builder.put(AUBlocks.ENDER_LILLY.get(), this::enderLilly);
            builder.put(AUBlocks.RED_ORCHID.get(), this::redOrchid);
            builder.put(AUBlocks.STONE_DRUM.get(), this::drum);
            builder.put(AUBlocks.IRON_DRUM.get(), this::drum);
            builder.put(AUBlocks.REINFORCED_LARGE_DRUM.get(), this::drum);
            builder.put(AUBlocks.DEMONICALLY_GARGANTUAN_DRUM.get(), this::drum);
            builder.put(AUBlocks.CREATIVE_DRUM.get(), this::drum);

            builder.put(AUBlocks.CURSED_EARTH.get(),block->createSingleItemTableWithSilkTouch(block,Items.DIRT));
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

        private LootTable.Builder redOrchid(Block block) {
            LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AUBlocks.RED_ORCHID.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockRedOrchid.AGE, 6));
            LootPoolEntryContainer.Builder<?> redEntry = LootItem.lootTableItem(Items.REDSTONE)
                    .apply(ApplyBonusCount.addBonusBinomialDistributionCount(getEnchantment(Enchantments.FORTUNE), 0.5714286F, 1)).when(condition);
            LootPoolEntryContainer.Builder<?> seedsEntry = LootItem.lootTableItem(AUBlocks.RED_ORCHID.asItem());
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(AlternativesEntry.alternatives(redEntry,seedsEntry));

            LootPoolEntryContainer.Builder<?> bonusSeedsEntry = LootItem.lootTableItem(AUBlocks.RED_ORCHID.asItem())
                    .apply(ApplyBonusCount.addBonusBinomialDistributionCount(getEnchantment(Enchantments.FORTUNE), 0.5714286F, 1));
            LootPool.Builder bonusPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(bonusSeedsEntry).when(condition);

            return LootTable.lootTable().apply(ApplyExplosionDecay.explosionDecay()).withPool(pool).withPool(bonusPool);
        }

        private LootTable.Builder enderLilly(Block block) {
            LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(AUBlocks.ENDER_LILLY.get())
                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockEnderLilly.AGE, 7));
            LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(Items.ENDER_PEARL)
                    .apply(ApplyBonusCount.addBonusBinomialDistributionCount(getEnchantment(Enchantments.FORTUNE), 0.5714286F, 1));
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
                    .when(condition);

            return defaultBuilder(block).withPool(pool);
        }
        private LootTable.Builder drum(Block block) {
            LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(block).apply(CopyComponentsFunction.copyComponents(
                    CopyComponentsFunction.Source.BLOCK_ENTITY
            ).include(AUDataComponents.STORED_FLUID.get()));
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
                    .when(ExplosionCondition.survivesExplosion());
            return LootTable.lootTable().withPool(pool);
        }


        protected final Holder<Enchantment> getEnchantment(ResourceKey<Enchantment> key) {
            return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
        }
    }
}
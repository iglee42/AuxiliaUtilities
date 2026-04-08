package fr.iglee42.auxiliautilities.datagen.providers.data;

import fr.iglee42.auxiliautilities.AuxiliaUtilities;
import fr.iglee42.auxiliautilities.blocks.AUBlocks;
import fr.iglee42.auxiliautilities.blocks.BlockEnderLilly;
import fr.iglee42.auxiliautilities.blocks.BlockSpike;
import fr.iglee42.auxiliautilities.items.ItemUnstableIngot;
import fr.iglee42.auxiliautilities.potions.effects.DoomEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AUDataRegistriesProvider {

    public static final ResourceKey<BiomeModifier> ENDER_LILLY_MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, AuxiliaUtilities.id("ender_lilly"));
    public static final ResourceKey<ConfiguredFeature<?,?>> ENDER_LILLY_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, AuxiliaUtilities.id("ender_lilly"));
    public static final ResourceKey<PlacedFeature> ENDER_LILLY_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, AuxiliaUtilities.id("ender_lilly"));

    public static RegistrySetBuilder build(){

        RegistrySetBuilder builder = new RegistrySetBuilder();


        builder.add(Registries.DAMAGE_TYPE,bootstrap-> {
            bootstrap.register(DoomEffect.DOOM_DAMAGE,
                    new DamageType(DoomEffect.DOOM_DAMAGE.location().getPath(),0.0f));
            bootstrap.register(ItemUnstableIngot.UNSTABLE_DAMAGE,
                    new DamageType(ItemUnstableIngot.UNSTABLE_DAMAGE.location().getPath(),0.0f));
            bootstrap.register(BlockSpike.SPIKE_DAMAGE,
                    new DamageType("spike",0.0f));
            bootstrap.register(BlockSpike.CREATIVE_SPIKE_DAMAGE,
                    new DamageType("creative_spike",0.0f));
        });

        AtomicReference<Holder<ConfiguredFeature<?, ?>>> enderLilly = new AtomicReference<>();
        builder.add(Registries.CONFIGURED_FEATURE,bootstrap->{
            enderLilly.set(bootstrap.register(ENDER_LILLY_CONFIGURED,
                    new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(SimpleStateProvider.simple(
                            AUBlocks.ENDER_LILLY.get().defaultBlockState().setValue(BlockEnderLilly.AGE,BlockEnderLilly.MAX_AGE)
                    )))));
        });

        AtomicReference<Holder<PlacedFeature>> enderLillyPlaced = new AtomicReference<>();

        builder.add(Registries.PLACED_FEATURE,bootstrap->{
            enderLillyPlaced.set(bootstrap.register(ENDER_LILLY_PLACED,
                    new PlacedFeature(
                            enderLilly.get(),
                            List.of(
                                    BiomeFilter.biome(),
                                    InSquarePlacement.spread(),
                                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG)
                                    //CountPlacement.of(UniformInt.of(10,48))
                            )
                    )
            ));
        });

        builder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS,bootstrap->{
            HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);
            bootstrap.register(ENDER_LILLY_MODIFIER,
                    new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(Tags.Biomes.IS_END), HolderSet.direct(enderLillyPlaced.get()), GenerationStep.Decoration.VEGETAL_DECORATION));
        });
        return builder;
    }

}

package fr.iglee42.auxiliautilities.utils;

import fr.iglee42.auxiliautilities.mixins.LevelChunkSectionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;

import java.util.List;

public class BiomeHelper {


    private static int quantize(int p_261998_) {
        return QuartPos.toBlock(QuartPos.fromBlock(p_261998_));
    }

    private static BlockPos quantize(BlockPos p_262148_) {
        return new BlockPos(quantize(p_262148_.getX()), quantize(p_262148_.getY()), quantize(p_262148_.getZ()));
    }

    public static void setBiome(ServerLevel level, BlockPos targetPos, Holder<Biome> biomeHolder) {

        ChunkAccess chunk = level.getChunk(targetPos);
        int quartX = QuartPos.fromBlock(targetPos.getX());
        int quartY = QuartPos.fromBlock(targetPos.getY());
        int quartZ = QuartPos.fromBlock(targetPos.getZ());

        int localX = quartX & 3;
        int localZ = quartZ & 3;
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndexFromSectionY(SectionPos.of(targetPos).y()));

        PalettedContainerRO<Holder<Biome>> biomes = section.getBiomes();
        if (biomes instanceof PalettedContainer<Holder<Biome>> biomesModifiable) {
            biomesModifiable.set(localX, quartY & 3, localZ, biomeHolder);
        }
        ((LevelChunkSectionAccessor) section).setBiomes(biomes);

        chunk.setUnsaved(true);
        level.getChunkSource().chunkMap.resendBiomesForChunks(List.of(chunk));

    }

    public static Holder<Biome> getBiomeAt(ServerLevel level, BlockPos targetPos) {

        ChunkAccess chunk = level.getChunk(targetPos);
        int quartX = QuartPos.fromBlock(targetPos.getX());
        int quartY = QuartPos.fromBlock(targetPos.getY());
        int quartZ = QuartPos.fromBlock(targetPos.getZ());

        int localX = quartX & 3;
        int localY = quartY & 3;
        int localZ = quartZ & 3;
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndexFromSectionY(SectionPos.of(targetPos).y()));
        PalettedContainerRO<Holder<Biome>> biomes = section.getBiomes();
        return biomes.get(localX, localY, localZ);
    }

}

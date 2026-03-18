package fr.iglee42.auxiliautilities.utils;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.HashMap;
import java.util.Map;

public class PositionPool {
  public static final BlockPos MID_HEIGHT = new BlockPos(0, 128, 0);
  
  private final Map<Long, BlockPos> pool = new HashMap<>();
  
  private int numDuplicateLookups;
  
  public BlockPos getPos(int x, int y, int z) {
    long key = BlockPos.asLong(x, y, z);
    BlockPos blockPos = this.pool.get(key);
    if (blockPos != null) {
      this.numDuplicateLookups++;
      return blockPos;
    } 
    blockPos = new BlockPos(x, y, z);
    this.pool.put(key, blockPos);
    return blockPos;
  }
  
  public BlockPos add(BlockPos input, int x, int y, int z) {
    return (x == 0 && y == 0 && z == 0) ? input : getPos(input.getX() + x, input.getY() + y, input.getZ() + z);
  }
  
  public BlockPos add(BlockPos input, Vec3i vec) {
    return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? input : getPos(input.getX() + vec.getX(), input.getY() + vec.getY(), input.getZ() + vec.getZ());
  }
  
  public BlockPos subtract(BlockPos input, Vec3i vec) {
    return (vec.getX() == 0 && vec.getY() == 0 && vec.getZ() == 0) ? input : getPos(input.getX() - vec.getX(), input.getY() - vec.getY(), input.getZ() - vec.getZ());
  }
  
  public BlockPos subtract(BlockPos input, int x, int y, int z) {
    return (x == 0 && y == 0 && z == 0) ? input : getPos(input.getX() - x, input.getY() - y, input.getZ() - z);
  }
  
  public BlockPos offset(BlockPos input, Direction facing) {
    return getPos(input.getX() + facing.getStepX(), input.getY() + facing.getStepY(), input.getZ() + facing.getStepZ());
  }
  
  public BlockPos offset(BlockPos input, Direction facing, int n) {
    return (n == 0) ? input : getPos(input.getX() + facing.getStepX() * n, input.getY() + facing.getStepY() * n, input.getZ() + facing.getStepZ() * n);
  }
  
  public BlockPos intern(BlockPos pos) {
    return getPos(pos.getX(), pos.getY(), pos.getZ());
  }
  
  public void clear() {
    this.pool.clear();
    this.numDuplicateLookups = 0;
  }
  
  public int size() {
    return this.pool.size();
  }
  
  public int getNumDuplicateLookups() {
    return this.numDuplicateLookups;
  }
}

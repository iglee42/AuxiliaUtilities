package fr.iglee42.auxiliautilities.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class SideHelper {
  public static Direction[][] edges = new Direction[][] { 
      { Direction.UP, Direction.WEST, Direction.NORTH }, { Direction.UP, Direction.EAST, Direction.NORTH }, { Direction.UP, Direction.NORTH, Direction.WEST }, { Direction.UP, Direction.SOUTH, Direction.WEST }, { Direction.DOWN, Direction.WEST, Direction.NORTH }, { Direction.DOWN, Direction.EAST, Direction.NORTH }, { Direction.DOWN, Direction.NORTH, Direction.WEST }, { Direction.DOWN, Direction.SOUTH, Direction.WEST }, { Direction.WEST, Direction.NORTH, Direction.UP }, { Direction.NORTH, Direction.EAST, Direction.UP }, 
      { Direction.EAST, Direction.SOUTH, Direction.UP }, { Direction.SOUTH, Direction.WEST, Direction.UP } };
  
  public static Direction[][] corners = new Direction[][] { { Direction.UP, Direction.WEST, Direction.NORTH }, { Direction.UP, Direction.WEST, Direction.SOUTH }, { Direction.UP, Direction.EAST, Direction.NORTH }, { Direction.UP, Direction.EAST, Direction.SOUTH }, { Direction.DOWN, Direction.WEST, Direction.NORTH }, { Direction.DOWN, Direction.WEST, Direction.SOUTH }, { Direction.DOWN, Direction.EAST, Direction.NORTH }, { Direction.DOWN, Direction.EAST, Direction.SOUTH } };
  
  public static Direction[][] perp_sides = new Direction[][] { { Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH }, { Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH }, { Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST }, { Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST }, { Direction.UP, Direction.DOWN, Direction.SOUTH, Direction.NORTH }, { Direction.UP, Direction.DOWN, Direction.SOUTH, Direction.NORTH } };
  
  public static Direction[][] crossProd = new Direction[6][6];
  
  static {
    BlockPos zero = new BlockPos(0, 0, 0);
    for (int i = 0; i < (Direction.values()).length; i++) {
      for (int j = 0; j < (Direction.values()).length; j++) {
        Direction dir = Direction.values()[i];
        Direction dir1 = Direction.values()[j];
        BlockPos crossP = zero.relative(dir).cross(zero.relative(dir1));
        if (!crossP.equals(zero))
          crossProd[i][j] = Direction.getNearest(crossP.getX(), crossP.getY(), crossP.getZ());
      } 
    } 
  }
}

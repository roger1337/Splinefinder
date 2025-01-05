package com.rogerr.splinefinder.pathfinder.astar.heuristic.impl;

import com.rogerr.splinefinder.pathfinder.astar.AStarNode;
import com.rogerr.splinefinder.pathfinder.astar.heuristic.AStarHeuristic;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.ChunkEvent;


// add small bias to blocks in open areas
public class HeuristicProximity extends AStarHeuristic {

    public HeuristicProximity(BlockPos start, BlockPos end) {
        super(start, end);
    }


    // scuffed solution thought of a dp solution but ceebs
    @Override
    public double getHeuristic(AStarNode n) {
        if(n.savedValues.containsKey(AStarNode.SavedValues.PROXIMITY)) {
            return n.savedValues.get(AStarNode.SavedValues.PROXIMITY);
        }

        int rad = 1;
        int count = 0;
        for(int i = -rad ; i <= rad; i++) {
            for(int j = -rad; j <= rad; j++) {
                int x = n.getX() + i;
                int z = n.getZ() + j;

                BlockPos b = new BlockPos(x, n.getY(), z);
                if(Minecraft.getMinecraft().theWorld.getBlockState(b).getBlock() instanceof BlockAir) {
                    count++;
                }
            }
        }
        double val = (9/((double)count+1))*3;
        n.savedValues.put(AStarNode.SavedValues.PROXIMITY, val);
        return val;
    }
}

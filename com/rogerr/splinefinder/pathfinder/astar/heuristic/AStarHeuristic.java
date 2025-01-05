package com.rogerr.splinefinder.pathfinder.astar.heuristic;

import com.rogerr.splinefinder.pathfinder.astar.AStarNode;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.BlockPos;

abstract public class AStarHeuristic {

    protected BlockPos start;
    protected BlockPos end;
    public AStarHeuristic(BlockPos start, BlockPos end) {
        this.start = start;
        this.end = end;
    }


    public abstract double getHeuristic(AStarNode b);
}

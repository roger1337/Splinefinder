package com.rogerr.splinefinder.pathfinder.astar.heuristic.impl;

import com.rogerr.splinefinder.pathfinder.astar.AStarNode;
import com.rogerr.splinefinder.pathfinder.astar.heuristic.AStarHeuristic;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.world.ChunkEvent;


// add small bias to blocks which are closer to the vector between start and end
public class HeuristicStraighter extends AStarHeuristic {

    public HeuristicStraighter(BlockPos start, BlockPos end) {
        super(start, end);
    }

    @Override
    public double getHeuristic(AStarNode n) {
        double a = end.getZ() - start.getZ();
        double b = end.getX() - start.getX();
        double c = (-(a*start.getX()) + (b*start.getZ()));

        double distance = Math.abs((a * n.getX() + (-b) * n.getZ() + c))/(Math.sqrt(a*a + b*b));
        return distance * 0.01;
    }
}

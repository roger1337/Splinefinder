package com.rogerr.splinefinder.pathfinder.astar;

import com.rogerr.splinefinder.pathfinder.astar.heuristic.AStarHeuristic;
import com.rogerr.splinefinder.pathfinder.astar.heuristic.impl.HeuristicProximity;
import com.rogerr.splinefinder.pathfinder.astar.heuristic.impl.HeuristicStraighter;
import com.rogerr.splinefinder.util.Util;
import net.minecraft.util.BlockPos;

import java.util.*;

public class AStarPathFinder {


    private static List<AStarHeuristic> heuristics = new ArrayList<>();


    public static List<AStarNode> compute(BlockPos start, BlockPos end, int depth) {
        heuristics.clear();
        heuristics.add(new HeuristicStraighter(start, end));
        heuristics.add(new HeuristicProximity(start, end));


        PriorityQueue<AStarNode> openQueue = new PriorityQueue<>(Comparator.comparingDouble(AStarNode::getTotalCost));
        Set<AStarNode> closedList = new TreeSet<>();

        AStarNode endNode = new AStarNode(end, heuristics);
        AStarNode startNode = new AStarNode(start, endNode, heuristics);

        // add start node
        openQueue.add(startNode);

        for(int i = 0 ; i < depth ; i++) {

            if(openQueue.isEmpty())
                return new ArrayList<>();

            AStarNode currentNode = openQueue.poll();
            closedList.add(currentNode);

            if(currentNode.equals(endNode)) {
                Util.msg("Closedlist size: " + closedList.size());
                return getPath(currentNode);
            }

            populateNeighbours(openQueue, closedList, currentNode, startNode, endNode);
        }

        return new ArrayList<>();
    }

    private static void populateNeighbours(PriorityQueue<AStarNode> openQueue, Set<AStarNode> closedList, AStarNode current, AStarNode startNode, AStarNode endNode) {
        List<AStarNode> neighbours = new ArrayList<>();
        neighbours.add(new AStarNode(-1, 0, 0, current, endNode, heuristics));
        neighbours.add(new AStarNode(0, 0, 1, current, endNode, heuristics));
        neighbours.add(new AStarNode(0, 0, -1, current, endNode, heuristics));
        neighbours.add(new AStarNode(1, 0, 0, current, endNode, heuristics));
        neighbours.add(new AStarNode(0, 1, 0, current, endNode, heuristics));
        neighbours.add(new AStarNode(0,-1,0, current, endNode, heuristics));

        for(AStarNode neighbour : neighbours) {

            if(closedList.contains(neighbour))
                continue;

            if(neighbour.getTotalCost() < current.getTotalCost() || !openQueue.contains(neighbour)) {
                if(neighbour.canBeTraversed()) {

                    if(openQueue.contains(neighbour)) {

                        neighbour.savedValues = openQueue.stream().filter(n -> n.equals(neighbour)).findFirst().get().savedValues;
                    }
                    openQueue.add(neighbour);
                }
            }
        }
    }

    private static List<AStarNode> getPath(AStarNode currentNode) {
        List<AStarNode> path = new ArrayList<>();
        path.add(currentNode);
        AStarNode parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }
}

package com.rogerr.splinefinder.pathfinder;

import com.rogerr.splinefinder.pathfinder.path.PathFinder;
import com.rogerr.splinefinder.util.Util;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class PathCommand extends CommandBase {
    public String getCommandName() {
        return "travel";
    }

    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length > 2) {
            BlockPos currentPos = Util.getPlayerBlockPos();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(currentPos.toString()));

            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            BlockPos targetPos = new BlockPos(x,y,z);


            Util.msg("Computing...");

            PathFinder.getInstance().find(currentPos, targetPos, 10000);

            List<Block> blox = new ArrayList<>();
            for(int a = 0; a < 16; a++) {
                for(int b = 0; b < 16; b++) {
                    for(int c = 0; c < 255; c++) {
                        BlockPos blockPos = new BlockPos(a,b,c);
                        blox.add(Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock());
                    }
                }

            }

            Util.msg(String.valueOf(blox.size()));

            //List<AStarNode> nodes = AStarPathFinder.compute(currentPos, targetPos, 10000);
            ///List<PathElm> path = ProcessorManager.process(nodes);
            //PathRenderer.getInstance().render(path);
        }
    }
}
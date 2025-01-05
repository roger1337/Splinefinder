package com.rogerr.splinefinder.pathfinder;

import com.rogerr.splinefinder.util.RenderUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;

public class PathRenderer {


    private static PathRenderer renderer;

    private List<BlockPos> blocks;

    private List<Vec3> points;

    public PathRenderer() {
        renderer = this;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(blocks!=null && blocks.size()>0) {
            for(BlockPos b : blocks) {
                RenderUtil.drawFilledEsp(b,  new Color(138, 206, 255));
            }
        }
        if(points!=null && points.size()>1) {
            RenderUtil.drawLines(points, 2f, event.partialTicks,  new Color(223, 6, 255).getRGB());
        }

    }

    public void setBlocks(List<BlockPos> blocks) {
        this.blocks = blocks;
    }

    public void setPoints(List<Vec3> points) {
        this.points = points;
    }

    public static PathRenderer getInstance() {
        return renderer;
    }

}

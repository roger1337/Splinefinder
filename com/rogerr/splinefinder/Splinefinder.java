package com.rogerr.splinefinder;

import com.rogerr.splinefinder.pathfinder.PathCommand;
import com.rogerr.splinefinder.pathfinder.path.PathFinder;
import com.rogerr.splinefinder.pathfinder.PathRenderer;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Arrays;

@Mod(modid = Splinefinder.MODID, version = Splinefinder.VERSION)
public class Splinefinder
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        registerCommands(new PathCommand());
        registerListeners(new PathRenderer(), new PathFinder());
    }


    private void registerCommands(ICommand... commands) {
        Arrays.stream(commands).forEachOrdered(ClientCommandHandler.instance::registerCommand);
    }

    private void registerListeners(Object... listeners) {
        Arrays.stream(listeners).forEachOrdered(MinecraftForge.EVENT_BUS::register);
    }
}

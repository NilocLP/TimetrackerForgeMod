package de.niloc.timetracker;

import de.niloc.timetracker.commands.SeeTimeCommand;
import de.niloc.timetracker.commands.TimeSetCommand;
import de.niloc.timetracker.commands.TimetrackerAdminCommand;
import de.niloc.timetracker.listener.*;
import de.niloc.timetracker.playtime.PlayerManager;
import de.niloc.timetracker.playtime.PlaytimeManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

import java.io.File;

@Mod(
        modid = Timetracker.MOD_ID,
        name = Timetracker.MOD_NAME,
        version = Timetracker.VERSION
)
public class Timetracker {

    public static final String MOD_ID = "timetracker";
    public static final String MOD_NAME = "Timetracker";
    public static final String VERSION = "1.0-SNAPSHOT";
    private static String modConfigLocation;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static Timetracker INSTANCE;
    private static PlaytimeManager playtimeManager = new PlaytimeManager();
    private static PlayerManager playerManager = new PlayerManager();

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        File modConfigLocation = event.getModConfigurationDirectory();
        Timetracker.modConfigLocation = modConfigLocation.toString();
        try {
            playtimeManager.loadTeams(modConfigLocation.toString());
        }catch (RuntimeException e){
            FMLLog.log.info("Config not found. Creating one");
            playtimeManager.saveTeams(modConfigLocation.toString());
        }

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TeamLoadedHandler());
        MinecraftForge.EVENT_BUS.register(new TeamCreateHandler());
        MinecraftForge.EVENT_BUS.register(new TeamDeletedHandler());
        MinecraftForge.EVENT_BUS.register(new ServerTickListener());
        MinecraftForge.EVENT_BUS.register(new PlayerLeaveHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerJoinHandler());
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void stop(FMLServerStoppingEvent event){
        Timetracker.getPlaytimeManager().saveTeams(Timetracker.getModConfigLocation());
    }

    @Mod.EventHandler
    public void registerCommand(FMLServerStartingEvent event){
        ServerCommandManager commandManager = (ServerCommandManager) event.getServer().getCommandManager();
        commandManager.registerCommand(new TimetrackerAdminCommand());
        commandManager.registerCommand(new TimeSetCommand());
        commandManager.registerCommand(new SeeTimeCommand());
    }


    public static PlaytimeManager getPlaytimeManager(){
        return playtimeManager;
    }

    public static String getModConfigLocation(){
        return modConfigLocation;
    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }
}

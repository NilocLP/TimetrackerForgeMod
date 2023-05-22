package de.niloc.timetracker.listener;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import de.niloc.timetracker.Timetracker;
import de.niloc.timetracker.playtime.PlaytimeManager;
import de.niloc.timetracker.playtime.TimerTeam;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.time.format.DateTimeFormatter;

public class PlayerJoinHandler {

    @SubscribeEvent
    public void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if (!(event.player instanceof EntityPlayerMP))
            return;

        Timetracker.getPlayerManager().getPlayers().add((EntityPlayerMP)event.player);
        Short teamId = FTBLibAPI.getTeamID(event.player.getUniqueID());
        PlaytimeManager pm = Timetracker.getPlaytimeManager();
        TimerTeam tt = pm.getTeam(teamId);

        if(tt == null){
            return;
        }

        if(tt.isTimeOver()){
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            TextComponentString kickMessage = new TextComponentString(TextFormatting.RED + "Deine Team-Spielzeit ist vorbei!" +
                    "\n\n" +
                    TextFormatting.YELLOW + "Du kannst am " + pm.nextResetIn().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " wieder spielen!");

            player.connection.onDisconnect(kickMessage);
            player.connection.disconnect(kickMessage);
        }

    }
}

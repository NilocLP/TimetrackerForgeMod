package de.niloc.timetracker.listener;

import com.feed_the_beast.ftblib.events.team.ForgeTeamLoadedEvent;
import de.niloc.timetracker.Timetracker;
import de.niloc.timetracker.playtime.PlaytimeManager;
import de.niloc.timetracker.playtime.TimerTeam;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



public class TeamLoadedHandler {

    @SubscribeEvent
    public void onTeamLoadedEvent(ForgeTeamLoadedEvent event){
        PlaytimeManager pm = Timetracker.getPlaytimeManager();
        Short teamId = event.getTeam().getUID();
        TimerTeam matchingTeam = pm.getTeam(teamId);
        if(matchingTeam == null){
            pm.addTeam(event.getTeam(), pm.getDefaultPlaytime());
        }else {
            pm.assignForgeTeamToMatchingTeam(event.getTeam());
        }

    }


}

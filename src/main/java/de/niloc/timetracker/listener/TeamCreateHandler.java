package de.niloc.timetracker.listener;

import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import de.niloc.timetracker.Timetracker;
import de.niloc.timetracker.playtime.PlaytimeManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeamCreateHandler {

    @SubscribeEvent
    public void onTeamLoadedEvent(ForgeTeamCreatedEvent event){
        PlaytimeManager pm = Timetracker.getPlaytimeManager();
        pm.addTeam(event.getTeam(), pm.getDefaultPlaytime());
    }
}

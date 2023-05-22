package de.niloc.timetracker.listener;

import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import de.niloc.timetracker.Timetracker;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeamDeletedHandler {

    @SubscribeEvent
    public void onTeamDeletion(ForgeTeamDeletedEvent event){
        Short id = event.getTeam().getUID();
        if(id == null){
            return;
        }
        Timetracker.getPlaytimeManager().removeTeam(id);
    }
}


package de.niloc.timetracker.listener;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import de.niloc.timetracker.Timetracker;
import de.niloc.timetracker.playtime.PlaytimeManager;
import de.niloc.timetracker.playtime.TimerTeam;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.format.DateTimeFormatter;

public class ServerTickListener {

    private int tick;
    private int second;

    private PlaytimeManager pt = Timetracker.getPlaytimeManager();

    @SubscribeEvent
    public void onServerTick(TickEvent event){
        if (!event.side.isServer() || event.phase != TickEvent.Phase.START || event.type != TickEvent.Type.SERVER)
            return;
        tick++;

        if (tick % 20 != 0) {
           return;
        }
        second++;

        if(second % (10*60) == 0){
            Timetracker.getPlaytimeManager().saveTeams(Timetracker.getModConfigLocation());

            //Check if reset is required
            boolean resetRequired = Timetracker.getPlaytimeManager().resetRequired();
            if(resetRequired){
                System.out.println("Team Playtime wird resetet");
                Timetracker.getPlaytimeManager().resetTeams();
                Timetracker.getPlaytimeManager().saveTeams(Timetracker.getModConfigLocation());
            }

        }
        if(second % 60 == 0) {

            Timetracker.getPlayerManager().getPlayers().forEach(player -> {
                Short teamId = Short.valueOf(FTBLibAPI.getTeamID(player.getUniqueID()));
                TimerTeam tt = this.pt.getTeam(teamId.shortValue());
                if (tt == null)
                    return;
                tt.increaseUsedTime(60);
                boolean timeOver = tt.isTimeOver();
                if (timeOver)
                    player.connection.disconnect(new TextComponentString(TextFormatting.RED + "Deine Team-Spielzeit ist vorbei!\n\n" + TextFormatting.YELLOW + "Du kannst am " + this.pt.nextResetIn().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " wieder spielen!"));
            });


        }
    }

    private void kickOnlineTeamPlayers(TimerTeam tt){
        tt.getBaseTeam().getOnlineMembers().forEach((entityPlayerMP) -> {
            entityPlayerMP.connection.disconnect(new TextComponentString(TextFormatting.RED + "Deine Team-Spielzeit ist vorbei!" +
                    "\n\n" +
                    TextFormatting.YELLOW + "Du kannst am " + Timetracker.getPlaytimeManager().nextResetIn().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " wieder spielen!"));
        });
    }
}

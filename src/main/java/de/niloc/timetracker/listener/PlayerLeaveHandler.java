package de.niloc.timetracker.listener;

import de.niloc.timetracker.Timetracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLeaveHandler {

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        if (!(event.player instanceof EntityPlayerMP))
            return;

        Timetracker.getPlayerManager().getPlayers().remove(event.player);
    }
}

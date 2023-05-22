package de.niloc.timetracker.playtime;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class PlayerManager {
    ArrayList<EntityPlayerMP> players = new ArrayList<>();

    public ArrayList<EntityPlayerMP> getPlayers() {
        return this.players;
    }
}

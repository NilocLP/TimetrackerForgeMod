package de.niloc.timetracker.commands;

import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import de.niloc.timetracker.Timetracker;
import de.niloc.timetracker.playtime.TimerTeam;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TimeSetCommand extends CommandBase {
    @Override
    public String getName() {
        return "ttSetTeamTime";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/ttSetTeamTime <player> <timeInSeconds>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Bitte verwende: /ttSetTeamTime <player> <timeInSeconds>"));
            return;
        }
        EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[0]);
        if(player == null){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Der Spieler konnte nicht gefunden werden"));
            return;
        }
        TimerTeam tt = Timetracker.getPlaytimeManager().getTeam(FTBLibAPI.getTeamID(player.getUniqueID()));
        int time = Integer.parseInt(args[1]);
        if(!(time > 0)){
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Die Zeit-Angabe stimmt nicht"));
            return;
        }
        tt.setMaxPlaytime(time);
        sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Die Maximale Zeit vom Team " + tt.getId() + " wurde auf " + time/60 + " Minuten gesetzt"));
    }
}

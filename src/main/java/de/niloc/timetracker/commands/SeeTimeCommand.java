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

public class SeeTimeCommand extends CommandBase {
    @Override
    public String getName() {
        return "seeTime";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/seeTime";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(!(sender instanceof EntityPlayerMP)){
            sender.sendMessage(new TextComponentString("You must a Player to do this"));
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) sender;
        Short teamId = FTBLibAPI.getTeamID(player.getUniqueID());
        if(teamId == null){
            player.sendMessage(new TextComponentString(TextFormatting.RED + "Es gab ein Problem dein Team zu ermitteln. Sicher das du in einem Team bist?"));
        }

        TimerTeam tt = Timetracker.getPlaytimeManager().getTeam(teamId);
        int remainingTime = tt.getMaxPlaytime() - tt.getTimeUsed();
        int remainingMinutes = (remainingTime / 60) % 60;
        int remainingHours = (remainingTime / 60) / 60;
        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Dein Team hat noch " + remainingHours + " Stunden und " + remainingMinutes + " Minuten Spielzeit übrig"));

    }
}

package de.niloc.timetracker.commands;

import de.niloc.timetracker.Timetracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TimetrackerAdminCommand extends CommandBase {
    @Override
    public String getName() {
        return "timetrackerAdmin";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/timetrackerAdmin <save/allTeams>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0){
            sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Bitte verwende: /timetrackerAdmin <save/allTeams>"));
            return;
        }
        switch (args[0]){
            case "save":
                Timetracker.getPlaytimeManager().saveTeams(Timetracker.getModConfigLocation());
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Config gespeichert"));

                break;
            case "allTeams":
                Timetracker.getPlaytimeManager().getTeams().forEach((id, team) ->{
                    sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "" + team.getId() + ": " + team.getTimeUsed() / 60 + " minutes used"));
                });
                break;
        }
    }

}

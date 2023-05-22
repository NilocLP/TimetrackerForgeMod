package de.niloc.timetracker.playtime;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaytimeManager {

    private HashMap<Short, TimerTeam> teams = new HashMap<>();
    private long resetPeriodInSeconds = 604800;
    private LocalDateTime lastReset = LocalDateTime.MIN;
    private int defaultPlaytime = 100800;

    public PlaytimeManager(){
    }

    public void addTeam(ForgeTeam team, int maxPlaytime){
        teams.put(team.getUID(), new TimerTeam(team, maxPlaytime));
    }
    public void assignForgeTeamToMatchingTeam(ForgeTeam team){
        this.getTeam(team.getUID()).setBaseTeam(team);
    }
    public void removeTeam(short teamId){
        teams.remove(teamId);
    }
    public TimerTeam getTeam(short teamId){
        return teams.get(teamId);
    }
    public HashMap<Short, TimerTeam> getTeams() {
        return teams;
    }

    public long getResetPeriodInSeconds() {
        return resetPeriodInSeconds;
    }
    public void setResetPeriodInSeconds(long resetPeriodInSeconds) {
        this.resetPeriodInSeconds = resetPeriodInSeconds;
    }

    public boolean resetRequired(){
        if(this.resetPeriodInSeconds <= 0){
            System.err.println("Incorrect Reset-Period");
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        long elapsed = lastReset.until(now, ChronoUnit.SECONDS);
        return elapsed > resetPeriodInSeconds;
    }
    public void resetTeams(){
        this.lastReset = LocalDateTime.now();
        teams.forEach((id, team) -> team.resetTeam());
    }
    public LocalDateTime nextResetIn(){
        LocalDateTime now = LocalDateTime.now();
        long elapsed = lastReset.until(now, ChronoUnit.SECONDS);
        long resetIn = resetPeriodInSeconds - elapsed;
        return now.plusSeconds(resetIn);
    }

    public int getDefaultPlaytime() {
        return defaultPlaytime;
    }
    public void increaseUsedTime(int seconds){
        teams.forEach((id, team) -> {
            team.increaseUsedTime(seconds);
        });
    }
    public void decreaseUsedTime(int seconds){
        teams.forEach((id, team) -> {
            team.decreaseUsedTime(seconds);
        });
    }

    public void saveTeams(String folderLocation){
        String saveLocation = folderLocation;
        if(!saveLocation.endsWith("/")){
            saveLocation = saveLocation + "/";
        }
        saveLocation = saveLocation + "timetracker-teamData.json";

        Gson gson = new Gson();
        try {
            FileWriter fileWriter = new FileWriter(saveLocation);
            gson.toJson(this.getDataAsValidJson(), fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadTeams(String folderLocation){

        String loadLocation = folderLocation;
        if(!loadLocation.endsWith("/")){
            loadLocation = loadLocation + "/";
        }

        loadLocation = loadLocation + "timetracker-teamData.json";
        HashMap<String, Object> tempData;
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(loadLocation));
            tempData = gson.fromJson(reader, HashMap.class);
            turnJsonIntoThisClass(tempData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private HashMap<String, Object> getDataAsValidJson(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("resetPeriodInSeconds",this.resetPeriodInSeconds);
        data.put("lastReset", this.lastReset);
        data.put("defaultPlaytime", this.defaultPlaytime);

        ArrayList<HashMap<String, Object>> validTeams = new ArrayList<>();
        this.teams.forEach((id, team) -> {
            HashMap<String, Object> teamData = new HashMap<>();
            teamData.put("timesUsed",team.getTimeUsed());
            teamData.put("teamId",team.getId());
            teamData.put("maxPlaytime",team.getMaxPlaytime());
            teamData.put("timeOver",team.isTimeOver());
            validTeams.add(teamData);
        });
        data.put("teams",validTeams);
        return data;
    }
    private void turnJsonIntoThisClass(HashMap<String, Object> data){
        LinkedTreeMap<String, ?> timestamp = (LinkedTreeMap<String, ?>) data.get("lastReset");
        LinkedTreeMap<String, Double> date = (LinkedTreeMap<String, Double>) timestamp.get("date");
        LinkedTreeMap<String, Double> time = (LinkedTreeMap<String, Double>) timestamp.get("time");

        Double year = date.get("year");
        Double month = date.get("month");
        Double day = date.get("day");
        Double hour = date.get("hour");
        Double minute = date.get("minute");
        Double second = date.get("second");
        Double nano = date.get("nano");

        if(year == null) year = 0.0;
        if(month == null) month = 0.0;
        if(day == null) day = 0.0;
        if(hour == null) hour = 0.0;
        if(minute == null) minute = 0.0;
        if(second == null) second = 0.0;
        if(nano == null) nano = 0.0;

        Double resetPeriodInSeconds = (Double) data.get("resetPeriodInSeconds");
        Double defaultPlaytime = (Double) data.get("defaultPlaytime");

        this.lastReset = LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(), hour.intValue(), minute.intValue(), second.intValue(), nano.intValue());
        this.resetPeriodInSeconds = resetPeriodInSeconds.longValue();
        this.defaultPlaytime = defaultPlaytime.intValue();

        Object teams = data.get("teams");
        ArrayList<LinkedTreeMap<String, Object>> validTeams = (ArrayList<LinkedTreeMap<String, Object>>) teams;
        validTeams.forEach((team) -> {
            Double timeUsed = (Double) team.get("timesUsed");
            Double maxPlaytime = (Double) team.get("maxPlaytime");
            Double teamId = (Double) team.get("teamId");
            boolean timeOver = (boolean) team.get("timeOver");

            TimerTeam tempTimerTeam = new TimerTeam(teamId.shortValue(), maxPlaytime.intValue(), timeUsed.intValue(), timeOver);
            this.teams.put(teamId.shortValue(), tempTimerTeam);
        });
    }
}

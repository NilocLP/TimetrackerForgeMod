package de.niloc.timetracker.playtime;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

public class TimerTeam {

    private short id;
    private int maxPlaytime = -1;
    private int timeUsed = 0;
    private ForgeTeam baseTeam;
    private boolean timeOver;

    protected TimerTeam(ForgeTeam team, int maxPlaytime){
        this.id = team.getUID();
        this.maxPlaytime = maxPlaytime;
    }
    protected TimerTeam(short id, int maxPlaytime, int timeUsed, boolean timeOver){
        this.id = id;
        this.maxPlaytime = maxPlaytime;
        this.timeUsed = timeUsed;
    }

    public int getMaxPlaytime() {
        return maxPlaytime;
    }
    public void setMaxPlaytime(int maxPlaytime) {
        this.maxPlaytime = maxPlaytime;
    }
    public int getTimeUsed() {
        return timeUsed;
    }
    public ForgeTeam getBaseTeam() {
        return baseTeam;
    }
    public short getId() {
        return id;
    }
    protected void setBaseTeam(ForgeTeam baseTeam) {
        this.baseTeam = baseTeam;
    }
    protected void setTimeOver(boolean timeOver) {
        this.timeOver = timeOver;
    }
    protected void resetTeam(){
        this.timeUsed = 0;
        this.timeOver = false;
    }

    public boolean isTimeOver() {
        return timeOver;
    }

    public void decreaseUsedTime(int seconds){
        if(seconds > this.timeUsed || seconds < 0){
            throw new NumberFormatException();
        }
        this.timeUsed -= seconds;
        if(this.timeUsed < this.maxPlaytime && this.timeOver){
            this.timeOver = false;
        }
    }
    public void increaseUsedTime(int seconds){
        if(seconds < 0){
            throw new NumberFormatException();
        }
        this.timeUsed += seconds;
        if(this.timeUsed > this.maxPlaytime && !this.timeOver){
            this.timeOver = true;
        }

    }


}

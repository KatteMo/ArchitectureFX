package project;

public class Source {

    private int numberOfSource;
    private int countOfTasks;
    private int countOfDenyTasks;
    private double timeOfUsing;
    private double timeOfWaiting;

    public Source(int numberOfSource) {
        this.numberOfSource = numberOfSource;
        this.countOfTasks = 0;
        this.countOfDenyTasks = 0;
        this.timeOfUsing = 0;
        this.timeOfWaiting = 0;
    }

    void addNewTask() { countOfTasks++; }

    void addDenyTask() {
        countOfDenyTasks++;
    }

    void addTimeUse(double time) {
        timeOfUsing += time;
    }

    void addTimeWait(double time) {
        timeOfWaiting += time;
    }

    public int getNumberOfSource() {
        return numberOfSource;
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    public double getDenyProb() { return countOfDenyTasks * 1.0/countOfTasks; }

    public double getAverageTimeUsing(){ return timeOfUsing/countOfTasks; }

    public double getAverageTimeWaiting(){ return timeOfWaiting/countOfTasks; }

    public double getAverageTimeStaying(){ return getAverageTimeUsing() + getAverageTimeWaiting(); }

    public double getTimeUsingDispersion(){return Math.pow(getAverageTimeUsing(), 2) / countOfTasks;}

    public double getTimeWaitingDispersion(){return Math.pow(getAverageTimeWaiting(), 2) / countOfTasks;}
}

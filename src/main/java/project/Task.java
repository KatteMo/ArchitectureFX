package project;

public class Task {

    private String TaskId;
    private double makingTime = 0;
    private int sourceNumber;
    private double timeOfUsing;

    public Task(int numberOfTask, int sourceNumber, double makingTime, double timeOfUsing) {
        this.TaskId = Integer.toString(sourceNumber) + '.' + numberOfTask;
        this.sourceNumber = sourceNumber;
        this.makingTime = makingTime;
        this.timeOfUsing = timeOfUsing;
    }

    int getIdOfTask()
    {
        return Integer.parseInt(TaskId.substring(TaskId.indexOf(".")+1));
    }

    public String getNumberOfTask() {
        return TaskId;
    }

    public void setIdOfTask(String idOfTask) {
        this.TaskId = idOfTask;
    }

    public double getMakingTime() {
        return makingTime;
    }

    public void setMakingTime(double makingTime) {
        this.makingTime = makingTime;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public double getTimeOfUsing() {
        return timeOfUsing;
    }

    public void setTimeOfUsing(double timeOfUsing) {
        this.timeOfUsing = timeOfUsing;
    }

    public void setSourceNumber(int sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

//    public double makeShorter(double num){
//        String formattedDouble = String.format("%.3f", num);
//        return Double.parseDouble(formattedDouble.replace(",","."));
//    }

    @Override
    public String toString() {

        return "Task {" +
                "TaskId:'" + TaskId + '\'' +
                ", makingTime:" + makingTime +
                ", timeOfUsing:" + timeOfUsing +
                ", sourceNumber:" + sourceNumber +
                '}';
    }
}

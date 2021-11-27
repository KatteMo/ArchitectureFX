package project;

public class Device {

    private int numberOfDevice;
    private double startTime;
    private double endTime;
    private double busyTime;

    public Device(int numberOfDevice, int startTime, int endTime) {
        this.numberOfDevice = numberOfDevice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.busyTime = 0;
    }


    public double getBusyTime() {
        return busyTime;
    }

    public Device(int numberOfDevice) {
        this.numberOfDevice = numberOfDevice;
    }

    public void addTask(Task task)
    {
        System.out.println("You add " + task + " to " + this);
    }

    boolean isFree(double currTime) {
        return currTime >= endTime;
    }

    public int getNumberOfDevice() {
        return numberOfDevice;
    }

    public void addTimeOfWork(double time){
        busyTime+=time;
    }

    public void setNumberOfDevice(int numberOfDevice) {
        this.numberOfDevice = numberOfDevice;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public double makeShorter(double num){
        String formattedDouble = String.format("%.3f", num);
        return Double.parseDouble(formattedDouble.replace(",","."));
    }

    public String toString() {

        return "Device {" +
                "DeviceId:'" + numberOfDevice + '\'' +
                ", startTime:" + makeShorter(startTime) +
                ", endTime:" + makeShorter(endTime) +
                '}';
    }

}


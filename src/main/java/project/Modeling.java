package project;

public class Modeling {

    private int bufferSize;
    private Generator generator;
    private Controller controller;
    public Buffer buffer;
    private int countOfTasks;
    private double p0;
    private double p1;
    private int count0;
    private int count1;
    private double t2 = 1.643 * 1.643;
    private double a = 0.9;
    private double b2 = 0.1 * 0.1;

    public Modeling(int countOfDevices, int countOfSources, int bufferSize, double lambda, int countOfTasks, double min, double max) {
        this.bufferSize = bufferSize;
        this.countOfTasks = countOfTasks;
        this.count1 = countOfTasks;
        this.p0 = 0;

        controller = new Controller(countOfDevices, countOfSources, lambda);
        generator = new Generator(min, max, lambda);
        buffer = new Buffer(bufferSize);
    }

    public void startByCountOfTask(int countOfTasks) {
        generator.autoGenerateTask(buffer, countOfTasks, controller);
    }

    public void beginAutoMode() {
        startByCountOfTask(count1);
        p1 = controller.getDenyOfSources();
        controller.showStats();
        while (Math.abs(p0 - p1) >= p0 * 0.1)
        {
            p0 = p1;
            count0 = count1;
            count1 = (int) ((t2 * (1 - p0)) / (p0 * b2));
            erase();
            startByCountOfTask(count1);
            p1 = controller.getDenyOfSources();
            System.out.println("num: " + count0);
        }
        System.out.println("the end: " + count1);

        controller.showStats();
    }

    public void erase(){
        controller.erase();
        buffer = new Buffer(bufferSize);
    }

    public void beginStepByStepMode() {
        startByCountOfTask(countOfTasks);
    }

    public Controller getController() {
        return controller;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}

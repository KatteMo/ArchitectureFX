package project;

public class Generator {

    private double minTimeDevice;
    private double maxTimeDevice;
    private double lambda;
    public Generator(double min, double max, double lambda) {
        this.minTimeDevice = min;
        this.maxTimeDevice = max;
        this.lambda = lambda;
    }

    public void autoGenerateTask(Buffer buffer, int countOfTasks, Controller controller) {
        double delta = 0;
        double currTime = 0;
        buffer.fillIn(controller.getCountOfSources());
        int i = 0;
        while (i < countOfTasks) {
            delta = (-1 / lambda) * Math.log(Math.random());
            currTime += delta;
            controller.createTask(buffer, makeShorter(currTime), makeShorter(generateTimeOfUsing()));
            i++;
        }
        //System.out.println("\nЗаявки созданы");
        controller.finishWork(buffer, currTime);
    }

    public double generateTimeOfUsing() {
        return Math.random() * maxTimeDevice + minTimeDevice;
    }

    public double makeShorter(double num) {
        String formattedDouble = String.format("%.3f", num);
        return Double.parseDouble(formattedDouble.replace(",", "."));
    }
}


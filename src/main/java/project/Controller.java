package project;

import java.util.ArrayList;

public class Controller {
    private int countOfDevices;
    private int countOfSources;
    private double lambda;
    private double workingTime;

    ArrayList<Source> sources = new ArrayList<>();
    ArrayList<Device> devices = new ArrayList<>();
    ArrayList<Device> finishDevices = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();


    public Controller(int countOfDevices, int countOfSources, double lambda) {
        this.countOfDevices = countOfDevices;
        this.countOfSources = countOfSources;
        this.lambda = lambda;
        generateByType(Type.SOURCE);
        generateByType(Type.DEVICE);
    }

    enum Type {
        SOURCE,
        DEVICE
    }

    public int getCountOfSources() {
        return countOfSources;
    }

    void createTask(Buffer buffer, double startTime, double timeOfUsing) {
        fillFreeDevices(startTime);
        if (!finishDevices.isEmpty() && !buffer.isEmpty()) {
            for (Device curDev : finishDevices) {
                if (!buffer.isEmpty()) {
                    //System.out.println("\nЗакончил девайс: " + curDev);
                    Task insTask = buffer.outTask();
                    if (setTaskOnDeviceByDevice(insTask, curDev.getNumberOfDevice())) {
                        buffer.taskGoToDevice();
                    }
                }
            }
            finishDevices.clear();
        }

        int currSourceNum = buffer.getMinWorkload();
        Source currSource = sources.get(currSourceNum - 1);
        currSource.addNewTask();
        Task newTask = new Task(currSource.getCountOfTasks(), currSource.getNumberOfSource(), startTime, timeOfUsing);
        currSource.addTimeUse(newTask.getTimeOfUsing());
        int pointer = buffer.getPointer();
        if (pointer == 0) {
            pointer = 6;
        }
        addEvent(EventType.generateTask, newTask.getNumberOfTask(),
                "Место куда можем поставить заявку, если свободно: " + pointer);

        boolean next_step = buffer.addTask(newTask);

        if (!next_step) {
            Task chosenTask = buffer.denyTask(newTask);
            pointer = buffer.getPointer();
            if (pointer == 0) {
                pointer = 6;
            }
            currSource.addDenyTask();
            addEvent(EventType.denyTask, chosenTask.getNumberOfTask(),
                    "Место в буфере откуда убрали заявку: " + pointer);
        } else {
            Task chosenTask = buffer.outTask();
            if (setTaskOnDeviceByBuffer(chosenTask, startTime)) {
                buffer.taskGoToDevice();
            }
        }
    }

    void generateByType(Type type) {
        if (type == Type.SOURCE) {
            for (int i = 1; i <= this.countOfSources; i++) {
                sources.add(new Source(i));
            }
            //System.out.println("Created sources: " + countOfSources);
        } else if (type == Type.DEVICE) {
            for (int i = 1; i <= this.countOfDevices; i++) {
                devices.add(new Device(i));
            }
            //System.out.println("Created devices: " + countOfDevices);
        }
    }

    public void fillFreeDevices(double currTime) {
        for (Device currentDevice : devices) {
            if (currentDevice.isFree(currTime) && currentDevice.getStartTime() != 0.0) {
                if (finishDevices.isEmpty()) {
                    finishDevices.add(currentDevice);
                } else {
                    int curSize = finishDevices.size();
                    boolean isAdd = false;
                    for (int i = 0; i < curSize; i++) {
                        if (finishDevices.get(i).getEndTime() > currentDevice.getEndTime()) {
                            finishDevices.add(i, currentDevice);
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        finishDevices.add(curSize, currentDevice);
                    }
                }
            }
        }
    }

    public boolean setTaskOnDeviceByBuffer(Task task, double currTime) {
        for (Device currentDevice : devices) {
            if (currentDevice.isFree(currTime)) {
                if (currentDevice.getStartTime() != 0) {
                    addEvent(EventType.finishDevice, findEventByNum(currentDevice.getNumberOfDevice()),
                            "Номер закончившего девайса: " + (currentDevice.getNumberOfDevice()));
                }
                currentDevice.setStartTime(currTime);
                currentDevice.setEndTime(currTime + task.getTimeOfUsing());
                //currentDevice.addTask(task);
                addEvent(EventType.startDevice, task.getNumberOfTask(),
                        "Номер начавшего работу девайса: " + (currentDevice.getNumberOfDevice()));
                sources.get(task.getSourceNumber() - 1).addTimeWait(currTime - task.getMakingTime());
                currentDevice.addTimeOfWork(task.getTimeOfUsing());
                return true;
            }
        }
        return false;
    }

    public boolean setTaskOnDeviceByDevice(Task task, int num) {
        for (Device currentDevice : devices) {
            if (currentDevice.getNumberOfDevice() == num) {
                if (currentDevice.getStartTime() != 0) {
                    addEvent(EventType.finishDevice, findEventByNum(currentDevice.getNumberOfDevice()),
                            "Номер закончившего девайса: " + (currentDevice.getNumberOfDevice()));
                }
                double time = currentDevice.getEndTime();
                currentDevice.setStartTime(time);
                currentDevice.setEndTime(time + task.getTimeOfUsing());
                //currentDevice.addTask(task);
                addEvent(EventType.startDevice, task.getNumberOfTask(),
                        "Номер начавшего работу девайса: " + (currentDevice.getNumberOfDevice()));
                sources.get(task.getSourceNumber() - 1).addTimeWait(time - task.getMakingTime());
                currentDevice.addTimeOfWork(task.getTimeOfUsing());
                return true;
            }
        }
        return false;
    }

    public void finishWork(Buffer buffer, double currTime) {
        double delta = 0;
        int freeDevices = 0;
        while (true) {
            fillFreeDevices(currTime);
            if (!finishDevices.isEmpty()) {
                if (!buffer.isEmpty()) {
                    for (Device curDev : finishDevices) {
                        if (!buffer.isEmpty()) {
                            //System.out.println("\nЗакончил девайс: " + curDev);
                            Task insTask = buffer.outTask();
                            if (setTaskOnDeviceByDevice(insTask, curDev.getNumberOfDevice())) {
                                buffer.taskGoToDevice();
                            }
                        }
                    }
                } else {
                    for (Device curDev : finishDevices) {
                        freeDevices++;
                        //System.out.println("\nОкончательно закончил девайс: " + curDev);
                        addEvent(EventType.finishDevice, findEventByNum(curDev.getNumberOfDevice()),
                                "Номер закончившего девайса: " + (curDev.getNumberOfDevice()));
                        if (freeDevices == countOfDevices) {
                            workingTime = curDev.getEndTime();
                            break;
                        }
                        curDev.setStartTime(0);
                    }
                }
                finishDevices.clear();
            }
            if (freeDevices == countOfDevices) {
                break;
            }
            delta = (-1 / lambda) * Math.log(Math.random());
            currTime += delta;
        }
        //System.out.println(events);
    }

    public void addEvent(EventType type, String eventId, String addInfo) {
        Event currEvent = new Event(type, eventId, addInfo);
        events.add(currEvent);
    }

    public String findEventByNum(int num) {
        for (Event curEvent : events) {
            int curNum = getNumOfDev(curEvent);
            if (curNum == num) {
                return curEvent.getTaskId();
            }
        }
        return "";
    }

    public int getNumOfDev(Event event) {
        String string = event.getAdditionInfo();
        int len = string.length();
        int i = string.lastIndexOf(" ") + 1;
        string = string.substring(i);
        return Integer.parseInt(string);
    }

    public void showStats() {
        new Table(sources, devices, workingTime);
    }

    public double getDenyOfSources() {
        double sum = 0;
        for (Source curSou : sources) {
            sum += curSou.getDenyProb();
        }
        return sum / countOfSources;
    }

    public void erase() {
        for (int i = 0; i < sources.size(); i++) {
            sources.set(i, new Source(i + 1));
        }
        for (int i = 0; i < devices.size(); i++) {
            devices.set(i, new Device(i + 1, 0, 0));
        }
        events.clear();
    }

    public void nextStep(Buffer buffer, int countOfTasks, Controller controller) {

    }

//    public double makeShorter(double num) {
//        String formattedDouble = String.format("%.3f", num);
//        return Double.parseDouble(formattedDouble.replace(",", "."));
//    }
}

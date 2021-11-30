package project;

import javax.xml.stream.FactoryConfigurationError;
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

    public int getCountOfDevices() {
        return countOfDevices;
    }

    public ArrayList<Event> getEvents() {
        return events;
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
        int pointer;

        boolean next_step = buffer.addTask(newTask);

        if (!next_step) {
            pointer = buffer.getPointer();
            addEvent(EventType.generateTask, newTask.getNumberOfTask(),
                    "Место куда можем поставить заявку, если свободно: " + (pointer + 1));
            Task chosenTask = buffer.denyTask(newTask);
            pointer = buffer.getPointer();
            if (pointer == 0) {
                pointer = buffer.getSize();
            }
            currSource.addDenyTask();
            addEvent(EventType.denyTask, chosenTask.getNumberOfTask(),
                    "Место в буфере откуда убрали заявку: " + pointer);
        } else {
            Task chosenTask = buffer.outTask();
            pointer = buffer.getPointer();
            if (pointer == 0){
                pointer = buffer.getSize();
            }
            addEvent(EventType.generateTask, newTask.getNumberOfTask(),
                    "Место куда поставили заявку: " + (pointer));
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
                    addEvent(EventType.finishDevice, findEventByNum(currentDevice),
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
                    addEvent(EventType.finishDevice, findEventByNum(currentDevice),
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
                        //System.out.println("\nОкончательно закончил девайс: " + curDev);
                        addEvent(EventType.finishDevice, findEventByNum(curDev),
                                "Номер закончившего девайса: " + (curDev.getNumberOfDevice()));
                        curDev.setStartTime(0);
                        if (isAllFinish()) {
                            workingTime = curDev.getEndTime();
                            break;
                        }
                    }
                }
                finishDevices.clear();
            }
            if (isAllFinish()) {
                break;
            }
            delta = (-1 / lambda) * Math.log(Math.random());
            currTime += delta;
        }
        //System.out.println(events);
    }

    public boolean isAllFinish() {
        for (Device curDev : devices) {
            if (curDev.getStartTime() != 0) {
                return false;
            }
        }
        return true;
    }

    public void addEvent(EventType type, String eventId, String addInfo) {
        Event currEvent = new Event(type, eventId, addInfo);
        events.add(currEvent);
    }

    public String findEventByNum(Device dev) {
        int s = events.size();
        for (int i = s-1; i >= 0; i--) {
            Event curEv = events.get(i);
            int curNum = getNumOfDev(curEv);
            if (curNum == dev.getNumberOfDevice() && curEv.getType() == EventType.startDevice) {
                return curEv.getTaskId();
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
}

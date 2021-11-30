package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Buffer {

    private ArrayList<Task> arrayTask = new ArrayList<Task>();
    private int size;
    private int pointer;
    private int outPointer;
    private int occupied_size;
    private Map<Integer, Integer> countPackages = new HashMap<>();

    public Buffer(int new_size) {
        size = new_size;
        pointer = 0;
        outPointer = 0;
        occupied_size = 0;
        //System.out.println("Created buffer size:" + size);
    }

    public int getOldestTask() {
        double maxTime = 1000000;
        int oldest = 0;
        for (int i = 0; i < arrayTask.size(); i++) {
            Task currTask = arrayTask.get(i);
            if (currTask != null) {
                double currTime = currTask.getMakingTime();
                if (currTime < maxTime) {
                    maxTime = currTime;
                    oldest = i;
                }
            }
        }
        return oldest;
    }

    public int getMinWorkload() {
        int minValue = 1000000;
        Random random = new Random();
        int minSource = random.nextInt(countPackages.size());
        for (int key : countPackages.keySet()) {
            int curr = countPackages.get(key);
            if (curr < minValue) {
                minValue = curr;
                minSource = key;
            }
        }
        return minSource;
    }

    public void fillIn(int sources) {
        for (int i = 1; i <= sources; i++) {
            countPackages.put(i, 0);
        }
    }

    public boolean addTask(Task task) {
        if (this.occupied_size < size) {
            for (int i = pointer; i < size; i++) {
                try {
                    if (arrayTask.get(i) == null) {
                        pointer = i;
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    arrayTask.add(i, null);
                    pointer = i;
                    break;
                }
                if (i + 1 == size) {
                    i = -1;
                }
            }
            arrayTask.set(pointer, task);
            pointer = (pointer == size - 1) ? 0 : pointer + 1;
//            int printPointer = pointer;
//            if (pointer == 0) {printPointer = size;}
//            System.out.println("New task was added in buffer №" + printPointer + ", new pointer:" + (pointer + 1) + ";  " + task);
            occupied_size += 1;
            int word = task.getSourceNumber();
            plusCountPackage(word);
            return true;
        } else {
            return false;
        }
    }

    public Task outTask() {
        Task OutTask = this.arrayTask.get(outPointer);
        if (OutTask == null && !isEmpty()) {
            for (int i = outPointer; i < size; i++) {
                Task currTask = this.arrayTask.get(i);
                if (currTask != null) {
                    OutTask = currTask;
                    outPointer = i;
                    break;
                }
                if (i + 1 == size) {
                    i = -1;
                }
            }
        }
        return OutTask;
    }

    public void taskGoToDevice(){
        this.arrayTask.set(outPointer, null);
        occupied_size -= 1;
        outPointer = (outPointer == size - 1) ? 0 : outPointer + 1;
        //System.out.println("Указатель какая заявка на прибор пойдет:" + (outPointer + 1) + '\n');
    }

    private void plusCountPackage(int word) {
        int count = countPackages.get(word);
        countPackages.put(word, count + 1);
    }

    public Task denyTask(Task task) {
        Task knockOutTask = this.arrayTask.set(getOldestTask(), null);
        //System.out.println("\nTask is denied: " + knockOutTask);
        occupied_size -= 1;
        addTask(task);
        return knockOutTask;
    }

    public boolean isEmpty() {
        return occupied_size == 0;
    }

    @Override
    public String toString() {
        return "Buffer{" +
                "\narrayProducer:" + arrayTask +
                ",\nsize:" + size +
                ",\npointer:" + pointer +
                ",\noccupied_size:" + occupied_size +
                ",\ncountPackages:" + countPackages +
                '}';
    }

    public ArrayList<Task> getArrayProducer() {
        return arrayTask;
    }

    public void setArrayProducer(ArrayList<Task> arrayTask) {
        this.arrayTask = arrayTask;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    public int getOccupied_size() {
        return occupied_size;
    }

    public void setOccupied_size(int occupied_size) {
        this.occupied_size = occupied_size;
    }

    public void setCountPackages(Map<Integer, Integer> countPackages) {
        this.countPackages = countPackages;
    }

    public Map<Integer, Integer> getCountPackages() {
        return countPackages;
    }
}

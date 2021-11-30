package project;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

public class Table extends JFrame {
    ArrayList<Source> outSources = new ArrayList<>();
    ArrayList<Device> outDevices = new ArrayList<>();

    private Object[] columnsHeader1 = new String[]{"№ источника", "кол-во заявок", "Р отказа", "Т преб", "Т бп", "Т обсл", "Д бп", "Д обсл"};
    private Object[] columnsHeader2 = new String[]{"№ прибора", "коэфф использ"};
    private double outTime;

    public Table(ArrayList<Source> sources, ArrayList<Device> devices, double time) {
        super("Статистика");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.outSources = sources;
        this.outDevices = devices;
        this.outTime = time;
        Object[][] array1 = f1();
        Object[][] array2 = f2();

        JTable table1 = new JTable(array1, columnsHeader1);
        JTable table2 = new JTable(array2, columnsHeader2);

        Vector<Vector<String>> data = new Vector<Vector<String>>();

        Vector<String> header = new Vector<String>();

        for (Object o : columnsHeader1) {
            for (Object[] objects : array1) {
                header.add((String) o);
                Vector<String> row = new Vector<String>();
                for (Object object : objects) {
                    row.add((String) object);
                }
                data.add(row);
            }
        }

        for (Object o : columnsHeader2) {
            for (Object[] objects : array2) {
                header.add((String) o);
                Vector<String> row = new Vector<String>();
                for (Object object : objects) {
                    row.add((String) object);
                }
                data.add(row);
            }
        }
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));
        contents.add(new JScrollPane(table2));

        setContentPane(contents);
        setSize(750, 100 + 20 * (outDevices.size() + outSources.size()));
        setVisible(true);

    }

    public Object[][] f1() {
        int size = outSources.size();
        Object[][] mass = new String[size][columnsHeader1.length];
        for (int i = 0; i < size; i++) {
            mass[i][0] = Double.toString(outSources.get(i).getNumberOfSource());
            mass[i][1] = Double.toString(outSources.get(i).getCountOfTasks());
            mass[i][2] = Double.toString(makeShorter(outSources.get(i).getDenyProb()));
            mass[i][3] = Double.toString(makeShorter(outSources.get(i).getAverageTimeStaying()));
            mass[i][4] = Double.toString(makeShorter(outSources.get(i).getAverageTimeWaiting()));
            mass[i][5] = Double.toString(makeShorter(outSources.get(i).getAverageTimeUsing()));
            mass[i][6] = Double.toString(makeShorter(outSources.get(i).getTimeWaitingDispersion()));
            mass[i][7] = Double.toString(makeShorter(outSources.get(i).getTimeUsingDispersion()));
        }
        return mass;
    }

    public Object[][] f2() {
        int size = outDevices.size();
        Object[][] mass = new String[size][columnsHeader2.length];
        for (int i = 0; i < size; i++) {
            mass[i][0] = Double.toString(outDevices.get(i).getNumberOfDevice());
            mass[i][1] = Double.toString(makeShorter(outDevices.get(i).getBusyTime() / outTime));
        }
        return mass;
    }

    public double makeShorter(double num) {
        String formattedDouble = String.format("%.3f", num);
        return Double.parseDouble(formattedDouble.replace(",", "."));
    }
}

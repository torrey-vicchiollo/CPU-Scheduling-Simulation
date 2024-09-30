package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import scheduling.FCFS;
import scheduling.PS;
import scheduling.SJF;
import scheduling.SchedulingAlgorithm;

public class Driver {

    public static void main(String[] args) {
        Scanner scan;
        try {
            scan = new Scanner(new File("src/proc.txt"));
            String alg = scan.nextLine();
            ArrayList<PCB> allProcesses = new ArrayList<>();
            int id = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] params = line.split(",\\s*");
                String name = params[0];
                int arrivalTime = Integer.parseInt(params[1]);
                int cpuBurstTime = Integer.parseInt(params[2]);
                int priority = Integer.parseInt(params[3]);
                allProcesses.add(new PCB(name, id, arrivalTime, cpuBurstTime, priority));
                id++;
            }
            scan.close(); //close file

            SchedulingAlgorithm scheduler = null;
            switch (alg) {
                case "FCFS":
                    scheduler = new FCFS(allProcesses);
                    break;
                case "SJF":
                    scheduler = new SJF(allProcesses);
                    break;
                case "PS":
                    scheduler = new PS(allProcesses);
                    break;
                default:
                    System.err.println("Unsupported algorithm!");
            }
            scheduler.schedule();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

package scheduling;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.CPU;
import main.IODevice;
import main.PCB;

public abstract class SchedulingAlgorithm {

    //scheduling algorithm name
    protected String name;
    //intial list of processes
    protected List<PCB> allProcs;
    //ready queue of ready processes (CPU)
    protected List<PCB> readyQueue;
    //waiting queue of waiting processes (IO)
    protected List<PCB> waitingQueue;
    //list of finished processes
    protected List<PCB> finishedProcs;
    //current CPU process selected by scheduler
    protected PCB curCPUProcess;
    //current IO process
    protected PCB curIOProcess;
    //system time/simulation steps
    protected int systemTime;
    //simulation mode
    protected int simulationMode;
    //simulation unit
    protected int simulationUnit;
    //idle time
    protected int idleTime;
    //throughput
    protected double throughput;

    public SchedulingAlgorithm(String name, List<PCB> queue, int simulationMode, int simulationUnit) {
        this.name = name;
        this.allProcs = queue;
        this.readyQueue = new ArrayList<>();
        this.waitingQueue = new ArrayList<>();
        this.finishedProcs = new ArrayList<>();
        this.simulationMode = simulationMode;
        this.simulationUnit = simulationUnit;
        this.systemTime = 0;
    }

    public void schedule(int quantumTime, Scanner inputScanner) {
        double avgTAT = 0;
        double avgWT = 0;
        double avgRPT = 0;
        double throughput = 0;
        //while there are processes and the ready queue is not empty
        while (!allProcs.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty()) {
            //clear terminal
            System.out.print("\033\143");
            //print scheduling algorithm name
            System.out.println("SCHEDULING ALGORITHM >> " + name);
            //print simulation mode
            System.out.println("SIMULATION MODE      >> " + (simulationMode == 0 ? "AUTO" : "MANUAL"));
            //print simulation time
            System.out.println("SIMULATION UNIT (MS) >> " + simulationUnit);
            //print quantum
            System.out.println("QUANTUM (TIME SLICE) >> " + quantumTime);
            System.out.println();
            //print system time
            System.out.println("SYSTEM TIME: " + systemTime);
            //move arrived processes from allProcs to readyQueue (arrivalTime = systemTime)
            for (PCB proc : allProcs) {
                if (proc.getArrivalTime() <= systemTime) {
                    readyQueue.add(proc);
                    System.out.printf("%s has arrived!\n", proc.getName());
                }
            }
            //clear allProcs of any processes in the Ready Queue
            allProcs.removeAll(readyQueue);

            //cpu
            //if the ready queue is not empty
            if (!readyQueue.isEmpty()) {
                //choose next cpu process
                curCPUProcess = pickNextProcess(readyQueue);
                //update starttime of process
                if (curCPUProcess.getStartTime() < 0) {
                    curCPUProcess.setStartTime(systemTime);
                }
                //execute cpu for 1 unit time
                CPU.execute(curCPUProcess, 1);
            } else {
                idleTime++;
            }

            //io 
            //if the waiting queue is not empty
            if (!waitingQueue.isEmpty()) {
                //choose next io process
                curIOProcess = pickNextProcess(waitingQueue);
                IODevice.execute(curIOProcess, 1);
            }

            //print system
            print();

            //increase waiting time for all procs
            for (PCB proc : readyQueue) {
                if (proc != curCPUProcess) {
                    proc.increaseWaitingTime(1);
                }
            }
            for (PCB proc : waitingQueue) {
                if (proc != curIOProcess) {
                    proc.increaseWaitingTime(1);
                }
            }

            //increment systemTime
            systemTime++;

            //cpu
            //if the current CPU process' current burst is 0
            if (curCPUProcess != null) {
                if (curCPUProcess.getCurrentBurst() == 0) {
                    //remove the current process from the ready queue
                    readyQueue.remove(curCPUProcess);
                    //if the current CPU process has IO bursts remaining
                    if (curCPUProcess.getIOBurstIndex() != curCPUProcess.getIOBursts().size()) {
                        //incremement the current process' cpu burst index by 1
                        curCPUProcess.setCPUBurstIndex(curCPUProcess.getCPUBurstIndex() + 1);
                        //if the current cpu's process has remaining io bursts
                        if (!curCPUProcess.getIOBursts().isEmpty()) {
                            //set the current cpu's burst to the next io burst
                            curCPUProcess.setCurrentBurst(curCPUProcess.getIOBursts().get(curCPUProcess.getIOBurstIndex()));
                        }
                        //add the current process to the waiting queue
                        waitingQueue.add(curCPUProcess);
                        System.out.printf("%s has been moved to the Waiting Queue!\n", curCPUProcess.getName());
                    } else { //if the current CPU process doesn't have any IO bursts remaining
                        curCPUProcess.setFinishTime(systemTime);
                        finishedProcs.add(curCPUProcess);
                        System.out.printf("%s has finished!\n", curCPUProcess.getName());
                    }
                    curCPUProcess = null;
                }
            }

            //io
            //if the current IO process' current burst is 0
            if (curIOProcess != null) {
                if (curIOProcess.getCurrentBurst() == 0) {
                    //remove the current process from the ready queue
                    waitingQueue.remove(curIOProcess);
                    //incremement the current process' IO burst index by 1
                    curIOProcess.setIOBurstIndex(curIOProcess.getIOBurstIndex() + 1);
                    //set the current io process' current burst to the next CPU burst (process always ends in a CPU burst)
                    curIOProcess.setCurrentBurst(curIOProcess.getCPUBursts().get(curIOProcess.getCPUBurstIndex()));
                    //add the current process to the waiting queue
                    readyQueue.add(curIOProcess);
                    System.out.printf("%s has been moved to the Ready Queue!\n", curIOProcess.getName());
                    curIOProcess = null;
                }
            }

            //printing
            String curCPUProcessString = curCPUProcess != null ? ("CURRENT CPU PROCESS >> " + curCPUProcess.toString()) : "CURRENT CPU PROCESS >> IDLE";
            System.out.println(curCPUProcessString);
            String curIOProcessString = curIOProcess != null ? ("CURRENT IO PROCESS  >> " + curIOProcess.toString()) : "CURRENT IO PROCESS  >> IDLE";
            System.out.println(curIOProcessString);

            //print finished processes
            System.out.println("\n---FINISHED-PROCESSES---");
            for (PCB proc : finishedProcs) {
                System.out.println(proc.toString());
            }
            System.out.println("system time: " + systemTime);
            System.out.println("idle time: " + idleTime);

            //getting the cpu utilization
            System.out.println("CPU utilization " + getCPUUtilization(systemTime, idleTime) + "%");
            int totalTAT = 0;
            int totalWT = 0;
            int totalRPT = 0;
            for(PCB pcb: finishedProcs){
                totalTAT += pcb.getTurnAroundTime();
                totalWT += pcb.getWaitingTime();
                totalRPT += pcb.getResponseTime();
            }
            avgTAT = (double) totalTAT / finishedProcs.size();
            avgWT = (double) totalWT / finishedProcs.size();
            avgRPT = (double) totalRPT / finishedProcs.size();

            System.out.println("average turnaround time " + avgTAT);
            System.out.println("Average wait time " + avgWT);
            System.out.println("Average response time: " + avgRPT);
            // Calculate throughput
            throughput = (double) finishedProcs.size() / systemTime;
            // Print the throughput with two decimal places
            System.out.printf("Throughput: %.2f tasks per unit time\n", throughput);


            System.out.println("\n\n\n");

            //if auto mode
            if (simulationMode == 0) {
                try {
                    Thread.sleep(simulationUnit);
                } catch (InterruptedException e) {
                }
            } else {
                // if manual mode
                System.out.print("Continue? Y/N");
                String next = inputScanner.nextLine();
                if (next.contains("n") || next.contains("N")) {
                    break;
                }
            }
        }
        System.out.print("Save data?Y/N");
        String next = inputScanner.nextLine();
        if (next.contains("y") || next.contains("Y")) {
            System.out.print("Enter the path to save?");
            String path = inputScanner.nextLine();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                bw.write("Average TAT: " + avgTAT + "\n");
                bw.write("Average WT: " + avgWT + "\n");
                bw.write("Average RPT: " + avgRPT + "\n");
                bw.write("Throughput: " + throughput + "\n");
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finished!");
    }

    //Selects the next task using the appropriate scheduling algorithm
    public abstract PCB pickNextProcess(List<PCB> queue);

    //print simulation step
    public void print() {
        System.out.println("idle time:  " + idleTime);
        System.out.println("-CPU--  -READY-QUEUE-----------------");
        System.out.printf("| %s |<<  ", curCPUProcess == null ? "ID" : "P" + curCPUProcess.getId());
        for (PCB proc : readyQueue) {
            if (proc != curCPUProcess) {
                System.out.print("(P" + proc.getId() + ") ");
            }
        }
        System.out.println();
        System.out.println("------  -----------------------------");
        System.out.println("  \\/                             /\\  ");
        System.out.println("-WAITING-QUEUE---------------  -IO---");
        int x = 0;
        for (PCB proc : waitingQueue) {
            if (proc != curIOProcess) {
                System.out.print("(P" + proc.getId() + ") ");
                x += 5;
            }
        }
        for (int i = x; i < 28; i++) {
            System.out.print(" ");
        }
        System.out.printf(" >>| %s |\n", curIOProcess == null ? "ID" : "P" + curIOProcess.getId());
        System.out.println("-----------------------------  ------");

    }

    public double getCPUUtilization(int systemTime, int idleTime) {
        double utilization = ((systemTime - idleTime) / (double) systemTime) * 100;
        return Math.round(utilization * 100.0) / 100.0;
    }


}

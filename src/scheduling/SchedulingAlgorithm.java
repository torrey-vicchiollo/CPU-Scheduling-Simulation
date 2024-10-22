package scheduling;

import java.util.ArrayList;
import java.util.List;
import main.CPU;
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

    public SchedulingAlgorithm(String name, List<PCB> queue) {
        this.name = name;
        this.allProcs = queue;
        this.readyQueue = new ArrayList<>();
        this.waitingQueue = new ArrayList<>();
        this.finishedProcs = new ArrayList<>();
    }

    public void schedule() {
        //print scheduling algorithm name
        System.out.println("Scheduling Algorithm: " + name);
        //while there are processes and the ready queue is not empty
        while (!allProcs.isEmpty() || !readyQueue.isEmpty()) {
            //print system time
            System.out.println("System Time: " + systemTime);
            //move arrived processes from allProcs to readyQueue (arrivalTime = systemTime)
            for (PCB proc : allProcs) {
                if (proc.getArrivalTime() >= systemTime) {
                    readyQueue.add(proc);
                }
            }
            //clear allProcs
            allProcs.removeAll(readyQueue);
            //Choose the next process
            curCPUProcess = pickNextProcess();
            //print system
            print();
            //update starttime of process
            if (curCPUProcess.getStartTime() < 0) {
                curCPUProcess.setStartTime(systemTime);
            }
            //execute cpu for 1 unit time
            CPU.execute(curCPUProcess, 1);
            //increase waiting time for all procs
            for (PCB proc : readyQueue) {
                if (proc != curCPUProcess) {
                    proc.increaseWaitingTime(1);
                }
            }
            //increment systemTime
            systemTime++;

            //if the current CPU process' current burst is 0
            if (curCPUProcess.getCurrentBurst() == 0) {
                //remove the current process from the ready queue
                readyQueue.remove(curCPUProcess);
                //incremement the current process' cpu burst index by 1
                curCPUProcess.setCPUBurstIndex(curCPUProcess.getCPUBurstIndex() + 1);
                //set the current process' current burst to its next io burst
                curCPUProcess.setCurrentBurst(curCPUProcess.getIOBursts()[curCPUProcess.getIOBurstIndex()]);
                //add the current process to the waiting queue
                waitingQueue.add(curCPUProcess);
                /*
                System.out.printf("Process %s terminated at %d, startTime = %d, TAT = %d, WT = %d\n",
                        curProcess.getName(), systemTime, curProcess.getStartTime(), curProcess.getTurnAroundTime(),
                        curProcess.getWaitingTime());
                 */
            }
            //print new line
            System.out.println();
        }
    }

    //Selects the next task using the appropriate scheduling algorithm
    public abstract PCB pickNextProcess();

    //print simulation step
    public void print() {
        System.out.println("-CPU--  -Ready-Queue-----------------");
        System.out.printf("| %s |  < ", curCPUProcess == null ? "ID" : "P" + curCPUProcess.getId());
        for (PCB proc : readyQueue) {
            if (proc != curCPUProcess) {
                System.out.print("(P" + proc.getId() + ") ");
            }
        }
        System.out.println();
        System.out.println("------  -----------------------------");
        System.out.println("  VV                             ^^  ");
        System.out.println("-Waiting-Queue---------------  -IO---");
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
        System.out.printf(">  | %s |\n", curIOProcess == null ? "ID" : "P" + curIOProcess.getId());
        System.out.println("-----------------------------  ------");
        System.out.println("\n\n\n");
        /*
        for (PCB proc : readyQueue) {
            System.out.println(proc);
        }
         */
    }
}

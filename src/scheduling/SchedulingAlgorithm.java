package scheduling;

import java.util.ArrayList;
import java.util.List;
import main.CPU;
import main.PCB;

public abstract class SchedulingAlgorithm {

    protected String name;		      //scheduling algorithm name
    protected List<PCB> allProcs;		//the initial list of processes
    protected List<PCB> readyQueue;	//ready queue of ready processes
    protected List<PCB> finishedProcs;	//list of terminated processes
    protected PCB curProcess; //current selected process by the scheduler
    protected int systemTime; //system time or simulation time steps

    public SchedulingAlgorithm(String name, List<PCB> queue) {
        this.name = name;
        this.allProcs = queue;
        this.readyQueue = new ArrayList<>();
        this.finishedProcs = new ArrayList<>();
    }

    public void schedule() {
        System.out.println("Scheduling Algorithm: " + name);
        while (!allProcs.isEmpty() || !readyQueue.isEmpty()) {
            System.out.println("System Time: " + systemTime);
            //Move arrived processes from allProcs to readyQueue (arrivalTime = systemTime)
            for (PCB proc : allProcs) {
                if (proc.getArrivalTime() >= systemTime) {
                    readyQueue.add(proc);
                }
            }
            allProcs.removeAll(readyQueue);
            //Choose the next process
            curProcess = pickNextProcess();
            print();

            // update starttime of process
            if (curProcess.getStartTime() < 0) {
                curProcess.setStartTime(systemTime);
            }
            //execute cpu for 1 unit time
            CPU.execute(curProcess, 1);

            //increase waiting time for all procs
            for (PCB proc : readyQueue) {
                if (proc != curProcess) {
                    proc.increaseWaitingTime(1);
                }
            }
            // increment systemTime
            systemTime++;

            // check if remaining cpu bursts of curprocess = 0
            if (curProcess.getCpuBurst() == 0) {
                curProcess.setFinishTime(systemTime);
                readyQueue.remove(curProcess);
                finishedProcs.add(curProcess);
                System.out.printf("Process %s terminated at %d, startTime = %d, TAT = %d, WT = %d\n",
                        curProcess.getName(), systemTime, curProcess.getStartTime(), curProcess.getTurnAroundTime(),
                        curProcess.getWaitingTime());
            }
            System.out.println();
        }
    }

    //Selects the next task using the appropriate scheduling algorithm
    public abstract PCB pickNextProcess();

    //print simulation step
    public void print() {
        System.out.println("----------");
        System.out.printf("|CPU: %s |\n", curProcess == null ? "idle" : curProcess.getName());
        System.out.println("----------");
        for (PCB proc : readyQueue) {
            System.out.println(proc);
        }
    }
}

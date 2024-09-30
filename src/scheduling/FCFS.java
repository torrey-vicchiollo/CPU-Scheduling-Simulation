package scheduling;

import java.util.List;
import main.PCB;

public class FCFS extends SchedulingAlgorithm {

    public FCFS(List<PCB> queue) {
        super("FCFS", queue);
    }

    public PCB pickNextProcess() {
        return readyQueue.get(0); //choose first process in ready queue
    }
}

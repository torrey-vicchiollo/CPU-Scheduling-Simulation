package scheduling;

import java.util.List;
import main.PCB;

public class FCFS extends SchedulingAlgorithm {

    public FCFS(List<PCB> queue) {
        super("FCFS", queue);
    }

    @Override
    public PCB pickNextProcess(List<PCB> queue) {
        return queue.get(0); //choose first process in ready queue
    }
}

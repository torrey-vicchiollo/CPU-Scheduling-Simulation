package scheduling;

import java.util.Collections;
import java.util.List;
import main.PCB;

public class SJF extends SchedulingAlgorithm {

    public SJF(List<PCB> queue) {
        super("SJF", queue);
    }

    @Override
    public PCB pickNextProcess(List<PCB> queue) {
        // Collections.sort(readyQueue, Comparator.comparingInt(PCB::getCpuBurst));
        Collections.sort(queue, (pcb1, pcb2) -> (pcb1.getCurrentBurst() - pcb2.getCurrentBurst()));
        return queue.get(0);
    }
}

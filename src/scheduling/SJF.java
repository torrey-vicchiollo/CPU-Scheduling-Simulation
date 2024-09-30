package scheduling;

import java.util.Collections;
import java.util.List;
import main.PCB;

public class SJF extends SchedulingAlgorithm {

    public SJF(List<PCB> queue) {
        super("SJF", queue);
    }

    public PCB pickNextProcess() {
        // Collections.sort(readyQueue, Comparator.comparingInt(PCB::getCpuBurst));
        Collections.sort(readyQueue, (pcb1, pcb2) -> (pcb1.getCpuBurst() - pcb2.getCpuBurst()));
        return readyQueue.get(0);
    }
}

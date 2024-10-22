package scheduling;

import java.util.Collections;
import java.util.List;
import main.PCB;

public class RR extends SchedulingAlgorithm {

    public RR(List<PCB> queue) {
        super("RR", queue);
    }

    public PCB pickNextProcess() {
        Collections.sort(readyQueue, (pcb1, pcb2) -> (pcb1.getPriority() - pcb2.getPriority()));
        return readyQueue.get(0);
    }
}

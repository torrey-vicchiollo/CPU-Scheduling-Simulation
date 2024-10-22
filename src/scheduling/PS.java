package scheduling;

import java.util.Collections;
import java.util.List;
import main.PCB;

public class PS extends SchedulingAlgorithm {

    public PS(List<PCB> queue) {
        super("PS", queue);
    }

    @Override
    public PCB pickNextProcess() {
        Collections.sort(readyQueue, (pcb1, pcb2) -> (pcb1.getPriority() - pcb2.getPriority()));
        return readyQueue.get(0);
    }
}

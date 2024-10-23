package scheduling;

import java.util.Collections;
import java.util.List;
import main.PCB;

public class PS extends SchedulingAlgorithm {

    public PS(List<PCB> queue) {
        super("PS", queue);
    }

    @Override
    public PCB pickNextProcess(List<PCB> queue ) {
        Collections.sort(queue, (pcb1, pcb2) -> (pcb1.getPriority() - pcb2.getPriority()));
        return queue.get(0);
    }
}

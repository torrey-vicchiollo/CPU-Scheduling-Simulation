package scheduling;

import java.util.Collections;
import java.util.List;
import main.PCB;

public class RR extends SchedulingAlgorithm {

    public RR(List<PCB> queue) {
        super("RR", queue);
    }

    @Override
    public PCB pickNextProcess(List<PCB> queue) {
        //get the process at 0 and from there check if its counter is 0 if not we return the next item in the queue and put the 
        //value with the tally counter back to the quantum time and behind the other processes
        PCB item = queue.get(0);
        if(item.getQuantumTimeTrack() == 0){
            //reset the count
            item.setQuantumTimeTrack(item.getQuantumTime());
            //send this process to the back maybe not needed
            queue.add(queue.remove(0));

            //start working on the next process (using recursion because its cool)
            return pickNextProcess(queue);
            //decrement its count
            //send it
        }
        else{
            item.setQuantumTimeTrack(item.getQuantumTimeTrack() - 1);
            return item;
        }
    

    }
}

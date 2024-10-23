package main;

public class CPU {

    public static void execute(PCB process, int cpuBurst) {
        process.setCurrentBurst(process.getCurrentBurst() - cpuBurst);
        
    }
}

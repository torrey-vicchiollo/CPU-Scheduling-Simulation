package main;

public class IODevice {

    public static void execute(PCB process, int ioBurst) {
        process.setCurrentBurst(process.getCurrentBurst() - ioBurst);
    }
}

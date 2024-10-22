package main;

public class PCB {

    //process name
    private String name;
    //process id
    private int id;
    //arrival time of the process
    private int arrivalTime;
    //process priority
    private int priority;
    //cpu burst array & index
    private Integer[] cpuBursts;
    private int cpuBurstIndex;
    //io burst array & index
    private Integer[] ioBursts;
    private int ioBurstIndex;
    //burst mode (either "CPU" or "IO")
    private String burstMode;
    //current burst
    private int currentBurst;
    //the stats of the process execution
    private int startTime, finishTime, turnaroundTime, waitingTime;

    //constructor
    public PCB(String name, int id, int arrivalTime, int priority, Integer[] cpuBursts, Integer[] ioBursts) {
        super();
        this.name = name;
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.cpuBursts = cpuBursts;
        this.cpuBurstIndex = 0;
        this.ioBursts = ioBursts;
        this.ioBurstIndex = 0;
        this.burstMode = "CPU";
        this.startTime = -1;
        this.finishTime = -1;
        this.currentBurst = cpuBursts[0];
    }

    //getters & setters
    public String getBurstMode() {
        return burstMode;
    }

    public void setBurstMode(String burstMode) {
        this.burstMode = burstMode;
    }

    public int getCPUBurstIndex() {
        return cpuBurstIndex;
    }

    public void setCPUBurstIndex(int index) {
        this.cpuBurstIndex = index;
    }

    public int getIOBurstIndex() {
        return ioBurstIndex;
    }

    public void setIOBurstIndex(int index) {
        this.ioBurstIndex = index;
    }

    public int getCurrentBurst() {
        return currentBurst;
    }

    public void setCurrentBurst(int currentBurst) {
        this.currentBurst = currentBurst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer[] getCPUBursts() {
        return cpuBursts;
    }

    public void setCPUBursts(Integer[] cpuBursts) {
        this.cpuBursts = cpuBursts;
    }

    public Integer[] getIOBursts() {
        return ioBursts;
    }

    public void setIOBursts(Integer[] ioBursts) {
        this.ioBursts = ioBursts;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
        this.turnaroundTime = finishTime - arrivalTime;
    }

    public int getTurnAroundTime() {
        return this.turnaroundTime;
    }

    public int getWaitingTime() {
        return this.waitingTime;
    }

    public void increaseWaitingTime(int burst) {
        // Increase the waitingTime variable with burst.
        this.waitingTime += burst;
    }

    @Override
    public String toString() {
        return "PCB [name=" + name + ", id=" + id + ", arrivalTime=" + arrivalTime + ", currentBurst=" + currentBurst
                + ", priority=" + priority + "]";
    }

}

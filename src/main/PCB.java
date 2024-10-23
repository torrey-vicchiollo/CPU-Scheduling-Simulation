package main;

import java.util.List;

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
    private List<Integer> cpuBursts;
    private int cpuBurstIndex;
    //io burst array & index
    private List<Integer> ioBursts;
    private int ioBurstIndex;
    //current burst
    private int currentBurst;
    //the stats of the process execution
    private int startTime, finishTime, turnaroundTime, waitingTime;

    //constructor
    public PCB(String name, int id, int arrivalTime, int priority, List<Integer> cpuBursts, List<Integer> ioBursts) {
        super();
        this.name = name;
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.cpuBursts = cpuBursts;
        this.cpuBurstIndex = 0;
        this.ioBursts = ioBursts;
        this.ioBurstIndex = 0;
        this.startTime = -1;
        this.finishTime = -1;
        this.currentBurst = cpuBursts.get(0);
    }

    //getters & setters
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

    public List<Integer> getCPUBursts() {
        return cpuBursts;
    }

    public void setCPUBursts(List<Integer> cpuBursts) {
        this.cpuBursts = cpuBursts;
    }

    public List<Integer> getIOBursts() {
        return ioBursts;
    }

    public void setIOBursts(List<Integer> ioBursts) {
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
        return "P" + id + " {Name=" + name + ", Arrival Time=" + arrivalTime + ", Current Burst=" + currentBurst + ", Priority=" + priority + "}";
    }

}

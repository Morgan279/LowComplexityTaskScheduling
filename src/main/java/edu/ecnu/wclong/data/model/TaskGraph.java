package edu.ecnu.wclong.data.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskGraph {

    private final int[][] communicationCostTable;

    private final int[][] computationCostTable;

    private final double[] upwardRank;

    private final double[] downwardRank;

    private final List<Set<Integer>> successors;

    private final List<Set<Integer>> predecessors;

    private final int TASK_NUM;

    private final int PROCESSOR_NUM;

    public TaskGraph(int taskNum, int processorNum) {
        this.TASK_NUM = taskNum;
        this.PROCESSOR_NUM = processorNum;
        this.communicationCostTable = new int[taskNum + 1][taskNum + 1];
        this.computationCostTable = new int[taskNum + 1][processorNum + 1];
        this.upwardRank = new double[taskNum + 1];
        this.downwardRank = new double[taskNum + 1];
        this.successors = new ArrayList<>(taskNum);
        this.predecessors = new ArrayList<>(taskNum);
        for (int i = 0; i <= taskNum; ++i) {
            successors.add(new HashSet<>());
            predecessors.add(new HashSet<>());
            communicationCostTable[i][i] = 0;
        }
    }

    public void setCommunicationCost(int start, int end, int cost) {
        communicationCostTable[start][end] = cost;
        successors.get(start).add(end);
        predecessors.get(end).add(start);
    }

    public int getCommunicationCost(int start, int end) {
        return communicationCostTable[start][end];
    }

    public void setComputationCost(int start, int end, int cost) {
        computationCostTable[start][end] = cost;
    }

    public int getComputationCost(int start, int end) {
        return computationCostTable[start][end];
    }

    public double getUpwardRank(int task) {
        return Double.parseDouble(String.format("%.3f", upwardRank[task]));
    }

    public double getDownwardRank(int task) {
        return Double.parseDouble(String.format("%.3f", downwardRank[task]));
    }

    public Set<Integer> getPredecessors(int task) {
        return predecessors.get(task);
    }

    public Set<Integer> getSuccessors(int task) {
        return successors.get(task);
    }

    public void calculateUpwardRank() {
        final int exitTask = TASK_NUM;
        upwardRank[exitTask] = getMeanComputationCost(exitTask);
        for (int i = exitTask - 1; i > 0; --i) {
            upwardRank[i] = getMeanComputationCost(i) + getMaxSuccessorWeight(i);
        }
    }

    public void calculateDownwardRank() {
        final int entryTask = 1;
        downwardRank[entryTask] = 0;
        for (int i = entryTask + 1; i <= TASK_NUM; ++i) {
            downwardRank[i] = getMaxPredecessorWeight(i);
        }
    }

    private double getMeanComputationCost(int task) {
        double totalComputationCost = 0;

        for (int i = 1; i <= PROCESSOR_NUM; ++i) {
            totalComputationCost += computationCostTable[task][i];
        }

        return totalComputationCost / PROCESSOR_NUM;
    }

    private double getMaxSuccessorWeight(int task) {
        double res = -1;
        for (int successor : successors.get(task)) {
            double weight = communicationCostTable[task][successor] + upwardRank[successor];
            res = Math.max(res, weight);
        }
        return res;
    }

    private double getMaxPredecessorWeight(int task) {
        double res = -1;

        for (int predecessor : predecessors.get(task)) {
            double weight = downwardRank[predecessor] + getMeanComputationCost(predecessor) + getCommunicationCost(predecessor, task);
            res = Math.max(res, weight);
        }

        return res;
    }
}

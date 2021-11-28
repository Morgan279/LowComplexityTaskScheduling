package edu.ecnu.wclong.algorithm.impl;

import edu.ecnu.wclong.data.model.Schedule;
import edu.ecnu.wclong.data.model.TaskGraph;
import edu.ecnu.wclong.entity.Processor;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class CPOP extends PELCTaskScheduler {

    private final Set<Integer> critical;

    private final Set<Integer> scheduledTaskSet;

    private Processor criticalProcessor;

    public CPOP(int taskNum, int processorNum) {
        this.critical = new HashSet<>();
        this.scheduledTaskSet = new HashSet<>(taskNum);
        this.init(taskNum, processorNum);
    }

    @Override
    public List<Schedule> schedule(TaskGraph taskGraph) {
        this.taskGraph = taskGraph;
        scheduledList.clear();
        for (int i = 0; i < PROCESSOR_NUM; ++i) {
            processors[i].reset();
        }

        PriorityQueue<Integer> priorityQueue = taskPrioritizing();

        scheduledTaskSet.clear();
        while (!priorityQueue.isEmpty()) {
            int task = priorityQueue.poll();
            Schedule schedule = new Schedule(task);
            Processor processor = processorSelection(schedule);
            processor.execute(schedule);
            scheduledList.add(schedule);
            scheduledTaskSet.add(schedule.task);
            for (int successor : taskGraph.getSuccessors(schedule.task)) {
                if (scheduledTaskSet.containsAll(taskGraph.getPredecessors(successor))) {
                    priorityQueue.add(successor);
                }
            }
        }

        return scheduledList;
    }

    @Override
    protected PriorityQueue<Integer> taskPrioritizing() {
        taskGraph.calculateUpwardRank();
        taskGraph.calculateDownwardRank();

        this.initCritical();
        this.initCriticalProcessor();

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((t1, t2) -> Double.compare(getPriority(t2), getPriority(t1)));
        priorityQueue.add(1);

        return priorityQueue;
    }

    @Override
    protected Processor processorSelection(Schedule schedule) {
        if (critical.contains(schedule.task)) {
            schedule.AST = getEST(schedule.task, criticalProcessor);
            schedule.AFT = schedule.AST + taskGraph.getComputationCost(schedule.task, criticalProcessor.getId());
            return criticalProcessor;
        }

        return getMinEFTProcessor(schedule);
    }

    private void initCritical() {
        critical.clear();
        int cur = 1;
        double CP = getPriority(cur);
        critical.add(cur);
        while (cur != TASK_NUM) {
            for (int successor : taskGraph.getSuccessors(cur)) {
                if (getPriority(successor) == CP) {
                    critical.add(successor);
                    cur = successor;
                    break;
                }
            }
        }
    }

    private void initCriticalProcessor() {
        double minCost = Double.MAX_VALUE;

        for (Processor processor : processors) {

            double sum = 0;
            for (int task : critical) {
                sum += taskGraph.getComputationCost(task, processor.getId());
            }
            double avgComputationCost = sum / PROCESSOR_NUM;

            if (avgComputationCost < minCost) {
                minCost = avgComputationCost;
                criticalProcessor = processor;
            }

        }
    }

    private double getPriority(int task) {
        return taskGraph.getUpwardRank(task) + taskGraph.getDownwardRank(task);
    }
}

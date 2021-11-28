package edu.ecnu.wclong.algorithm.impl;

import edu.ecnu.wclong.algorithm.TaskScheduler;
import edu.ecnu.wclong.data.model.Schedule;
import edu.ecnu.wclong.data.model.TaskGraph;
import edu.ecnu.wclong.entity.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class PELCTaskScheduler implements TaskScheduler {

    protected int TASK_NUM;

    protected int PROCESSOR_NUM;

    protected Processor[] processors;

    protected List<Schedule> scheduledList;

    protected TaskGraph taskGraph;

    @Override
    public List<Schedule> schedule(TaskGraph taskGraph) {
        this.taskGraph = taskGraph;
        scheduledList.clear();
        for (int i = 0; i < PROCESSOR_NUM; ++i) {
            processors[i].reset();
        }

        PriorityQueue<Integer> priorityQueue = taskPrioritizing();

        while (!priorityQueue.isEmpty()) {
            int task = priorityQueue.poll();
            Schedule schedule = new Schedule(task);
            Processor processor = processorSelection(schedule);
            processor.execute(schedule);
            scheduledList.add(schedule);
        }

        return scheduledList;
    }

    protected void init(int taskNum, int processorNum) {
        this.TASK_NUM = taskNum;
        this.PROCESSOR_NUM = processorNum;
        this.processors = new Processor[processorNum];
        this.scheduledList = new ArrayList<>(TASK_NUM);
        for (int i = 0; i < processorNum; ++i) {
            processors[i] = new Processor(i + 1);
        }
    }

    protected Processor getMinEFTProcessor(Schedule schedule) {
        Processor minEFTProcessor = null;
        schedule.AFT = Integer.MAX_VALUE;

        for (Processor processor : processors) {
            int EST = getEST(schedule.task, processor);
            int EFT = EST + taskGraph.getComputationCost(schedule.task, processor.getId());
            if (EFT < schedule.AFT) {
                schedule.AST = EST;
                schedule.AFT = EFT;
                minEFTProcessor = processor;
            }
        }

        return minEFTProcessor;
    }

    protected int getEST(int task, Processor processor) {
        if (task == 1) {
            return 0;
        }

        int readyTime = getReadyTime(task, processor);
        return Math.max(processor.getAvailableTime(taskGraph.getComputationCost(task, processor.getId()), readyTime), readyTime);
    }

    protected int getReadyTime(int task, Processor processor) {
        int readyTime = -1;
        Set<Integer> predecessors = taskGraph.getPredecessors(task);

        for (Schedule schedule : scheduledList) {
            if (predecessors.contains(schedule.task)) {
                int communicationCost = schedule.processor == processor.getId() ? 0 : taskGraph.getCommunicationCost(schedule.task, task);
                readyTime = Math.max(readyTime, schedule.AFT + communicationCost);
            }
        }

        return readyTime;
    }

    protected abstract PriorityQueue<Integer> taskPrioritizing();

    protected abstract Processor processorSelection(Schedule schedule);
}

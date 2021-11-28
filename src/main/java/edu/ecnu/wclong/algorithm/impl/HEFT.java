package edu.ecnu.wclong.algorithm.impl;

import edu.ecnu.wclong.data.model.Schedule;
import edu.ecnu.wclong.entity.Processor;

import java.util.PriorityQueue;

public class HEFT extends PELCTaskScheduler {


    public HEFT(int taskNum, int processorNum) {
        this.init(taskNum, processorNum);
    }


    @Override
    protected PriorityQueue<Integer> taskPrioritizing() {
        taskGraph.calculateUpwardRank();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((t1, t2) -> Double.compare(taskGraph.getUpwardRank(t2), taskGraph.getUpwardRank(t1)));

        for (int i = 1; i <= TASK_NUM; ++i) {
            priorityQueue.add(i);
        }

        return priorityQueue;
    }

    @Override
    protected Processor processorSelection(Schedule schedule) {
        return getMinEFTProcessor(schedule);
    }

}

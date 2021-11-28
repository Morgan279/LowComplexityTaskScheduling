package edu.ecnu.wclong.util;

import edu.ecnu.wclong.data.model.TaskGraph;

public class TestDataGenerator {

    public static final int TASK_NUM = 10;

    public static final int PROCESSOR_NUM = 3;

    public static TaskGraph generateTestTaskGraph() {
        TaskGraph taskGraph = new TaskGraph(TASK_NUM, PROCESSOR_NUM);
        generateTestComputationCost(taskGraph);
        generateTestCommunicationCost(taskGraph);
        return taskGraph;
    }

    private static void generateTestComputationCost(TaskGraph taskGraph) {
        int[][] computationCost = {
                {14, 16, 9},
                {13, 19, 18},
                {11, 13, 19},
                {13, 8, 17},
                {12, 13, 10},
                {13, 16, 9},
                {7, 15, 11},
                {5, 11, 14},
                {18, 12, 20},
                {21, 7, 16}
        };
        for (int i = 0; i < TASK_NUM; ++i) {
            for (int j = 0; j < PROCESSOR_NUM; ++j) {
                taskGraph.setComputationCost(i + 1, j + 1, computationCost[i][j]);
            }
        }

    }

    private static void generateTestCommunicationCost(TaskGraph taskGraph) {
        taskGraph.setCommunicationCost(1, 2, 18);
        taskGraph.setCommunicationCost(1, 3, 12);
        taskGraph.setCommunicationCost(1, 4, 9);
        taskGraph.setCommunicationCost(1, 5, 11);
        taskGraph.setCommunicationCost(1, 6, 14);

        taskGraph.setCommunicationCost(2, 8, 19);
        taskGraph.setCommunicationCost(2, 9, 16);

        taskGraph.setCommunicationCost(3, 7, 23);

        taskGraph.setCommunicationCost(4, 8, 27);
        taskGraph.setCommunicationCost(4, 9, 23);

        taskGraph.setCommunicationCost(5, 9, 13);

        taskGraph.setCommunicationCost(6, 8, 15);

        taskGraph.setCommunicationCost(7, 10, 17);

        taskGraph.setCommunicationCost(8, 10, 11);

        taskGraph.setCommunicationCost(9, 10, 13);
    }

}

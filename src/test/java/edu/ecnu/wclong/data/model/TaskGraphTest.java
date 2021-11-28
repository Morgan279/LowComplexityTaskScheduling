package edu.ecnu.wclong.data.model;

import edu.ecnu.wclong.util.TestDataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskGraphTest {

    @Test
    public void testGetUpwardRank() {
        double[] upwardRanks = {108.0, 77.0, 80.0, 80.0, 69.0, 63.333, 42.667, 35.667, 44.333, 14.667};
        TaskGraph taskGraph = TestDataGenerator.generateTestTaskGraph();
        taskGraph.calculateUpwardRank();
        for (int i = 1; i <= TestDataGenerator.TASK_NUM; ++i) {
            Assertions.assertEquals(upwardRanks[i - 1], taskGraph.getUpwardRank(i));
        }
    }

    @Test
    public void testGetDownwardRank() {
        double[] downwardRanks = {0.0, 31.0, 25.0, 22.0, 24.0, 27.0, 62.333, 66.667, 63.667, 93.333};
        TaskGraph taskGraph = TestDataGenerator.generateTestTaskGraph();
        taskGraph.calculateDownwardRank();
        for (int i = 1; i <= TestDataGenerator.TASK_NUM; ++i) {
            Assertions.assertEquals(downwardRanks[i - 1], taskGraph.getDownwardRank(i));
        }
    }

}

package edu.ecnu.wclong.algorithm;

import edu.ecnu.wclong.data.model.Schedule;
import edu.ecnu.wclong.data.model.TaskGraph;

import java.util.List;

public interface TaskScheduler {

    List<Schedule> schedule(TaskGraph taskGraph);

}

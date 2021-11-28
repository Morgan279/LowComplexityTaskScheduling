package edu.ecnu.wclong.entity;

import edu.ecnu.wclong.data.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class Processor {

    private final int id;

    private final List<Schedule> schedules;

    public Processor(int id) {
        this.id = id;
        this.schedules = new ArrayList<>();
    }

    public void execute(Schedule schedule) {
        schedule.processor = this.id;
        schedules.add(schedule);
    }

    public void reset() {
        schedules.clear();
    }

    public int getAvailableTime(int computationCost, int readyTime) {
        if (schedules.isEmpty()) {
            return 0;
        }

        Schedule curSchedule = schedules.get(0);
        for (int i = 1; i < schedules.size(); ++i) {
            Schedule preSchedule = schedules.get(i - 1);
            curSchedule = schedules.get(i);
            if (curSchedule.AST - preSchedule.AFT >= computationCost && readyTime >= preSchedule.AFT) {
                return readyTime;
            }
        }

        return curSchedule.AFT;
    }

    public int getId() {
        return this.id;
    }

}

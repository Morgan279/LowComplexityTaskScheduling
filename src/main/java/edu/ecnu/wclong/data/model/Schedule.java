package edu.ecnu.wclong.data.model;

public class Schedule {

    public int task;

    public int processor;

    public int AST;

    public int AFT;

    public Schedule(int task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "T" + task + "=>P" + processor + ":[" + AST + ", " + AFT + "]";
    }
}

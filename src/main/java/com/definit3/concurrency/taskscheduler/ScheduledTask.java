package com.definit3.concurrency.taskscheduler;

import java.util.concurrent.TimeUnit;

public class ScheduledTask {
    private String taskId;
    private Long executionTime;
    private TaskType taskType;
    private Long scheduledDelay;

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    private Runnable runnable;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Long getScheduledDelay() {
        return scheduledDelay;
    }

    public void setScheduledDelay(Long scheduledDelay) {
        this.scheduledDelay = scheduledDelay;
    }

    public ScheduledTask(Runnable runnable, Long executionTime, String taskId, TaskType taskType, Long scheduledDelay) {
        this.runnable = runnable;
        this.executionTime = executionTime;
        this.taskId = taskId;
        this.taskType = taskType;
        this.scheduledDelay = scheduledDelay;
    }
}

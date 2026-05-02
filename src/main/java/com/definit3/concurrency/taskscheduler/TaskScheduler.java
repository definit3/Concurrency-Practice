package com.definit3.concurrency.taskscheduler;

public interface TaskScheduler extends Runnable {
    public void submitTaskWithFixedRate(Long initialDelay, Long scheduledDelay, Runnable runnable);

    public void submitTaskWithFixedDelay(Long initialDelay, Long scheduledDelay, Runnable runnable);

    public void submitOneTimeTask(Long initialDelay, Runnable runnable);
}

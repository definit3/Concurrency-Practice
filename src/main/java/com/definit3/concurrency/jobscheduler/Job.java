package com.definit3.concurrency.jobscheduler;

import java.util.List;

public class Job {
    private int jobId;
    private List<Job> dependencies;
    private List<Job> dependents;
    private final Runnable task;

    public Job(int jobId, List<Job> dependencies, List<Job> dependents, Runnable task) {
        this.jobId = jobId;
        this.dependencies = dependencies;
        this.dependents = dependents;
        this.task = task;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public List<Job> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Job> dependencies) {
        this.dependencies = dependencies;
    }

    public List<Job> getDependents() {
        return dependents;
    }

    public void setDependents(List<Job> dependents) {
        this.dependents = dependents;
    }

    public void execute() {
        task.run();
    }
}

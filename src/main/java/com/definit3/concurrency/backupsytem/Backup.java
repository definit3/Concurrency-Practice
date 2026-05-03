package com.definit3.concurrency.backupsytem;

import java.util.ArrayList;
import java.util.List;

public class Backup {
    private String id;
    private Long endTime;
    private Long retention;
    private List<Backup> dependencies;
    private List<Backup> dependents;

    public Backup(String id, Long endTime, Long retention) {
        this.id = id;
        this.endTime = endTime;
        this.retention = retention;
        this.dependencies = new ArrayList<>();
        this.dependents = new ArrayList<>();
    }

    public boolean isExpired(final Long now) {
        return endTime + retention <= now;
    }

    public Long getRetention() {
        return retention;
    }

    public void setRetention(Long retention) {
        this.retention = retention;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Backup> getDependents() {
        return dependents;
    }

    public void setDependents(List<Backup> dependents) {
        this.dependents = dependents;
    }

    public List<Backup> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Backup> dependencies) {
        this.dependencies = dependencies;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

package com.definit3.concurrency.backupsytem;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class BackupManager {
    private final HashMap<String, Backup> backupMap = new HashMap<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public void registerBackup(String id, Long endTime, Long retention) {
        try {
            writeLock.lock();
            backupMap.put(id, new Backup(id, endTime, retention));
        } finally {
            writeLock.unlock();
        }
    }

    public void addDependency(final String from, final String to) {
        try {
            writeLock.lock();
            final Backup fromBackup = backupMap.get(from);
            final Backup toBackup = backupMap.get(to);
            if (fromBackup == null || toBackup == null) throw new IllegalArgumentException("Backup not found");
            fromBackup.getDependencies().add(toBackup);
            toBackup.getDependents().add(fromBackup);
        } finally {
            writeLock.unlock();
        }
    }

    public List<String> findRecoveryChain(final String nodeId) {
        try {
            readLock.lock();
            final Backup node = backupMap.get(nodeId);
            Set<String> visited = new HashSet<>();
            List<String> chain = new ArrayList<>();
            dfs(node, chain, visited);
            return chain;
        } finally {
            readLock.unlock();
        }
    }

    private void dfs(final Backup node, final List<String> chain, final Set<String> visited) {
        if (node == null || visited.contains(node.getId())) {
            return;
        }
        visited.add(node.getId());
        for (final Backup dependency: node.getDependencies()) {
                dfs(dependency, chain, visited);
        }
        chain.add(node.getId());
    }

    public void expireBackup() {
        try {
            writeLock.lock();
            final long now = System.currentTimeMillis();
            final Set<String> toDelete = new HashSet<>();
            final HashMap<String, Boolean> dp = new HashMap<>();
            for (final Backup node : backupMap.values()) {
                if (canDelete(node, now, dp)) {
                    toDelete.add(node.getId());
                }
            }
            for (final String id : toDelete) {
                removeNode(backupMap.get(id));
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void removeNode(final Backup node) {
        for (final Backup dependencies: node.getDependencies()) {
            dependencies.getDependents().remove(node);
        }
        backupMap.remove(node.getId());
    }

    private boolean canDelete(final Backup node, final Long now, final HashMap<String, Boolean> dp) {
        if (dp.containsKey(node.getId())) {
            return dp.get(node.getId());
        }
        if (!node.isExpired(now)) {
            dp.put(node.getId(), false);
            return false;
        }
        for (final Backup dependents: node.getDependents()) {
            if (!canDelete(dependents, now, dp)) {
                dp.put(node.getId(), false);
                return false;
            }
        }
        dp.put(node.getId(), true);
        return true;
    }

}

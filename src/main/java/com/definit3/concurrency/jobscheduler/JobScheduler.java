package com.definit3.concurrency.jobscheduler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JobScheduler {
    private final Map<Integer, Job> jobMap = new HashMap<>();
    private final Map<Integer, Integer> degree = new HashMap<>();
    private int remainingJobs = 0;
    private final Queue<Integer> readyQueue = new ArrayDeque<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public JobScheduler(final List<Job> jobs) {
        for (final Job job: jobs) {
            jobMap.put(job.getJobId(), job);
            degree.put(job.getJobId(), job.getDependencies().size());
            if (job.getDependencies().isEmpty()) {
                readyQueue.offer(job.getJobId());
            }
        }
        remainingJobs = jobs.size();
    }

    public Runnable worker() {
        return () -> {
            Job job = null;
            while (true) {
                try {
                    lock.lock();
                    while (readyQueue.isEmpty() && remainingJobs > 0) {
                        condition.await();
                    }
                    if (remainingJobs == 0) {
                        break;
                    }
                    job = jobMap.get(readyQueue.poll());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
                job.execute();
                try {
                    lock.lock();
                    for (final Job dependent : job.getDependents()) {
                        degree.put(dependent.getJobId(), degree.get(dependent.getJobId()) - 1);
                        if (degree.get(dependent.getJobId()) == 0) {
                            readyQueue.offer(dependent.getJobId());
                            condition.signalAll();
                        }
                    }
                    remainingJobs--;
                    if (remainingJobs == 0) {
                        condition.signalAll();
                    }
                } finally {
                    lock.unlock();
                }
            }
        };
    }

    private static void log(String msg) {
        System.out.printf("[%s] %s%n", Thread.currentThread().getName(), msg);
    }

    // DAG:  1 ──> 3 ──┐
    //        \          ├──> 5
    //         2 ──> 4 ──┘
    public static void main(String[] args) throws InterruptedException {
        Job job1 = new Job(1, new ArrayList<>(), new ArrayList<>(), () -> {
            log("Job 1 RUNNING");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            log("Job 1 DONE");
        });
        Job job2 = new Job(2, new ArrayList<>(), new ArrayList<>(), () -> {
            log("Job 2 RUNNING");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            log("Job 2 DONE");
        });
        Job job3 = new Job(3, new ArrayList<>(List.of(job1)), new ArrayList<>(), () -> {
            log("Job 3 RUNNING  (needs 1)");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            log("Job 3 DONE");
        });
        Job job4 = new Job(4, new ArrayList<>(List.of(job1, job2)), new ArrayList<>(), () -> {
            log("Job 4 RUNNING  (needs 1, 2)");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            log("Job 4 DONE");
        });
        Job job5 = new Job(5, new ArrayList<>(List.of(job3, job4)), new ArrayList<>(), () -> {
            log("Job 5 RUNNING  (needs 3, 4)");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            log("Job 5 DONE");
        });

        job1.getDependents().addAll(List.of(job3, job4));
        job2.getDependents().add(job4);
        job3.getDependents().add(job5);
        job4.getDependents().add(job5);

        JobScheduler scheduler = new JobScheduler(List.of(job1, job2, job3, job4, job5));

        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(scheduler.worker());
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("All jobs completed");
    }
}

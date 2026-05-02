package com.definit3.concurrency.politicalbathroom;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;

public class Solution {
    final int bathroomCapacity = 3;
    int currentOccupied = 0;
    int sameOccupiedCounter = 0;
    int waitingRepublicanCount = 0;
    int waitingDemocratCount = 0;
    Party occupiedBy;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    private boolean canOccupy(Party party) {
        if (occupiedBy == null) {
            return true;
        }
        if (party.equals(occupiedBy)) {
            int oppositePartyWaitingCounter = party == Party.REPUBLICAN ? waitingDemocratCount : waitingRepublicanCount;
            boolean maxOccupyCondition = !(sameOccupiedCounter >= 5 && oppositePartyWaitingCounter > 0);
            boolean samePartyCondition = currentOccupied < bathroomCapacity;
            return maxOccupyCondition && samePartyCondition;
        } else {
            return currentOccupied == 0;
        }
    }

    private String state() {
        return "[occupied=" + currentOccupied + " occupiedBy=" + occupiedBy
                + " sameCounter=" + sameOccupiedCounter
                + " repWaiting=" + waitingRepublicanCount
                + " demWaiting=" + waitingDemocratCount + "]";
    }

    void useBathroom(final String name, final Party party) {
        try {
            lock.lock();
            while (!canOccupy(party)) {
                if (party.equals(Party.REPUBLICAN)) waitingRepublicanCount++;
                else waitingDemocratCount++;

                System.out.println(Thread.currentThread().getName()
                        + " | WAITING  | " + name + " (" + party + ") " + state());

                condition.await();

                if (party.equals(Party.REPUBLICAN)) waitingRepublicanCount--;
                else waitingDemocratCount--;
            }

            if (occupiedBy == party || occupiedBy == null) sameOccupiedCounter++;
            else sameOccupiedCounter = 1;
            occupiedBy = party;
            currentOccupied++;

            System.out.println(Thread.currentThread().getName()
                    + " | ENTER    | " + name + " (" + party + ") " + state());

            lock.unlock();
            try {
                Thread.sleep(name.length() * 200L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.lock();
            }

            currentOccupied--;
            System.out.println(Thread.currentThread().getName()
                    + " | EXIT     | " + name + " (" + party + ") " + state());
            condition.signalAll();

        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1);

        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivek", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivek", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivek", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivek", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("demovivekraj2", Party.DEMOCRAT);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("demovivekraj3", Party.DEMOCRAT);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivekraj", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivekraj1", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("vivekraj2", Party.REPUBLICAN);});
        executorService.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            solution.useBathroom("demovivek3", Party.DEMOCRAT);});
        latch.countDown();
    }
}

enum Party {
    REPUBLICAN,
    DEMOCRAT;
}

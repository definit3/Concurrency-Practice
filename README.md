# Concurrency Practice

Practicing multithreading and concurrency concepts from scratch. Covers thread lifecycle, synchronization, locks, classical problems, and the java.util.concurrent utilities.

## Topics Practiced

### Thread Basics
- Creating threads by extending `Thread`
- Thread naming and priorities (`MIN_PRIORITY`, `MAX_PRIORITY`)
- `sleep`, `yield`, `interrupt`
- Daemon threads

### Synchronization
- Race condition demo with a shared `Counter`
- Fixing race conditions using `synchronized` blocks
- Using `join` to wait for threads to finish

### CompletableFuture
- `supplyAsync` for async non-blocking tasks
- `thenApply` for chaining transformations
- `orTimeout` and `exceptionally` for timeout handling and fallback
- `allOf` and `join` to wait on multiple futures

### CyclicBarrier
- Synchronizing multiple threads at a common barrier point
- Barrier action (runnable triggered when all threads arrive), `await`, `reset`
- Difference from `CountDownLatch` — reusable and threads wait for each other

### CountDownLatch
- Waiting for multiple services to finish using `Future.get` vs `CountDownLatch`
- `countDown`, `await` with timeout

### Executor Framework
- Raw threads vs `ExecutorService` — performance comparison with thread pools
- `Executors.newFixedThreadPool`, `newSingleThreadExecutor`, `newScheduledThreadPool`
- `Future` — `get`, `isDone`, `cancel`, `isCancelled`, timeout with `get(timeout, unit)`
- `Callable` vs `Runnable` with futures
- `invokeAll` with timeout, `invokeAny`
- `ScheduledExecutorService` — `schedule`, `scheduleAtFixedRate`, `scheduleWithFixedDelay`

### Thread Communication
- Producer-Consumer using `wait` and `notify` on a shared resource
- Guarded blocks to coordinate between threads without busy waiting

### Explicit Locks (`java.util.concurrent.locks`)
- `ReentrantLock` — basic usage, reentrancy with nested method calls
- `lock()`, `tryLock()` with timeout, `lockInterruptibly()`, `unlock()`
- Bank account withdrawal demo showing `tryLock` with timeout to avoid indefinite blocking
- Fair vs unfair lock — `ReentrantLock(true)` for FIFO ordering vs default unfair mode
- `ReentrantReadWriteLock` — concurrent reads with exclusive write locks

### File Read/Write with Offset (Rubrik-style problem)
- Simulates a file abstraction with `read(offset, length)` and `write(offset, insert)` operations
- File content backed by `StringBuilder` for efficient mutable operations
- Thread-safe using `ReentrantReadWriteLock` — multiple concurrent readers, exclusive writer
- `lock()` called before `try` block with `unlock()` in `finally` to prevent `IllegalMonitorStateException`
- Debug logging per thread showing lock acquisition, release, before/after state on writes

### Concurrent Data Structure (Searcher / Inserter / Deleter problem)
- Classic three-thread-type problem: searchers, inserters, deleters on a shared list
- Searchers run fully concurrently with each other and with inserters (`readLock`)
- Inserters are mutually exclusive with each other (`ReentrantLock` mutex) but allow parallel searches
- Deleters are fully exclusive — block all searches and inserts (`writeLock`)
- Lock design: `readLock` shared by search + insert; `writeLock` exclusive to delete; `mutex` serialises inserts
- Debug logging per thread: lock acquire/release, operation type (APPEND, INSERT, DELETED), list size
- `DataStructureMain` tests all thread types concurrently using `ExecutorService` + `CountDownLatch` start gate

### Political Bathroom Problem
- Single bathroom shared by Democrats and Republicans with a capacity of 3
- No mixed occupancy allowed — bathroom must be pure Democrat or pure Republican at all times
- `ReentrantLock` + `Condition` for mutual exclusion and waiting
- `canOccupy(party)` encapsulates all entry logic: same-party capacity check, opposite-party empty-bathroom check
- Anti-starvation: `sameOccupiedCounter` tracks consecutive same-party entries; capped at 5 when opposite party is waiting
- `waitingRepublicanCount` / `waitingDemocratCount` track actual waiters — cap only enforced when opposite party has real waiters, preventing indefinite blocking when opposite party is absent
- Sleep (bathroom use) happens outside the lock; try-finally guarantees lock re-acquisition before cleanup
- Debug logging shows WAITING / ENTER / EXIT with full state: occupied count, current party, consecutive counter, both waiting counts

### Multi-Threaded Task Scheduler
- Supports one-time (ad-hoc), fixed-rate, and fixed-delay recurring task scheduling
- Three-layer architecture: user thread → scheduler thread → worker thread pool
- `PriorityQueue<ScheduledTask>` (min-heap by `nextExecutionTime`) as the core data structure
- `ReentrantLock` + `Condition` for thread-safe queue access and timed waiting (`condition.await(delay)`)
- `condition.signalAll()` on enqueue wakes the scheduler thread immediately for earlier-than-current-head tasks
- **FIXED_RATE**: next run = last scheduled time + period (clock-based, independent of task duration)
- **FIXED_DELAY**: next run = task finish time + delay (re-enqueued by worker thread after completion)
- Debug logging includes `HH:mm:ss.SSS` timestamp + thread name at every lifecycle event: ENQUEUE, DISPATCH, EXECUTING, DONE, RE-ENQUEUE

## Project Structure

```
src/main/java/com/definit3/concurrency/
├── basic/                    # Thread creation, lifecycle, priorities, daemon
├── synchronization/          # Shared state, race conditions, synchronized blocks
├── completablefuture/        # supplyAsync, thenApply, allOf, orTimeout, exceptionally
├── cyclicbarrier/            # CyclicBarrier, barrier action, reset
├── countdownlatch/           # CountDownLatch, await, countDown
├── executorframework/        # ExecutorService, Future, Callable, ScheduledExecutor
├── threadcommunication/      # wait, notify, producer-consumer
├── filereadwrite/            # Thread-safe file read/write with offset using ReentrantReadWriteLock
├── concurrentdatastructure/  # Searcher/Inserter/Deleter with ReadWriteLock + mutex
├── taskscheduler/            # Multi-threaded task scheduler: one-time, fixed-rate, fixed-delay
├── politicalbathroom/        # Political bathroom problem: categorical exclusion with anti-starvation
└── explicit/
    ├── lock/                 # ReentrantLock, tryLock, lockInterruptibly
    ├── lockfairness/         # Fair vs unfair lock ordering
    └── readwritelock/        # ReentrantReadWriteLock, concurrent reads with exclusive writes
```

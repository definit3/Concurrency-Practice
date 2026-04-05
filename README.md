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

## Project Structure

```
src/main/java/com/definit3/concurrency/
├── basic/              # Thread creation, lifecycle, priorities, daemon
├── synchronization/    # Shared state, race conditions, synchronized blocks
├── cyclicbarrier/       # CyclicBarrier, barrier action, reset
├── countdownlatch/      # CountDownLatch, await, countDown
├── executorframework/   # ExecutorService, Future, Callable, ScheduledExecutor
├── threadcommunication/ # wait, notify, producer-consumer
└── explicit/
    ├── lock/           # ReentrantLock, tryLock, lockInterruptibly
    ├── lockfairness/   # Fair vs unfair lock ordering
    └── readwritelock/  # ReentrantReadWriteLock, concurrent reads with exclusive writes
```

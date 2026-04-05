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
└── explicit/
    ├── lock/           # ReentrantLock, tryLock, lockInterruptibly
    ├── lockfairness/   # Fair vs unfair lock ordering
    └── readwritelock/  # ReentrantReadWriteLock, concurrent reads with exclusive writes
```

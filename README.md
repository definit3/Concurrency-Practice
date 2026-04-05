# Concurrency Practice

Practicing multithreading and concurrency concepts from scratch. Covers thread lifecycle, synchronization, locks, classical problems, and the java.util.concurrent utilities.

## Topics Practiced

### Thread Basics
- Creating threads by extending `Thread`
- Thread naming and priorities (`MIN_PRIORITY`, `MAX_PRIORITY`)
- `sleep`, `yield`, `interrupt`
- Daemon threads

### Synchronization _(in progress)_
- Race condition demo with a shared `Counter`

## Project Structure

```
src/main/java/com/definit3/concurrency/
├── basic/              # Thread creation, lifecycle, priorities, daemon
└── synchronization/    # Shared state, race conditions
```

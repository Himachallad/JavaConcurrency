# Job Scheduler

### Requirements

- Users should be able to schedule adhoc event with initial delay
- Users should be able to schedule a recurring event with fixed rate
- Users should be able to schedule a reccuring event with delay rate
- Users should be able to configure number of worker threads for the end tasks

### Non functional Requirements
- Scale to million jobs a day
- Flexibility: System should support multiple jobs at a time
- Async
- Concurrent
- Multithreading


## Proposed solution

### Entities
- Client
- Job
- JobExecutor
- SchedulerService
- ScheduledTask
- Task Type

### Design pattern
- Singleton for Scheduler service
- Strategy for queuing strategy
- KISS
- SOLID
- DRY

### Data structure
- Priority Queue
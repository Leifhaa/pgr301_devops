package com.example.pgr301_devops.service

import com.example.pgr301_devops.data.Task
import com.example.pgr301_devops.data.TaskState
import com.example.pgr301_devops.dto.DtoConverter
import com.example.pgr301_devops.dto.TaskDto
import com.example.pgr301_devops.repository.TaskRepository
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service
import org.tsdes.advanced.rest.dto.PageDto
import java.util.concurrent.atomic.AtomicInteger

@Service
class TaskService (
        private val repository: TaskRepository,
        private val meterRegistry: MeterRegistry
)
{
    //val tmpFinished: AtomicInteger? = meterRegistry.gauge("tasks.current", AtomicInteger(0))
    final val openTasks: AtomicInteger = AtomicInteger()
    final val runningTasks: AtomicInteger = AtomicInteger()
    final val finishedTasks: AtomicInteger = AtomicInteger()

    init{
        Gauge.builder("tasks.current", openTasks::get)
                .tag("state", TaskState.Open.name)
                .description("How many tasks is currently open")
                .register(meterRegistry)

        Gauge.builder("tasks.current", runningTasks::get)
                .tag("state", TaskState.Running.name)
                .description("How many tasks is currently running")
                .register(meterRegistry)

        /*
        * Using a counter instead of gauge for finished tasks as it's only increasing. Gauge should not be used on something which we can use a counter on, but rather when there is a 'natural upper bound'
        *  */
        Counter.builder("tasks.current")
                .tag("state", TaskState.Completed.name)
                .description("Number of tasks finished")
                .register(meterRegistry)
    }

    fun create(dto: TaskDto){
        val entity = Task(title = dto.title!!, description = dto.description!!, user = dto.user)
        openTasks.incrementAndGet()
        repository.save(entity)
    }

    fun exists(id: Long) : Boolean{
        return repository.existsById(id)
    }

    fun delete(id: Long) {
        val task = repository.findById(id)
        if (task.get().state == TaskState.Open){
            openTasks.decrementAndGet()
        }
        repository.deleteById(id)
    }

    fun updateState(id: Long, state: TaskState){
        val task = repository.findById(id)
        if (task.get().state == state){
            openTasks.decrementAndGet()
        }
    }

    fun get(keysetId: Long?) : PageDto<TaskDto> {
        val page = PageDto<TaskDto>()

        //Page size
        val n = 5;
        val tasks = DtoConverter.transform(repository.getNextPage(n, keysetId))
        page.list = tasks

        if (tasks.size == n){
            val last = tasks.last()
            page.next = "/api/tasks?keysetId=${last.id}"
        }
        return page
    }


    //Todo: Implement database calls counter
}
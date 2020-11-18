package com.example.pgr301_devops.service

import com.example.pgr301_devops.data.Task
import com.example.pgr301_devops.data.TaskState
import com.example.pgr301_devops.dto.DtoConverter
import com.example.pgr301_devops.dto.TaskDto
import com.example.pgr301_devops.metrics.TaskDistributionSummary
import com.example.pgr301_devops.repository.TaskRepository
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service
import org.tsdes.advanced.rest.dto.PageDto
import java.util.concurrent.atomic.AtomicInteger


@Service
class TaskService (
        private val repository: TaskRepository,
        private val meterRegistry: MeterRegistry,
        private val distributionSummary: TaskDistributionSummary
)
{

    final val openTasks: AtomicInteger = AtomicInteger()
    final val runningTasks: AtomicInteger = AtomicInteger()

    init{
        Gauge.builder("tasks.current", openTasks::get)
                .tag("state", TaskState.Open.name)
                .description("How many tasks is currently open")
                .register(meterRegistry)

        Gauge.builder("tasks.current", runningTasks::get)
                .tag("state", TaskState.Running.name)
                .description("How many tasks is currently running")
                .register(meterRegistry)
    }

    fun create(dto: TaskDto){
        val entity = Task(title = dto.title!!, description = dto.description!!, user = dto.user)
        openTasks.incrementAndGet()
        //Add to rate of opened/completed tasks
        distributionSummary.StateDistributionSummary(meterRegistry).record(0.0)
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

    fun updateState(id: Long, dto: TaskDto){
        val task = repository.findById(id)
        if (dto.state == TaskState.Open && task.get().state == dto.state){
            openTasks.decrementAndGet()
        }
        else if (dto.state == TaskState.Completed){
            meterRegistry.counter("tasks.current", "state", TaskState.Completed.name).increment();
            //Add to rate of opened/completed tasks
            distributionSummary.StateDistributionSummary(meterRegistry).record(1.0)
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
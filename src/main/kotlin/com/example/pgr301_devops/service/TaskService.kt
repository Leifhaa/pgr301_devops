package com.example.pgr301_devops.service

import com.example.pgr301_devops.model.Task
import com.example.pgr301_devops.model.TaskState
import com.example.pgr301_devops.dto.DtoConverter
import com.example.pgr301_devops.dto.TaskDto
import com.example.pgr301_devops.metrics.TaskDistributionSummary
import com.example.pgr301_devops.repository.TaskRepository
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.tsdes.advanced.rest.dto.PageDto
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.lang.instrument.Instrumentation;


@Service
@Transactional
class TaskService (
        private val repository: TaskRepository,
        private val meterRegistry: MeterRegistry,
        private val distributionSummary: TaskDistributionSummary)
{

    /**
     * How many tasks is currently running
     */
    final val running: AtomicInteger = AtomicInteger()

    init{
        Gauge.builder("tasks.current", running::get)
                .tag("state", TaskState.Running.name)
                .description("How many tasks is currently running")
                .register(meterRegistry)
    }

    fun create(dto: TaskDto) : Task{
        val task = Task(title = dto.title!!, description = dto.description!!, user = dto.user, creationTime = ZonedDateTime.now())
        repository.save(task)
        return task
    }

    fun exists(id: Long) : Boolean{
        return repository.existsById(id)
    }

    fun findById(id: Long) : Optional<Task> {
        return repository.findById(id)
    }

    fun delete(id: Long) {
        val task = repository.findById(id)
        if (task.isPresent){
            repository.deleteById(id)
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

    @Async
    fun runTask(task: Task) {
        running.incrementAndGet()

        //Simulate a computation
        val randTime = (5..15).random()
        Thread.sleep( randTime.toLong() * 10000)
        task.state = TaskState.Completed

        //Calculate the price for computation of the task (our business is charging per millisecond running a task)
        val price = randTime * 0.0015
        task.price = price

        //Save the money made into metrics
        distributionSummary.StateDistributionSummary(meterRegistry).record(price)


        repository.save(task)

        running.decrementAndGet()
    }
}
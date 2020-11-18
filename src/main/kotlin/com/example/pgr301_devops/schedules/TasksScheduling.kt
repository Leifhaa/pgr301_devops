package com.example.pgr301_devops.schedules

import com.example.pgr301_devops.data.TaskState
import com.example.pgr301_devops.repository.TaskRepository
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.ZonedDateTime


@Configuration
@EnableScheduling
class TasksScheduling(
        private val repository: TaskRepository
){
    @Scheduled(fixedDelay = 20000)
    fun scanOldTasks() {
        /* Scans for tasks is old and should be deleted*/
        val thres = ZonedDateTime.now().minusMinutes(1)
        repository.deleteAllByCreationTimeBeforeAndState(thres, TaskState.Completed)
    }
}
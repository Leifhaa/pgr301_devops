package com.example.pgr301_devops.schedules

import com.example.pgr301_devops.model.TaskState
import com.example.pgr301_devops.repository.TaskRepository
import io.micrometer.core.annotation.Timed
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.ZonedDateTime


@Configuration
@EnableScheduling
class TasksScheduling(
        private val repository: TaskRepository
){

    /**
     * Removes tasks which is old & completed.
     */
    @Timed(value = "tasks.old.removal", longTask = true)
    @Scheduled(fixedDelay = 60000)
    fun removeOldCompleted() {
        /*
        * LongTask allows us to measure time while the task is running unlike regular timer where it's measured afterwards.
        * This is beneficial for tasks which takes long to execute.
        *
        * This method would usually execute instantly however I'd like to simulate the method as more complex and time consuming by simply
        * adding a sleep. As the method now takes longer to execute, it also makes sense to have a longTask timer to see the duration
        * of this method.
        *  */
        val rnds = (5..55).random()
        Thread.sleep(rnds.toLong() * 10000)
        val thres = ZonedDateTime.now().minusMinutes(1)
        repository.deleteAllByCreationTimeBeforeAndState(thres, TaskState.Completed)
    }
}
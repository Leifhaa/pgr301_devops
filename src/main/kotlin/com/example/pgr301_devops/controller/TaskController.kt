package com.example.pgr301_devops.controller

import com.example.pgr301_devops.dto.TaskDto
import com.example.pgr301_devops.model.TaskState
import com.example.pgr301_devops.service.TaskService
import io.micrometer.core.annotation.Timed
import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse
import java.net.URI
import kotlin.math.log


@Api(value="/api/tasks", description = "Handling incoming requests for tasks")
@RequestMapping(path = ["/api/tasks"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@RestController
class TaskController(
        private val service: TaskService,
        private val meterRegistry: MeterRegistry
)
{
    val uri = "/api/tasks"
    val metName = "http.requests"
    var logger: Logger = LoggerFactory.getLogger(TaskController::class.java)


    @Bean
    fun timedAspect(registry: MeterRegistry): TimedAspect? {
        return TimedAspect(registry)
    }

    @Timed(description= "Time spent resolving http request", value = "http.requests.timer")
    @ApiOperation("Retrieves all tasks")
    @GetMapping
    fun getAll(
            @RequestParam("keysetId", required = false)
            keysetId: Long?
    ): ResponseEntity<WrappedResponse<PageDto<TaskDto>>>{
        val page = service.get(keysetId)
        return RestResponseFactory.payload(200, page)
    }

    @Timed(description= "Time spent resolving http request", value = "http.requests.timer")
    @ApiOperation("Create a new task")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun run(
            @ApiParam("Data for new task")
            @RequestBody
            dto: TaskDto
    ): ResponseEntity<WrappedResponse<Void>> {
        meterRegistry.counter(metName, "uri", uri, "method", HttpMethod.POST.toString()).increment();
        if (dto.id != null) {
            return RestResponseFactory.userFailure("Cannot specify an id when creating a new task")
        }

        val dbTask = service.create(dto)
        logger.info("A new task was created!")
        return RestResponseFactory.created(URI.create("${uri}/${dbTask.id}"))
    }

    @Timed(description= "Time spent resolving http request", value = "http.requests.timer")
    @ApiOperation("Executes a task")
    @PostMapping(path = ["/{id}/run"])
    fun run(
            @ApiParam("The id of the task")
            @PathVariable("id")
            taskId: String
    ): ResponseEntity<WrappedResponse<Void>> {
        meterRegistry.counter(metName, "uri", uri, "method", HttpMethod.POST.toString()).increment();
        val id: Long
        try {
            id = taskId.toLong()
        } catch (e: Exception) {
            return RestResponseFactory.userFailure("Invalid id")
        }

        val optional = service.findById(id);
        if (optional.isEmpty){
            return RestResponseFactory.notFound("Task not found")
        }
        val task = optional.get()
        if (task.state == TaskState.Running){
            return RestResponseFactory.userFailure("Task already running")
        }
        if (task.state == TaskState.Completed){
            return RestResponseFactory.userFailure("Task is completed")
        }

        logger.info("Running task :" + id)
        service.runTask(task)

        return RestResponseFactory.noPayload(200)
    }

    @Timed(description= "Time spent resolving http request", value = "http.requests.timer")
    @ApiOperation("Delete a specific task, by id")
    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
            @ApiParam("The id of the task")
            @PathVariable("id")
            pathId: String
    ): ResponseEntity<WrappedResponse<Void>> {
        meterRegistry.counter(metName, "uri", uri, "method", HttpMethod.DELETE.toString()).increment();
        val id: Long
        try {
            id = pathId.toLong()
        } catch (e: Exception) {
            return RestResponseFactory.userFailure("Invalid id")
        }

        if (!service.exists(id)) {
            return RestResponseFactory.notFound("Task with id $id does not exist")
        }

        service.delete(id)
        return RestResponseFactory.noPayload(204)
    }

}
package com.example.pgr301_devops.controller

import com.example.pgr301_devops.data.Task
import com.example.pgr301_devops.dto.DtoConverter
import com.example.pgr301_devops.dto.TaskDto
import com.example.pgr301_devops.repository.TaskRepository
import io.micrometer.core.instrument.MeterRegistry
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse
import java.net.URI


@Api(value="/api/tasks", description = "Handling incoming requests for tasks")
@RequestMapping(path = ["/api/tasks"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@RestController
class TaskController(
        private val repository: TaskRepository,
        private val meterRegistry: MeterRegistry
) {
    @ApiOperation("Retrieves all tasks")
    @GetMapping
    fun getAll(
            @RequestParam("keysetId", required = false)
            keysetId: Long?
    ): ResponseEntity<WrappedResponse<PageDto<TaskDto>>>{
        val page = PageDto<TaskDto>()

        //Page size
        val n = 5;
        val tasks = DtoConverter.transform(repository.getNextPage(n, keysetId))
        page.list = tasks

        if (tasks.size == n){
            val last = tasks.last()
            page.next = "/api/tasks?keysetId=${last.id}"
        }
        return RestResponseFactory.payload(200, page)
    }

    @ApiOperation("Create a new task")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun create(
            @ApiParam("Data for new task")
            @RequestBody
            dto: TaskDto
    ): ResponseEntity<WrappedResponse<Void>> {

        if (dto.id != null) {
            return RestResponseFactory.userFailure("Cannot specify an id when creating a new task")
        }

        //service.priveledge.findByName
        val entity = Task(title = dto.title!!, description = dto.description!!)
        repository.save(entity)
        meterRegistry.counter("controller.", "task", "123").increment();
        return RestResponseFactory.created(URI.create("/api/tasks/" + entity.id))
    }

}
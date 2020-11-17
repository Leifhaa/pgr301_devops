package com.example.pgr301_devops.controller

import com.example.pgr301_devops.data.Task
import com.example.pgr301_devops.dto.DtoConverter
import com.example.pgr301_devops.dto.TaskDto
import com.example.pgr301_devops.repository.TaskRepository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.apache.coyote.Response
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.tsdes.advanced.rest.dto.PageDto
import org.tsdes.advanced.rest.dto.RestResponseFactory
import org.tsdes.advanced.rest.dto.WrappedResponse

@Api(value="/api/tasks", description = "Handling incoming requests for tasks")
@RequestMapping(path = ["/api/tasks"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
@RestController
class TaskController(
        val repository: TaskRepository
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
}
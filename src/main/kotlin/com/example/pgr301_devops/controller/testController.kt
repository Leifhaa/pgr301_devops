package com.example.pgr301_devops.controller

import com.example.pgr301_devops.data.Task
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class testController {
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)]) //@RequestMapping(value="/",method=RequestMethod.GET)
    fun get(): ResponseEntity<Task> {
        val dto = Task(0, "foo", "Bar");
        return ResponseEntity.ok(dto)
    }
}
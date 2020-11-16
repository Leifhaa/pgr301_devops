package com.example.pgr301_devops.controller

import com.example.pgr301_devops.dto.TestDto
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class testController {
    @GetMapping(produces = [(MediaType.APPLICATION_JSON_VALUE)]) //@RequestMapping(value="/",method=RequestMethod.GET)
    fun get(): ResponseEntity<TestDto> {
        val dto = TestDto(id="foo");
        return ResponseEntity.ok(dto)
    }
}
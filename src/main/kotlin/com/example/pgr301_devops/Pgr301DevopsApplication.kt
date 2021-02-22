package com.example.pgr301_devops

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync // IMPORTANT: otherwise @Async will not work
@SpringBootApplication
class Pgr301DevopsApplication

fun main(args: Array<String>) {
	runApplication<Pgr301DevopsApplication>(*args)
}

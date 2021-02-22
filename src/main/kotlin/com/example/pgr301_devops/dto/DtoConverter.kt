package com.example.pgr301_devops.dto

import com.example.pgr301_devops.model.Task

object DtoConverter {


    fun transform(task: Task) : TaskDto {

        return TaskDto(
                id = task.id,
                title = task.title,
                description = task.description,
                user = task.user,
                state = task.state,
                price = task.price,
                creationTime = task.creationTime
        )
    }


    fun transform(books: Iterable<Task>) : List<TaskDto>{
        return books.map { transform(it) }
    }
}
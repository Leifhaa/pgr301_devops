package com.example.pgr301_devops.dto

import com.example.pgr301_devops.data.TaskState
import com.sun.istack.NotNull
import java.time.ZonedDateTime
import javax.persistence.GeneratedValue
import javax.persistence.Id

class TaskDto(
        @get:Id
        var id: Long? = null,

        @get:NotNull
        var title: String? = null,

        @get:NotNull
        var description: String? = null,

        @get:NotNull
        var user: Long? = null,

        @get:NotNull
        var state: TaskState? = null,

        @get:NotNull
        var creationTime: ZonedDateTime? = null

)
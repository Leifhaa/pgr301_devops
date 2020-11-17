package com.example.pgr301_devops.dto

import com.sun.istack.NotNull
import javax.persistence.GeneratedValue
import javax.persistence.Id

class TaskDto(
        @get:Id
        var id: Long? = null,

        @get:NotNull
        var title: String? = null,

        @get:NotNull
        var description: String? = null
)
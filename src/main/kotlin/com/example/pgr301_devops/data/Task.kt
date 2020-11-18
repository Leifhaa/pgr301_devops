package com.example.pgr301_devops.data

import org.springframework.boot.autoconfigure.domain.EntityScan
import java.time.ZonedDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Task(
        @get:Id @get:GeneratedValue
        var id: Long? = null,

        @get:NotBlank @get:Size(max = 64)
        var title: String? = null,

        @get:NotBlank @get:Size(max = 256)
        var description: String? = null,

        @get:NotNull
        var user: Long? = null

)
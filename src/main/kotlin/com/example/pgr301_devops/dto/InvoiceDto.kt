package com.example.pgr301_devops.dto

import com.example.pgr301_devops.model.TaskState
import com.sun.istack.NotNull
import java.time.ZonedDateTime
import javax.persistence.Id


/**
 * A data transfer object regarding invoicing of a user
 */
class InvoiceDto(
        @get:NotNull
        var user: Long? = null,

        @get:NotNull
        var price: Double? = null,

        @get:NotNull
        var elapsed: Int? = null

)
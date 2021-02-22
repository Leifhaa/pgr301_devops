package com.example.pgr301_devops.repository

import com.example.pgr301_devops.model.Task
import javax.transaction.Transactional

@Transactional
interface KeysetPageRepo {
    //Getting new page using keyset pagination
    fun getNextPage(size: Int, keysetId: Long?) : List<Task>
}
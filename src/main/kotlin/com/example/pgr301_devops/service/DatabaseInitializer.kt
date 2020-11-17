package com.example.pgr301_devops.service

import com.example.pgr301_devops.data.Task
import com.example.pgr301_devops.repository.KeysetPageRepoImpl
import com.example.pgr301_devops.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class DatabaseInitializer(
        @Autowired val repository: TaskRepository
){

    @PostConstruct
    fun initialize(){
        repository.run {
            deleteAll()
            save(Task(0, "Process data", "Process data from incoming request"))
            save(Task(1, "Write data", "Write data from incoming request"))
            save(Task(2, "Write data", "Write data from incoming request"))
        }
    }
}
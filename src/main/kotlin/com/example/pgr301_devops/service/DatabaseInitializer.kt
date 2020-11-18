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
            save(Task(0, "Process data", "Process data from incoming request", 0))
            save(Task(1, "Write data", "Write data from incoming request", 0))
            save(Task(2, "Write data", "Write data from incoming request", 0))
            save(Task(3, "Notify customer", "Notify customer about bad sales month", 0))
            save(Task(4, "Send invoices", "Sends invoices", 0))
            save(Task(5, "Retrieve customer information", "Retrieve all customer information from all customers", 1))
            save(Task(6, "Run sales report", "Prints a sales report for a team", 1))
        }
    }
}
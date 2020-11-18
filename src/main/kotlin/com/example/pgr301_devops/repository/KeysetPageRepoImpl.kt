package com.example.pgr301_devops.repository

import com.example.pgr301_devops.data.Task
import com.example.pgr301_devops.data.TaskState
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException
import java.time.ZonedDateTime
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.transaction.Transactional

@Repository
interface TaskRepository: CrudRepository<Task, Long>, KeysetPageRepo{
    fun deleteAllByCreationTimeBeforeAndState(time: ZonedDateTime, state: TaskState)
}

@Transactional
@Repository
class KeysetPageRepoImpl(
        val em: EntityManager
) : KeysetPageRepo{

    //Imporant takes:
    //1. The ordering MUST be deterministic and strict (usually primary key is included in the ordering)
    //2. WHERE statement MUST include all columns in the 'order by'.
    override fun getNextPage(size: Int, keysetId: Long?): List<Task> {
        if (size < 1 || size > 1000){
            throw IllegalArgumentException("Invalid size value: $size")
        }


        val query: TypedQuery<Task>
        if (keysetId == null){
            //Retrieving first page
            query = em.createQuery("select t from Task t order by t.id DESC", Task::class.java)
        }
        else{
            //Retrieving next page
            query = em.createQuery("select t from Task t where t.id<?1 order by t.id DESC", Task::class.java)
            query.setParameter(1, keysetId)
        }
        //Set how many records to return
        query.maxResults = size
        return query.resultList
    }
}
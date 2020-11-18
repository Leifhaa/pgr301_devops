package com.example.pgr301_devops.controller

import com.example.pgr301_devops.Pgr301DevopsApplication
import com.example.pgr301_devops.repository.TaskRepository
import com.example.pgr301_devops.service.DatabaseInitializer
import io.restassured.RestAssured
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes=[(Pgr301DevopsApplication::class)], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {


    //Will be random generated and injected
    @LocalServerPort
    protected var port = 0

    @Autowired
    protected lateinit var repository: TaskRepository

    @Autowired
    protected lateinit var dbInitializer: DatabaseInitializer

    val page : Int = 5

    @BeforeEach
    @AfterEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost/api/tasks"
        RestAssured.port = port
        RestAssured.basePath = "/"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        //Todo: Delete DB values
    }

    @Test
    fun testGetPage() {
        given().accept(ContentType.JSON)
                .get()
                .then()
                .statusCode(200)
    }

}

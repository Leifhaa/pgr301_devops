package com.example.pgr301_devops

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes=[(Pgr301DevopsApplication::class)], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class TestBase{

    //Will be random generated and injected
    @LocalServerPort
    protected var port = 0

    @BeforeEach
    @AfterEach
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        //Todo: Delete DB values


    }
}
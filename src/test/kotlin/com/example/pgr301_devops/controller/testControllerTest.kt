package com.example.pgr301_devops.controller

import com.example.pgr301_devops.TestBase
import io.restassured.RestAssured.*
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test

class testControllerTest: TestBase(){
    @Test
    fun testCleanDB() {

        given().get().then()
                .statusCode(200)
                .body("id", equalTo(0))
    }

}

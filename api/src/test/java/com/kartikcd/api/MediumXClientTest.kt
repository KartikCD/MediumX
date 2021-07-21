package com.kartikcd.api

import com.kartikcd.api.models.entities.LoginData
import com.kartikcd.api.models.entities.SignupData
import com.kartikcd.api.models.requests.LoginRequest
import com.kartikcd.api.models.requests.SignupRequest
import com.kartikcd.api.models.response.UserResponse
import org.junit.Test
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import javax.xml.ws.Response
import kotlin.random.Random

class MediumXClientTest {

    @Test
    fun `GET articles`() {
        runBlocking {
            val articles = MediumXClient.PUBLIC_API.getArticles()
            assertNotNull(articles.body()?.articles)
        }
    }

    @Test
    fun `Login User`() {
        runBlocking {

            val userBody = LoginData(
                "errayindia@gmail.com",
                "dhwani01101999"
            )
            val user = MediumXClient.authApi.loginUser(LoginRequest(userBody))
            assertEquals(userBody.email, user.body()?.user?.email)
        }
    }

    @Test
    fun `Signup User`() {
        runBlocking {
            val userBody = SignupData(
                email = "testmail${Random.nextInt(999, 9999)}@mail.com",
                password = "textpass${Random.nextInt(9999, 99999)}",
                username = "username${Random.nextInt(99, 999)}"
            )
            val user = MediumXClient.authApi.signupUser(SignupRequest(userBody))
            assertEquals(userBody.username, user.body()?.user?.username)
        }
    }

}
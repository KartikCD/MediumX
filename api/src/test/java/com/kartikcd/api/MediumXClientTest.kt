package com.kartikcd.api

import org.junit.Test
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull

class MediumXClientTest {

    @Test
    fun `GET articles`() {
        runBlocking {
            val articles = MediumXClient.publicApi.getArticles()
            assertNotNull(articles.body()?.articles)
        }
    }
}
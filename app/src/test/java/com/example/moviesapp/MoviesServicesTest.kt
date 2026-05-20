package com.example.moviesapp

import com.example.moviesapp.api.models.movies.MoviesBaseResponse
import com.example.moviesapp.api.requests.MoviesServices
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals

class MoviesServicesTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var moviesServices: MoviesServices

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val logging =
            HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        moviesServices = retrofit.create(MoviesServices::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getMovies returns expected data`() {
        val mockResponse = MoviesBaseResponse(
            page = 1,
            results = listOf(),
            totalPages = 1,
            totalResults = 1
        )
        val responseJson = Gson().toJson(mockResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setBody(responseJson)
                .addHeader("Content-Type", "application/json")
        )

        runBlocking {
            val response = moviesServices.getMovies(
                includeAdult = false,
                includeVideo = false,
                language = "en",
                page = 1
            )

            assertEquals(mockResponse, response)
        }

        val request = mockWebServer.takeRequest()
        assertEquals(
            "/3/discover/movie?sort_by=popularity.desc&include_adult=false&include_video=false&language=en&page=1",
            request.path
        )
    }
}

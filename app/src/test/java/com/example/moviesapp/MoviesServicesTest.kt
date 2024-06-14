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

    private lateinit var mMockWebServer: MockWebServer
    private lateinit var mMoviesServices: MoviesServices

    @Before
    fun setUp() {
        mMockWebServer = MockWebServer()
        mMockWebServer.start()

        val lLogging =
            HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) }
        val lClient = OkHttpClient.Builder().addInterceptor(lLogging).build()

        val lRetrofit = Retrofit.Builder()
            .baseUrl(mMockWebServer.url("/"))
            .client(lClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mMoviesServices = lRetrofit.create(MoviesServices::class.java)
    }

    @After
    fun tearDown() {
        mMockWebServer.shutdown()
    }

    @Test
    fun `getMovies returns expected data`() {
        val lMockResponse = MoviesBaseResponse(
            page = 1,
            results = listOf(),
            total_pages = 1,
            total_results = 1
        )
        val lResponseJson = Gson().toJson(lMockResponse)

        mMockWebServer.enqueue(
            MockResponse()
                .setBody(lResponseJson)
                .addHeader("Content-Type", "application/json")
        )

        runBlocking {
            val lResponse = mMoviesServices.getMovies(
                aIncludeAdult = false,
                aIncludeVideo = false,
                aLanguage = "en",
                aPage = 1
            )

            assertEquals(lMockResponse, lResponse)
        }
    }
}

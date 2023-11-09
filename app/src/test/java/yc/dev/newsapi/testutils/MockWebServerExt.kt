package yc.dev.newsapi.testutils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import retrofit2.Retrofit

fun MockWebServer.enqueueResponse(fileName: String, statusCode: Int) {
    val classLoader = this.javaClass.classLoader ?: throw IllegalStateException("Class loader not found.")
    val response = MockResponse().apply {
        val path = String.format("api-response/%s", fileName)
        val inputStream =
            classLoader.getResourceAsStream(path) ?: throw IllegalStateException("File($fileName) not found on $path")
        val source = inputStream.source().buffer()
        setBody(source.readUtf8())
        setResponseCode(statusCode)
    }
    enqueue(response)
}

fun <T> MockWebServer.service(service: Class<T>): T {
    return Retrofit.Builder()
        .baseUrl(url("/"))
        .addConverterFactory(Json { coerceInputValues = true }.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(service)
}
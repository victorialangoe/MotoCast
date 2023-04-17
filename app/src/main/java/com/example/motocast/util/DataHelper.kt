package com.example.motocast.util

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class DataHelper: ViewModel() {


    fun <T> fetchData(
        apiCall: suspend () -> Response<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = apiCall()

                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            onSuccess(data)
                        } else {
                            Log.d("Empty response", response.toString())
                            onError("Empty response")
                        }
                    } else {
                        Log.d("Error", response.toString())
                        onError("Error: ${response.code()} ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                    onError(e.localizedMessage ?: "Unknown error")
                }
            }
        }
    }

    fun <T> createAPI(
        apiClass: Class<T>,
        baseUrl: String,
        apiKey: String? = null,
        headers: Map<String, String>? = null
    ): T? {
        return try {
            val clientBuilder = OkHttpClient.Builder()

            // Optional API key header
            apiKey?.let { key ->
                    clientBuilder.addInterceptor { chain ->
                        val requestBuilder = chain.request().newBuilder()
                        requestBuilder.header("X-Gravitee-API-Key", key)
                        chain.proceed(requestBuilder.build())
                    }
            }

            // Optional custom headers
            headers?.let { customHeaders ->
                clientBuilder.addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    customHeaders.forEach { (name, value) ->
                        requestBuilder.header(name, value)
                    }
                    chain.proceed(requestBuilder.build())
                }
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(apiClass)
        } catch (e: Exception) {
            Log.d("DataHelper","Error: ${e}")
            null
        }
    }

}
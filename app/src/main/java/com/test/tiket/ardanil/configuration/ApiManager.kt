package com.test.tiket.ardanil.configuration

import com.test.tiket.ardanil.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiManager {

    private val BASEURL = BuildConfig.BASE_URL

    private var retrofit: Retrofit? = null
    private var services: ApiEndpoint? = null
    private val REQUESTTIMEOUT = 30

    fun getService(): ApiEndpoint? {
        if(services != null){
            return services
        }
        if(retrofit == null){
            initializeRetrofit()
        }
        services = retrofit?.create(ApiEndpoint::class.java)
        return services
    }

    private fun initializeRetrofit(){
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient().newBuilder()
        httpClient.readTimeout(REQUESTTIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.connectTimeout(REQUESTTIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(httpClient.build())
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .build()
    }
}
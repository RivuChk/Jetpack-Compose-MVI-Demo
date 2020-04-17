package dev.rivu.mvijetpackcomposedemo.moviesearch.data.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.rivu.mvijetpackcomposedemo.BuildConfig
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

object MovieApiFactory {
    fun makeMovieApi(): MovieApi {
        val okHttpClient = makeOkHttpClient(
            makeLoggingInterceptor()
        )
        return makeMovieApi(okHttpClient, makeGson())
    }

    private fun makeMovieApi(okHttpClient: OkHttpClient, gson: Gson): MovieApi {
        return makeRetrofit(okHttpClient, gson).create(MovieApi::class.java)
    }

    private fun makeRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun makeOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    private fun makeGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(httpLogger)
        loggingInterceptor.level =
            HttpLoggingInterceptor.Level.BODY //No need to check if debug, since using Timber
        return loggingInterceptor
    }

    private val httpLogger: HttpLoggingInterceptor.Logger by lazy {
        object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.d("HTTP::TrendingService:: $message")
            }
        }
    }
}
package com.example.tarclearn.api

import com.example.tarclearn.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //change the base url according to the api's uri
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient = OkHttpClient().newBuilder().addInterceptor(logging).build()

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApi by lazy{
        retrofit.create(UserApi::class.java)
    }

    val courseApi: CourseApi by lazy{
        retrofit.create(CourseApi::class.java)
    }
    val chapterApi: ChapterApi by lazy{
        retrofit.create(ChapterApi::class.java)
    }
    val materialApi: MaterialApi by lazy{
        retrofit.create(MaterialApi::class.java)
    }
    val discussionApi: DiscussionApi by lazy{
        retrofit.create(DiscussionApi::class.java)
    }
    val messageApi: MessageApi by lazy{
        retrofit.create(MessageApi::class.java)
    }
    val quizApi: QuizApi by lazy{
        retrofit.create(QuizApi::class.java)
    }
}
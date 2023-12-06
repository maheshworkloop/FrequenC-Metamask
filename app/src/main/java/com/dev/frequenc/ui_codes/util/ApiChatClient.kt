package com.dev.frequenc.ui_codes.util

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiChatClient {
    companion object {
        private var retrofit: Retrofit? = null
        private var apiService: ApiService? = null

        private fun getClient(): Retrofit? {
            if (retrofit == null) {

                val okHttpClient: OkHttpClient =
                    OkHttpClient.Builder().build()

                retrofit = Retrofit.Builder()
                    .baseUrl(KeysConstant.CHAT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
            return retrofit
        }


        fun getInstance(): ApiService? {
            if (apiService == null) {
                apiService = getClient()!!.create(ApiService::class.java)
            }
            return apiService
        }

    }


}
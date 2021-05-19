package com.example.zolpe_05


interface ChatService {
    @POST("/message")
    fun postMessage(@Body body:Message): Call<Void>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/"

        fun create(): ChatService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            return retrofit.create(ChatService::class.java)
        }
    }
}
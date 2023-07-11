package com.apex.codeassesment.data.remote

import com.apex.codeassesment.data.model.BaseUserResponse
import retrofit2.http.GET

interface WebService {

    @GET("api")
    suspend fun loadSingleUser(): BaseUserResponse

    @GET("api?results=10")
    suspend fun loadUsers(): BaseUserResponse
}
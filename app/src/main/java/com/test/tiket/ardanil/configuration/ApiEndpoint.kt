package com.test.tiket.ardanil.configuration

import com.test.tiket.ardanil.model.SearchUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {

    @GET(ApiKeys.SEARCH_USERS)
    fun getSearchUsers(
        @Query("page") page: String?,
        @Query("q") q: String?,
        @Query("per_page") per_page: String?
    ): Call<SearchUsersResponse>

}
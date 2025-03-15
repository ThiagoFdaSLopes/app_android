// JobService.kt
package com.grupo.appandroid.service

import JobResponse
import com.grupo.appandroid.model.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface JobService {
    @GET("v1/api/jobs/br/search/{page}")
    suspend fun searchJobs(
        @Path("page") page: Int = 1,
        @Query("app_id") appId: String = "5a6db800",
        @Query("app_key") appKey: String = "efd24a6990aff00bb79c366b0a37812b",
        @Query("results_per_page") resultsPerPage: Int = 20,
        @Query("content-type") contentType: String = "application/json",
        @Query("category") category: String? = null
    ): Response<JobResponse>

    @GET("v1/api/jobs/br/categories")
    suspend fun getCategories(
        @Query("app_id") appId: String = "5a6db800",
        @Query("app_key") appKey: String = "efd24a6990aff00bb79c366b0a37812b",
        @Query("content-type") contentType: String = "application/json"
    ): Response<CategoryResponse>
}

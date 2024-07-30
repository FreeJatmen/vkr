package com.example.vkr.ui.retrofit

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    suspend fun loginUser(@Body user: MyUser ): Response<AuthTokenResponse>

    @POST("register")
    suspend fun registerUser(@Body user: MyUser): Response<AuthTokenResponse>

    @POST("add_vacancies")
    suspend fun addVacancies(@Header("token") token: String, @Body vacancies: Vacancies): Response<Vacancies>

    @GET("view_login_vacancies")
    suspend fun viewLoggedVacancies(@Header("token") token: String): Response<List<Vacancies>>

    @GET("view_vacancies")
    suspend fun viewAllVacancies(): Response<List<Vacancies>>

    @DELETE("delete_vacancies/{id}")
    suspend fun deleteVacancy(@Path("id") id: String): Response<Unit>

    @GET("search_vacancies")
    suspend fun searchVacancies(@Query("query") query: String): Response<List<Vacancies>>

    @POST("update_vacancy/{id}")
    suspend fun updateVacancy(@Header("token") token: String, @Path("id") id: String, @Body vacancies: Vacancies): Response<Unit>

    @GET("get_auth_vacancy/{id}")
    suspend fun getAuthVacancy(@Header("token") token: String, @Path("id") id: String): Response<Vacancies>

    @GET("get_vacancy/{id}")
    suspend fun getVacancy(@Path("id") id: String): Response<Vacancies>

    @POST("change_password")
    suspend fun changePassword(@Header("token") token: String, @Header("newPassword") newPassword: String, @Body user: MyUser): Response<ResponseBody>

    @GET("view_resume")
    suspend fun viewResume(@Header("token") token: String): Response<List<Resume>>

    @POST("add_resume")
    suspend fun addResume(@Header("token") token: String, @Body resume: Resume): Response<Resume>

    @GET("get_resume/{id}")
    suspend fun getResume(@Header("token") token: String, @Path("id") id: String): Response<Resume>

    @DELETE("delete_resume/{id}")
    suspend fun deleteResume(@Path("id") id: String): Response<Unit>

    @POST("update_resume/{id}")
    suspend fun updateResume(@Header("token") token: String, @Path("id") id: String, @Body resume: Resume): Response<Unit>

    @POST("reply_to_vacancy")
    suspend fun replyToVacancy(@Header("token") token: String, @Body resumeRoute: ResumeRoute): Response<Unit>

    @GET("view_resume_to_vacancy")
    suspend fun viewResumeToVacancy(@Header("token") token: String, @Header("id") id: String): Response<List<Resume>>
}


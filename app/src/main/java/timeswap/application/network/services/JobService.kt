package timeswap.application.network.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import timeswap.application.data.request.JobPostRequest

import timeswap.application.data.response.BaseResponse
import timeswap.application.data.response.JobDetailResponse
import timeswap.application.data.response.JobPostResponse
import timeswap.application.network.RetrofitClient
import timeswap.application.shared.constants.AppConstants

interface JobService {

    @POST("jobposts")
    suspend fun postJob(
        @Header("Authorization") token: String,
        @Body request: JobPostRequest
    ): Response<BaseResponse<JobPostResponse>>

    @GET("jobposts")
    suspend fun getJobLists(
        @Query("IndustryId") industryId: Int? = null,
        @Query("Search") search: String? = null,
        @Query("PageIndex") pageIndex: Int = AppConstants.PAGE_INDEX,
        @Query("PageSize") pageSize: Int = AppConstants.PAGE_SIZE
    ): Response<BaseResponse<JobPostResponse>>

    @GET("jobposts/{id}")
    suspend fun getJobDetail(
        @Path("id") jobId: String,
        @Header("Authorization") token: String
    ): Response<BaseResponse<JobDetailResponse>>

}


class JobPostService {

    suspend fun getJobLists(industryId: Int?, search: String?, pageIndex: Int, pageSize: Int): JobPostResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.jobService.getJobLists(industryId, search, pageIndex, pageSize)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 1000) {
                        body.data
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: HttpException) {
                null
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getJobDetail(jobId: String, accessToken: String): JobDetailResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.jobService.getJobDetail(jobId, "Bearer $accessToken")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 1000) {
                        body.data
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: HttpException) {
                null
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun postJob(accessToken: String, jobPostRequest: JobPostRequest): JobPostResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.jobService.postJob("Bearer $accessToken", jobPostRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.statusCode == 1000) {
                        body.data
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: HttpException) {
                null
            } catch (e: Exception) {
                null
            }
        }
    }

}



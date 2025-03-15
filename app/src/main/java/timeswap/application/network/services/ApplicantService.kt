package timeswap.application.network.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

import timeswap.application.data.response.BaseResponse
import timeswap.application.data.response.ApplicantResponse
import timeswap.application.network.RetrofitClient

import timeswap.application.shared.constants.AppConstants

interface ApplicantService {
    @GET("applicants/{jobPostId}")
    suspend fun getApplicantList(
        @Path("jobPostId") jobPostId: String,
        @Query("PageIndex") pageIndex: Int = AppConstants.PAGE_INDEX,
        @Query("PageSize") pageSize: Int = AppConstants.PAGE_SIZE,
        @Header("Authorization") token: String
    ): Response<BaseResponse<ApplicantResponse>>
}

class ApplicantsService {
    suspend fun getApplicantList(jobPostId: String, token: String, pageIndex: Int, pageSize: Int): ApplicantResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.applicantService.getApplicantList(jobPostId, pageIndex, pageSize, "Bearer $token")
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
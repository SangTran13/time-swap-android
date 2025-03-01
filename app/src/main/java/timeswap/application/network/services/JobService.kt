package timeswap.application.network.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

import timeswap.application.data.response.BaseResponse
import timeswap.application.data.response.JobListResponse
import timeswap.application.network.RetrofitClient
import timeswap.application.shared.constants.AppConstants

interface JobService {

    @GET("jobposts")
    suspend fun getJobLists(
        @Query("PageIndex") pageIndex: Int = AppConstants.PAGE_INDEX,
        @Query("PageSize") pageSize: Int = AppConstants.PAGE_SIZE
    ): Response<BaseResponse<JobListResponse>>
}

class JobListRepository {
    suspend fun getJobLists(pageIndex: Int, pageSize: Int): JobListResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.jobService.getJobLists(pageIndex, pageSize)
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

package timeswap.application.network.services

import retrofit2.Response
import retrofit2.http.GET

import timeswap.application.data.entity.Industry
import timeswap.application.data.response.BaseResponse

interface IndustryService {

    @GET("industries")
    suspend fun getIndustries(): Response<BaseResponse<List<Industry>>>

}
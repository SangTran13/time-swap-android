package timeswap.application.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

import timeswap.application.data.response.BaseResponse
import timeswap.application.data.entity.City
import timeswap.application.data.entity.Ward

interface LocationService {

    @GET("location/cities")
    suspend fun getCities(): Response<BaseResponse<List<City>>>

    @GET("location/cities/{cityId}/wards")
    suspend fun getWards(@Path("cityId") cityId: String): Response<BaseResponse<List<Ward>>>

}
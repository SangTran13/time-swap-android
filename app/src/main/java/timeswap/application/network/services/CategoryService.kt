package timeswap.application.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import timeswap.application.data.entity.Category
import timeswap.application.data.response.BaseResponse

interface CategoryService {

    @GET("industries/{industryId}/categories")
    suspend fun getCategories(@Path("industryId") industryId: Int): Response<BaseResponse<List<Category>>>

}
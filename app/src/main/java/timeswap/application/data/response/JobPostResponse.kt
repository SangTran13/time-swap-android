package timeswap.application.data.response

import timeswap.application.data.entity.JobPost


data class JobPostResponse(
    val pageIndex: Int,
    val pageSize: Int,
    val count: Int,
    val data: List<JobPost>
)

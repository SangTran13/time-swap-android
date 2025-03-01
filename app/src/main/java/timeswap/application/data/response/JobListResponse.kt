package timeswap.application.data.response

import timeswap.application.data.entity.JobList


data class JobListResponse(
    val pageIndex: Int,
    val pageSize: Int,
    val count: Int,
    val data: List<JobList>
)

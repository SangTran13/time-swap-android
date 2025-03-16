package timeswap.application.data.response

import timeswap.application.data.entity.Applicant

data class ApplicantResponse (

    val pageIndex: Int,
    val pageSize: Int,
    val count: Int,
    val data: List<Applicant>

)
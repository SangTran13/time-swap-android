package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timeswap.application.data.response.JobDetailResponse
import timeswap.application.network.services.JobPostService

class JobDetailViewModel(private val repository: JobPostService) : ViewModel() {
    private val _jobDetail = MutableStateFlow<JobDetailResponse?>(null)
    val jobDetail: StateFlow<JobDetailResponse?> get() = _jobDetail

    fun fetchJobDetail(jobId: String, accessToken: String) {
        viewModelScope.launch {
            val detail = repository.getJobDetail(jobId, accessToken)
            _jobDetail.value = detail
        }
    }
}
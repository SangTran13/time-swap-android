package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timeswap.application.data.entity.Applicant
import timeswap.application.data.response.ApplicantResponse
import timeswap.application.network.services.ApplicantsService
import timeswap.application.shared.constants.AppConstants

sealed class ApplicantListUiState {

    data object Loading : ApplicantListUiState()
    data class Success(val applicantList: List<Applicant>, val pageIndex: Int, val totalPages: Int) : ApplicantListUiState()
    data class Error(val message: String) : ApplicantListUiState()

}

class ApplicantViewModel(private val repository: ApplicantsService): ViewModel() {

    private val _uiState = MutableStateFlow<ApplicantListUiState>(ApplicantListUiState.Loading)
    val uiState: StateFlow<ApplicantListUiState> = _uiState.asStateFlow()

    private var pageIndex = AppConstants.PAGE_INDEX
    private val pageSize = AppConstants.PAGE_SIZE
    private var totalPages = 1

    private var currentJobId: String? = null
    private var currentAccessToken: String? = null

    fun fetchApplicantList(jobId: String, accessToken: String) {
        currentJobId = jobId
        currentAccessToken = accessToken
        pageIndex = 1
        loadApplicants()
    }

    fun loadApplicants() {
        val jobId = currentJobId ?: return
        val accessToken = currentAccessToken ?: return

        viewModelScope.launch {
            _uiState.value = ApplicantListUiState.Loading
            try {
                val response: ApplicantResponse? = repository.getApplicantList(jobId, accessToken, pageIndex, pageSize)
                if (response != null) {
                    totalPages = (response.count + pageSize - 1) / pageSize
                    _uiState.value = ApplicantListUiState.Success(response.data, pageIndex, totalPages)
                } else {
                    _uiState.value = ApplicantListUiState.Error("No applicants found")
                }
            } catch (e: Exception) {
                _uiState.value = ApplicantListUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun nextPage() {
        if (pageIndex < totalPages) {
            pageIndex++
            loadApplicants()
        }
    }

    fun previousPage() {
        if (pageIndex > 1) {
            pageIndex--
            loadApplicants()
        }
    }

}
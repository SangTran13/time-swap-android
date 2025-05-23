package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import timeswap.application.data.entity.JobPost
import timeswap.application.network.services.JobPostService
import timeswap.application.shared.constants.AppConstants

sealed class JobListUiState {

    data object Loading : JobListUiState()
    data class Success(val jobList: List<JobPost>, val pageIndex: Int, val totalPages: Int) : JobListUiState()
    data class Error(val message: String) : JobListUiState()

}

class JobListViewModel : ViewModel() {

    private val jobListService = JobPostService()

    private val _uiState = MutableStateFlow<JobListUiState>(JobListUiState.Loading)
    val uiState: StateFlow<JobListUiState> = _uiState.asStateFlow()

    private var pageIndex = AppConstants.PAGE_INDEX
    private val pageSize = AppConstants.PAGE_SIZE
    private var totalPages = 1

    private var currentSearchQuery: String? = null
    private var currentIndustryId: Int? = null

    init {
        fetchJobLists()
    }

    private fun fetchJobLists() {
        viewModelScope.launch {
            _uiState.value = JobListUiState.Loading
            try {
                val response = jobListService.getJobLists(currentIndustryId, currentSearchQuery, pageIndex, pageSize)
                if (response != null) {
                    totalPages = (response.count + pageSize - 1) / pageSize
                    _uiState.value = JobListUiState.Success(response.data, pageIndex, totalPages)
                } else {
                    _uiState.value = JobListUiState.Error("No job lists found")
                }
            } catch (e: Exception) {
                _uiState.value = JobListUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun searchJobs(query: String) {
        currentSearchQuery = query
        pageIndex = 1
        fetchJobLists()
    }

    fun filterByIndustry(industryId: Int?) {
        currentIndustryId = industryId
        pageIndex = 1
        fetchJobLists()
    }

    fun nextPage() {
        if (pageIndex < totalPages) {
            pageIndex++
            fetchJobLists()
        }
    }

    fun previousPage() {
        if (pageIndex > 1) {
            pageIndex--
            fetchJobLists()
        }
    }

}
package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import timeswap.application.data.entity.JobList
import timeswap.application.network.services.JobListRepository
import timeswap.application.shared.constants.AppConstants

sealed class JobListUiState {
    data object Loading : JobListUiState()
    data class Success(val jobList: List<JobList>, val pageIndex: Int, val totalPages: Int) : JobListUiState()
    data class Error(val message: String) : JobListUiState()
}

class JobListViewModel : ViewModel() {
    private val jobListRepository = JobListRepository()

    private val _uiState = MutableStateFlow<JobListUiState>(JobListUiState.Loading)
    val uiState: StateFlow<JobListUiState> = _uiState.asStateFlow()

    private var pageIndex = AppConstants.PAGE_INDEX
    private val pageSize = AppConstants.PAGE_SIZE
    private var totalPages = 1

    init {
        fetchJobLists()
    }

    private fun fetchJobLists() {
        viewModelScope.launch {
            _uiState.value = JobListUiState.Loading
            try {
                val response = jobListRepository.getJobLists(pageIndex, pageSize)
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

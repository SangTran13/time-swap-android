package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timeswap.application.data.entity.Category
import timeswap.application.data.entity.Industry
import timeswap.application.network.RetrofitClient
import timeswap.application.network.services.CategoryService
import timeswap.application.network.services.IndustryService

class IndustryCategoryViewModel(
    private val industryService: IndustryService = RetrofitClient.industryService,
    private val categoryService: CategoryService = RetrofitClient.categoryService
) : ViewModel() {

    private val _industries = MutableStateFlow<List<Industry>>(emptyList())
    val industries: StateFlow<List<Industry>> get() = _industries

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    fun loadIndustries() {
        viewModelScope.launch {
            val response = industryService.getIndustries()
            if (response.isSuccessful) {
                response.body()?.data?.let { _industries.value = it }
            }
        }
    }

    fun loadCategories(industryId: Int) {
        viewModelScope.launch {
            val response = categoryService.getCategories(industryId)
            if (response.isSuccessful) {
                response.body()?.data?.let { _categories.value = it }
            }
        }
    }
}

package timeswap.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timeswap.application.data.entity.City
import timeswap.application.data.entity.Ward
import timeswap.application.network.services.LocationService
import timeswap.application.network.RetrofitClient

class LocationViewModel(
    private val locationService: LocationService = RetrofitClient.locationService
) : ViewModel() {

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> get() = _cities

    private val _wards = MutableStateFlow<List<Ward>>(emptyList())
    val wards: StateFlow<List<Ward>> get() = _wards

    fun loadCities() {
        viewModelScope.launch {
            val response = locationService.getCities()
            if (response.isSuccessful) {
                response.body()?.data?.let { _cities.value = it }
            }
        }
    }

    fun loadWards(cityId: String) {
        viewModelScope.launch {
            val response = locationService.getWards(cityId)
            if (response.isSuccessful) {
                response.body()?.data?.let { _wards.value = it }
            }
        }
    }
}
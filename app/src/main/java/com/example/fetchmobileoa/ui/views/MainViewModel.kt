package com.example.fetchmobileoa.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchmobileoa.domain.model.Item
import com.example.fetchmobileoa.domain.usecase.GetAllItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase
) : ViewModel() {
    sealed class MainUIState {
        data object Loading : MainUIState()
        data class Loaded(val items: List<Item>): MainUIState()
    }

    private val _mainUIState: MutableStateFlow<MainUIState> = MutableStateFlow(MainUIState.Loading)
    val mainUIState: StateFlow<MainUIState> = _mainUIState

    init {
        viewModelScope.launch {
            val items = getAllItemsUseCase.invoke()

            _mainUIState.value = MainUIState.Loaded(items = items)
        }
    }
}
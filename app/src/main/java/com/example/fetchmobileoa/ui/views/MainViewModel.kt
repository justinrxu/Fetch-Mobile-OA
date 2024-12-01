package com.example.fetchmobileoa.ui.views

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchmobileoa.domain.exception.ApiException
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
        data class Loaded(
            val items: List<Item>,
            val expanded: SnapshotStateMap<Int, Boolean>
        ): MainUIState()
        data class Error(val e: String?) : MainUIState()
    }

    private val _mainUIState: MutableStateFlow<MainUIState> = MutableStateFlow(MainUIState.Loading)
    val mainUIState: StateFlow<MainUIState> = _mainUIState

    init {
        viewModelScope.launch {
            try {
                val items = getAllItemsUseCase.invoke()

                _mainUIState.value =
                    MainUIState.Loaded(items = items, expanded = mutableStateMapOf())
            } catch (e: ApiException) {
                _mainUIState.value = MainUIState.Error(e.message)
            }
        }
    }
}
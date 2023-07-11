package com.apex.codeassesment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.model.HomeUiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val userLiveData: LiveData<HomeUiState>
        get() = _userLiveData
    private val _userLiveData = MutableLiveData<HomeUiState>()


    fun getUser() = viewModelScope.launch {
        kotlin.runCatching {
            userRepository.getUser(true)
        }.onSuccess {
            _userLiveData.postValue(HomeUiState.SingleUserSuccess(it))
        }.onFailure {
            _userLiveData.postValue(HomeUiState.Error(it.message?: "Something went wrong"))
        }
    }

    fun getUsers() = viewModelScope.launch {
        kotlin.runCatching {
            userRepository.getUsers()
        }.onSuccess {
            _userLiveData.postValue(HomeUiState.Success(it))
        }.onFailure {
            _userLiveData.postValue(HomeUiState.Error(it.message?: "Something went wrong"))
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(userRepository) as T
        }
    }

}
package com.apex.codeassesment.data.model

sealed class HomeUiState {
    object Loading : HomeUiState()
    class Success(val users: List<User>) : HomeUiState()
    class SingleUserSuccess(val user: User) : HomeUiState()
    class Error(val message: String) : HomeUiState()
}

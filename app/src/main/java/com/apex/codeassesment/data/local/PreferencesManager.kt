package com.apex.codeassesment.data.local

import android.content.SharedPreferences
import javax.inject.Inject

// TODO (2 point): Add tests
class PreferencesManager @Inject constructor(
  private val sharedPreferences: SharedPreferences
){

  fun saveUser(user: String) {
    sharedPreferences.edit()?.putString("saved-user", user)?.apply()
  }

  fun loadUser(): String? {
    return sharedPreferences.getString("saved-user", null)
  }
}

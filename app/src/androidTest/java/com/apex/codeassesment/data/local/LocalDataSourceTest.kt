package com.apex.codeassesment.data.local

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.apex.codeassesment.data.model.User
import com.squareup.moshi.Moshi
import org.junit.Assert.*
import org.junit.Test

class LocalDataSourceTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun when_save_user_is_called_user_is_saved() {
        val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val preferencesManager = PreferencesManager(sharedPreferences = sharedPreferences)
        val moshi =  Moshi.Builder().build()
        val localDataSource = LocalDataSource(preferencesManager = preferencesManager, moshi =moshi)
        val createRandom = User.createRandom()
        val stringUser = moshi.adapter(User::class.java).toJson(createRandom)
        localDataSource.saveUser(createRandom)
        assertEquals(stringUser, sharedPreferences.getString("saved-user", null))
    }

    @Test
    fun when_save_user_is_called_user_is_saved_and_load_user_returns_saved_user() {
        val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val preferencesManager = PreferencesManager(sharedPreferences = sharedPreferences)
        val moshi =  Moshi.Builder().build()
        val localDataSource = LocalDataSource(preferencesManager = preferencesManager, moshi =moshi)
        val createRandom = User.createRandom()
        localDataSource.saveUser(createRandom)
        assertEquals(createRandom, localDataSource.loadUser())
    }

    @Test
    fun when_load_user_is_called_with_invalid_data_throws_json_exception() {
        val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val preferencesManager = PreferencesManager(sharedPreferences = sharedPreferences)
        val moshi =  Moshi.Builder().build()
        val localDataSource = LocalDataSource(preferencesManager = preferencesManager, moshi =moshi)
        preferencesManager.saveUser("test")
        assertThrows(Exception::class.java) {
            localDataSource.loadUser()
        }
    }
}
package com.apex.codeassesment.data.local

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

class PreferencesManagerTest {
    private val context: Context =InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun when_save_user_is_called_user_is_saved() {
        val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val preferencesManager = PreferencesManager(sharedPreferences = sharedPreferences)
        preferencesManager.saveUser("test")
        assertEquals("test", sharedPreferences.getString("saved-user", null))
    }

    @Test
    fun when_user_is_saved_load_user_returns_saved_user() {
        val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val preferencesManager = PreferencesManager(sharedPreferences = sharedPreferences)
        preferencesManager.saveUser("test")
        assertEquals("test", preferencesManager.loadUser())
    }

    @Test
    fun when_user_is_not_saved_load_user_returns_null() {
        val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val preferencesManager = PreferencesManager(sharedPreferences = sharedPreferences)
        assertEquals(null, preferencesManager.loadUser())
    }
}
package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.BaseUserResponse
import com.apex.codeassesment.data.model.Id
import com.apex.codeassesment.data.model.Name
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.atomic.AtomicReference

class UserRepositoryImplementationTest {

    private lateinit var userRepository: UserRepositoryImplementation
    private val localDataSource: LocalDataSource = mockk()
    private val remoteDataSource: RemoteDataSource = mockk()

    @Before
    fun setup() {
        userRepository = UserRepositoryImplementation(localDataSource, remoteDataSource)
    }

    @Test
    fun get_saved_user_returns_correct_user() {
        val user = User()
        every { localDataSource.loadUser() } returns user

        val savedUser = userRepository.getSavedUser()

        assertEquals(user, savedUser)
        verify(exactly = 1) { localDataSource.loadUser() }
    }

    @Test
    fun get_user_calls_remote_data_when_force_update_is_true() = runBlocking {
        val baseUserResponse = BaseUserResponse(results = listOf(User(id = Id(name = ""), name = Name(first = "random", last = "user"))))
        val user = User(id = Id(name = ""), name = Name(first = "random", last = "user"))

        coEvery { remoteDataSource.loadSingleUser() } returns baseUserResponse
        coEvery { localDataSource.saveUser(user) } just Runs

        val result = userRepository.getUser(forceUpdate = true)

        assertEquals(result, user)
        coVerify(exactly = 1) { remoteDataSource.loadSingleUser() }
        verify(exactly = 1) { localDataSource.saveUser(user) }
    }

    @Test
    fun get_user_calls_local_data_when_force_update_is_false() = runBlocking {
        val user = User(id = Id(name = ""), name = Name(first = "random", last = "user"))
        UserRepositoryImplementation::class.java.getDeclaredField("savedUser").apply {
            isAccessible = true
            set(userRepository, AtomicReference(user))
        }

        val result = userRepository.getUser(forceUpdate = false)

        assertEquals(result, user)
        coVerify(exactly = 0) { remoteDataSource.loadSingleUser() }
        verify(exactly = 0) { localDataSource.saveUser(any()) }
    }

    @Test
    fun testGetUsers() = runBlocking {
        val users = listOf(User(id = Id(name = ""), name = Name(first = "random", last = "user")), User(id = Id(name = ""), name = Name(first = "random", last = "user")))
        val baseUserResponse = BaseUserResponse(results = users)

        coEvery { remoteDataSource.loadUsers() } returns baseUserResponse

        val result = userRepository.getUsers()

        assertEquals(result, users)
        coVerify(exactly = 1) { remoteDataSource.loadUsers() }
    }
}
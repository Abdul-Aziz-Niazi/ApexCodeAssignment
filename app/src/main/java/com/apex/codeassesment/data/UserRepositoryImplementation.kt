package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

interface UserRepository {
    fun getSavedUser(): User
    suspend fun getUser(forceUpdate: Boolean): User
    suspend fun getUsers(): List<User>
}

// TODO (2 points) : Add tests
// TODO (3 points) : Hide this class through an interface, inject the interface in the clients instead and remove warnings
class UserRepositoryImplementation @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : UserRepository {

    private val savedUser = AtomicReference(User())

    override fun getSavedUser() = localDataSource.loadUser()

    override suspend fun getUser(forceUpdate: Boolean): User {
        if (forceUpdate) {
            val baseUserResponse = remoteDataSource.loadSingleUser()
            val user = baseUserResponse.results.first()
            localDataSource.saveUser(user)
            savedUser.set(user)
        }
        return savedUser.get()
    }

    override suspend fun getUsers() = remoteDataSource.loadUsers().results
}

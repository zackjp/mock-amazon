package com.zackjp.mockamazon.shared.data

import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldNot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class UserRepositoryTest {

    @Test
    fun getUser_ReturnsAStaticUser() = runTest {
        val repository = UserRepository()

        repository.getUser() shouldNot beNull()
    }

}

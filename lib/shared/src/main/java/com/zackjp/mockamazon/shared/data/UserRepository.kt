package com.zackjp.mockamazon.shared.data

import com.zackjp.mockamazon.shared.model.User
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    suspend fun getUser(): User {
        delay(600) // simulate api delay here, for now
        return User(
            deliveryAddress = "123 Example St., Apt. 867, New York, NY 10101, United States",
            deliveryFullName = "John Doe",
            paymentMethodText = "Visa 1234",
        )
    }

}

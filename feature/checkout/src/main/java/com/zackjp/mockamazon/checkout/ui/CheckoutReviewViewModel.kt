package com.zackjp.mockamazon.checkout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zackjp.mockamazon.checkout.ui.model.CheckoutEffect
import com.zackjp.mockamazon.checkout.ui.model.CheckoutState
import com.zackjp.mockamazon.shared.data.CartRepository
import com.zackjp.mockamazon.shared.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CheckoutReviewViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
) : ContainerHost<CheckoutState, CheckoutEffect>, ViewModel() {

    override val container = container<CheckoutState, CheckoutEffect>(
        CheckoutState.Loading
    )

    fun load() = intent {
        val cartDeferred = viewModelScope.async { cartRepository.getCart() }
        val userDeferred = viewModelScope.async { userRepository.getUser() }

        val cart = cartDeferred.await()
        val user = userDeferred.await()

        reduce {
            CheckoutState.Loaded(
                cart = cart,
                user = user,
            )
        }
    }

}

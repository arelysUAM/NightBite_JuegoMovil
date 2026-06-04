package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

class AccountCredentialsViewModelFactory(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository = UserRepository()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountCredentialsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountCredentialsViewModel(
                userRepository = userRepository,
                sessionManager = sessionManager
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.repository.UserRepository

class StartViewModelFactory(
    private val sessionManager: SessionManager,
    private val userRepository: UserRepository = UserRepository()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            return StartViewModel(
                sessionManager = sessionManager,
                userRepository = userRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
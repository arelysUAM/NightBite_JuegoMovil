package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.repository.UserRepository
import ni.edu.uam.nightbiteapp.data.repository.ProgressSyncRepository

class AccountCredentialsViewModelFactory(
    private val sessionManager: SessionManager,
    private val progressSyncRepository: ProgressSyncRepository,
    private val userRepository: UserRepository = UserRepository()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountCredentialsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountCredentialsViewModel(
                userRepository = userRepository,
                sessionManager = sessionManager,
                progressSyncRepository = progressSyncRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
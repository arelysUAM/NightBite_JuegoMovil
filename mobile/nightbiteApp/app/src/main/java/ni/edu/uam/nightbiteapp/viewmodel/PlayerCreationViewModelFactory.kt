package ni.edu.uam.nightbiteapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager

class PlayerCreationViewModelFactory(
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerCreationViewModel::class.java)) {
            return PlayerCreationViewModel(sessionManager) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
package ni.edu.uam.nightbiteapp.data.local.session

data class UserSession(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val username: String = "",
    val email: String = "",
    val age: Int? = null,
    val hasPlayer: Boolean = false
)
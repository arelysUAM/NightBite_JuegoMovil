package ni.edu.uam.nightbiteapp.data.remote.dto

data class UpdateAccountInfoRequest(
    val username: String,
    val age: Int,
    val gender: String
)
package ni.edu.uam.nightbiteapp.data.remote.dto

data class UsernameAvailabilityResponse(
    val username: String,
    val available: Boolean,
    val message: String
)
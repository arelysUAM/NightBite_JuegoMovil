package ni.edu.uam.nightbiteapp.ui.utils

object UserDisplayIdGenerator {

    fun generate(
        userId: Long,
        username: String
    ): String {
        val formattedUserId = userId
            .toString()
            .padStart(4, '0')

        val usernameLength = username.length

        return "NB-$formattedUserId-L$usernameLength"
    }
}
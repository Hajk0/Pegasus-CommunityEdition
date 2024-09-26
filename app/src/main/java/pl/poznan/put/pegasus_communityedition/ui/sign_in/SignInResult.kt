package pl.poznan.put.pegasus_communityedition.ui.sign_in


data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val userEmail: String?,
    val profilePictureUrl: String?
)
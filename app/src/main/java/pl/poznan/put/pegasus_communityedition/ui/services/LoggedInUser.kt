package pl.poznan.put.pegasus_communityedition.ui.services

object LoggedInUser {
    var userEmail: String = ""

    fun updateUserEmail(userEmail: String) {
        this.userEmail = userEmail
    }
}
package resource.strings.english

import resource.strings.IStringResources

data class English(
    override val click: String = "Click",
    override val logo: String = "Logo",
    override val login: String = "Login",
    override val logout: String = "Logout",
    override val admin: String = "Admin",
    override val exit: String = "Exit",
    override val dinningIn: String = "Dinning in",
    override val outlet: String = "Outlet",
    override val settings: String = "Settings",
    override val userName: String = "Username",
    override val password: String = "Password",
    override val ok: String = "Ok",
    override val cancel: String = "Cancel",
    override val tryAgain: String = "Try Again",
    override val somethingWrongHappened: String = "Something wrong happened please try again later!",
    override val noInternetMessage: String = "Internet is not available",
    override val noInternetDescription: String = "Please make sure you are connected to the internet and try again",
    override val nullDataMessage: String = "There are no data",
    override val nullDataDescription: String = "Enjoy adding items to your app and get ready to enjoy",
    override val enterYourInfo: String = "Enter your information",
    override val warning: String = "Warning",
    override val error: String = "Error",
    override val userNotFound: String = "User not found",
    override val enterYourPasscode: String = "Enter Passcode",
    override val retry: String = "Retry",
    override val passcode: String = "Passcode",
    override val userNameEmptyException: String = "Username can't be empty",
    override val passwordEmptyException: String = "Password can't be empty",
    override val passcodeEmptyException: String = "Passcode can't be empty",
    override val authEmptyException: String = "Please enter username and password first",
    override val doYouWantToCloseApp: String = "Do you want to close app ?",
    override val covers: String = "Covers",
    override val welcome: String = "Welcome",
    override val bye: String = "GoodBye",
    override val itemAddedSuccess: String = "Item has been added successfully",
    override val alreadyOpenChecks: String = "There is an open check, do you want to make new check?",
) : IStringResources
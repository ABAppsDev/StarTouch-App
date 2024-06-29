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
    override val enterYourInfo: String = "Your information",
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
    override val comment: String = "Comment",
    override val createTableGuest: String = "Create Table Guest",
    override val showAllTableGuest: String = "Show All Tables Guest",
    override val tableName: String = "Table name",
    override val doYouWantToAbortCheck: String = "Do you want to abort check",
    override val itemAlreadyExist: String = "This Item Is Exist, Do You Want Add Another Qty ?",
    override val item: String = "item",
    override val quantity: String = "Qty",
    override val price: String = "Price",
    override val totalItems: String = "Total Items",
    override val totalCheckPrice: String = "Total Check price",
    override val fire: String = "Fire",
    override val enterComment: String = "Enter comment",
    override val checkNumber: String = "Check number",
    override val no: String = "No",
    override val yes: String = "Yes",
    override val enterPrice: String = "Enter your price",
    override val openPrice: String = "Open Price",
    override val fireSettle: String = "Fire & Settle",
    override val firePrint: String = "Fire & Print",
    override val fireHold: String = "Fire & Hold",
    override val orderNumber: String = "Order Number",
    override val vat: String = "Total Vat 14%",
    override val service: String = "Total Service 12%",
    override val splitCheck: String = "Split Check",
    override val unSplitCheck: String = "UnSplit Check",
    override val combineCheck: String = "Combine Check",
    override val unCombineCheck: String = "UnCombine Check",
    override val shareItem: String = "Share Item",
    override val moveItem: String = "Move Item",
    override val moveItemToNewCheck: String = "Move Item To New Check",
    override val splitAndPay: String = "Split And Pay",
    override val disableTable: String = "Disable Table",
    override val enableTable: String = "Enable Table",
    override val moveTableChecks: String = "Move Table Checks",
    override val void: String = "Void",
) : IStringResources
package domain.usecase

import data.util.AppLanguage
import domain.util.ValidationException
import util.LanguageCode
import util.LocalizationManager

class ValidationAuthUseCase {

    fun validateAttendance(username: String, password: String) {
        val localMessage = LocalizationManager.getStringResources(
            LanguageCode.entries.find { code ->
                code.value == AppLanguage.code.value
            } ?: LanguageCode.EN
        )
        if (username.isEmpty() && password.isEmpty()) throw ValidationException(localMessage.authEmptyException)
        if (username.isEmpty()) throw ValidationException(localMessage.userNameEmptyException)
        if (password.isEmpty()) throw ValidationException(localMessage.passwordEmptyException)
    }


    fun validatePermissionPasscode(passcode: String) {
        val localMessage = LocalizationManager.getStringResources(
            LanguageCode.entries.find { code ->
                code.value == AppLanguage.code.value
            } ?: LanguageCode.EN
        )
        if (passcode.isEmpty()) throw ValidationException(localMessage.passcodeEmptyException)
    }
}
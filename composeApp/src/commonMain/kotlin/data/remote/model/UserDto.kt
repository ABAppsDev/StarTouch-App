package data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String? = null,
    val code: Int? = null,
    val password: String? = null,
    val userName: String? = null,
    val userClassID: Int? = null,
    val myLanguage: String? = null,
    @SerialName("tital")
    val title: String? = null,
    val fullName: String? = null,
    val address: String? = null,
    val jop: String? = null,
    val mobile: String? = null,
    val email: String? = null,
    val passCode: String? = null,
    val phone: String? = null,
    val active: Boolean? = null,
    val createDate: LocalDateTime? = null,
    val modifiedDate: LocalDateTime? = null,
    val userID: Int? = null,
    val restIDActive: Int? = null,
    val isWaiter: Boolean? = null,
    val refCode: String? = null,
    val isUpdated: Boolean? = null,
)
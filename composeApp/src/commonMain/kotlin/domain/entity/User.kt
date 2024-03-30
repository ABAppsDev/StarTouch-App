package domain.entity

import kotlinx.datetime.LocalDateTime

data class User(
    val name: String,
    val code: Int,
    val password: String,
    val userName: String,
    val userClassID: Int,
    val language: String,
    val title: String,
    val fullName: String,
    val address: String?,
    val jop: String?,
    val mobile: String?,
    val email: String?,
    val passCode: String,
    val phone: String?,
    val active: Boolean,
    val createDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val adminID: Int,
    val restIDActive: Int,
    val isWaiter: Boolean,
)
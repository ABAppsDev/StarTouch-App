package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val data: T?,
    val isSuccess: Boolean,
    val status: ResponseStatusDto,
)

@Serializable
data class ResponseStatusDto(
    val messageError: String? = null,
    val messageSuccess: String? = null,
    val code: Int? = null,
)


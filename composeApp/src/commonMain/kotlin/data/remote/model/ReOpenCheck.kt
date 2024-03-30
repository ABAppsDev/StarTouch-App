package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ReOpenCheck(
    val id: Long,
    val checkSerial: Int,
    val covers: Int,
    val myTable: String,
    val myStatus: String,
    val myDateTime: String? = null,
    val createDate: String? = null,
    val serverID: Int,
)
package data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Int? = null,
    val code: String? = null,
    val name: String? = null,
    val name2: String? = null,
    val barCode: String? = null,
    val crossCode: Int? = null,
    val taxable: Boolean? = null,
    @SerialName("assimbly")
    val assembly: Boolean? = null,
    @SerialName("subCategryId")
    val subCategoryId: Int? = null,
    val isModifier: Boolean? = null,
    val standAlone: Boolean? = null,
    val printOnChick: Boolean? = null,
    val printOnReport: Boolean? = null,
    val followItem: Boolean? = null,
    val imageItem: ByteArray? = null,
    val backColor: String? = null,
    val fontColor: String? = null,
    val cost: Float? = null,
    val openPrice: Boolean? = null,
    val staticPrice: Float? = null,
    val description1: String? = null,
    val description2: String? = null,
    val description3: String? = null,
    val description4: String? = null,
    val modPrice0: Boolean? = null,
    val itemFont: String? = null,
    val useItemTimer: Boolean? = null,
    val itemTimerValue: Int? = null,
    val active: Boolean? = null,
    val createDate: LocalDateTime? = null,
    val modifiedDate: LocalDateTime? = null,
    val userId: Int? = null,
    val noServiceCharge: Boolean? = null,
    val marketPrice: Boolean? = null,
    val itemTrack: Boolean? = null,
    val printItemOnCheck: Boolean? = null,
    val isParent: Boolean? = null,
    val restIdActive: Int? = null,
    val openFood: Boolean? = null,
    val modPrice: Float? = null,
    val prePaidCard: Boolean? = null,
    val modifierCount: Int? = null
)
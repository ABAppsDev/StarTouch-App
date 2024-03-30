package domain.gateway

import domain.entity.OpenCheck
import domain.entity.OpenNewCheck


interface IChecksGateway {
    suspend fun openNewCheck(openNewCheck: OpenNewCheck): OpenCheck
    //suspend fun addItemsToCheck(checkID: Long, userID: String, items: List<FireItems>)
}
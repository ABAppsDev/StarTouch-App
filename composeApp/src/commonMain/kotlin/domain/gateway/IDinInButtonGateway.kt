package domain.gateway

import domain.entity.FireItems

interface IDinInButtonGateway {
    suspend fun getTableItems(
        checkId: Long,
        tableId: Long,
    ): List<FireItems>

    suspend fun moveItems(
        fromCheckId: Long,
        toCheckId: Long,
        itemSerials: List<Int>,
    )
}
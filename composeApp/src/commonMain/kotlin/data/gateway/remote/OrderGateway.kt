package data.gateway.remote

import data.remote.mapper.toEntity
import data.remote.model.ItemDto
import data.remote.model.PresetsDto
import data.remote.model.ServerResponse
import domain.entity.Item
import domain.entity.Preset
import domain.gateway.IOrderGateway
import domain.util.NotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class OrderGateway(client: HttpClient) : BaseGateway(client), IOrderGateway {
    override suspend fun getAllPresets(outletID: Int, restID: Int, checkId: Long): List<Preset> {
        return tryToExecute<ServerResponse<List<PresetsDto>>> {
            get("/presets") {
                parameter("outletID", outletID)
                parameter("restID", restID)
                parameter("checkId", checkId)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Presets not found")
    }

    override suspend fun getAllItems(
        outletID: Int,
        restID: Int,
        presetID: Int,
        checkId: Long,
        priceLvlId: Int
    ): List<Item> {
        return tryToExecute<ServerResponse<List<ItemDto>>> {
            get("/presets/$presetID") {
                parameter("outletID", outletID)
                parameter("restID", restID)
                parameter("checkId", checkId)
                parameter("priceLvlId", priceLvlId)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Items not found")
    }

    override suspend fun checkItemHasChildren(
        restID: Int,
        itemID: Int,
        priceLvlId: Int
    ): List<Item> {
        return tryToExecute<ServerResponse<List<ItemDto>>> {
            get("/item/child/$itemID") {
                parameter("restID", restID)
                parameter("priceLvlId", priceLvlId)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Item not found")
    }

    override suspend fun checkItemHasModifiers(
        restID: Int,
        itemID: Int,
        priceLvlId: Int
    ): List<Item> {
        return tryToExecute<ServerResponse<List<ItemDto>>> {
            get("/item/$itemID") {
                parameter("restID", restID)
                parameter("priceLvlId", priceLvlId)
            }
        }.data?.map { it.toEntity() } ?: throw NotFoundException("Item not found")
    }
}
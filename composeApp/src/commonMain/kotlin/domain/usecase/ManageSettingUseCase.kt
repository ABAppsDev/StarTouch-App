package domain.usecase

import domain.gateway.local.ILocalConfigurationGateway

class ManageSettingUseCase(private val localGateway: ILocalConfigurationGateway) {

    suspend fun getOutletId(): Int = localGateway.getOutletId()

    suspend fun getRestId(): Int = localGateway.getRestId()

    suspend fun getWorkStationId(): Int = localGateway.getWorkStationId()

    suspend fun getIsCallCenter(): Boolean = localGateway.getIsCallCenter()

    fun getApiUrl(): String = localGateway.getApiUrl()

    suspend fun getDinInMainRoomId(): Int = localGateway.getDinInMainRoomId()

    suspend fun getIsBackToHome(): Boolean = localGateway.getIsBackToHome()

    suspend fun saveOutletId(outletId: Int) = localGateway.saveOutletId(outletId)

    suspend fun saveRestId(restId: Int) = localGateway.saveRestId(restId)

    suspend fun saveWorkStationId(wsId: String) =
        if (wsId.trim() == "") localGateway.saveWorkStationId(0) else localGateway.saveWorkStationId(
            wsId.toInt()
        )

    suspend fun saveApiUrl(url: String) = localGateway.saveApiUrl(url)

    suspend fun saveDinInMainRoomId(id: Int) = localGateway.saveDinInMainRoomId(id)

    suspend fun saveIsBackToHome(isBackToHome: Boolean) =
        localGateway.saveIsBackToHome(isBackToHome)

    suspend fun saveIsCallCenter(isCallCenter: Boolean) =
        localGateway.saveIsCallCenter(isCallCenter)
}
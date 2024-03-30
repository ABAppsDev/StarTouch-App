package domain.gateway.local

interface ILocalConfigurationGateway {
    suspend fun getLanguageCode(): String
    suspend fun getOutletId(): Int
    suspend fun getRestId(): Int
    suspend fun getWorkStationId(): Int
    suspend fun getIsCallCenter(): Boolean
    fun getApiUrl(): String
    suspend fun getDinInMainRoomId(): Int
    suspend fun getHasMoreButton(): Boolean
    suspend fun getIsBackToHome(): Boolean

    suspend fun saveOutletId(outletId: Int)
    suspend fun saveRestId(restId: Int)
    suspend fun saveWorkStationId(wsId: Int)
    suspend fun saveApiUrl(url: String)
    suspend fun saveDinInMainRoomId(id: Int)
    suspend fun saveHasMoreButton(hasMoreButton: Boolean)
    suspend fun saveIsBackToHome(isBackToHome: Boolean)
    suspend fun saveIsCallCenter(isCallCenter: Boolean)
}
package data.gateway.local

import data.local.model.AppConfigurationCollection
import data.util.AppLanguage
import domain.gateway.local.ILocalConfigurationGateway
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.first

class LocalConfigurationGateway(private val realm: Realm) : ILocalConfigurationGateway {

    init {
        createUserConfiguration()
    }

    private fun createUserConfiguration() {
        realm.writeBlocking {
            copyToRealm(AppConfigurationCollection().apply {
                id = CONFIGURATION_ID
            })
        }
    }

    override suspend fun getLanguageCode(): String {
        return AppLanguage.code.first()
    }

    override suspend fun getOutletId(): Int {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.outletId ?: 0
    }

    override suspend fun getRestId(): Int {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.restId ?: 0
    }

    override suspend fun getWorkStationId(): Int {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.workStationId ?: 0
    }

    override suspend fun getIsCallCenter(): Boolean {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.isCallCenter ?: false
    }

    override fun getApiUrl(): String {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.apiUrl ?: ""
    }

    override suspend fun getDinInMainRoomId(): Int {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.dinInMainRoomId ?: 0
    }

    override suspend fun getHasMoreButton(): Boolean {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.hasMoreButtonClick ?: false
    }

    override suspend fun getIsBackToHome(): Boolean {
        return realm.query(
            AppConfigurationCollection::class,
            "$ID == $CONFIGURATION_ID"
        ).first().find()?.isReturnedHomeAfterFinishOrder ?: true
    }

    override suspend fun saveOutletId(outletId: Int) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.outletId = outletId
        }
    }

    override suspend fun saveRestId(restId: Int) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.restId = restId
        }
    }

    override suspend fun saveWorkStationId(wsId: Int) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.workStationId = wsId
        }
    }

    override suspend fun saveIsCallCenter(isCallCenter: Boolean) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.isCallCenter = isCallCenter
        }
    }

    override suspend fun saveApiUrl(url: String) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.apiUrl = url
        }
    }

    override suspend fun saveDinInMainRoomId(id: Int) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.dinInMainRoomId = id
        }
    }

    override suspend fun saveHasMoreButton(hasMoreButton: Boolean) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.hasMoreButtonClick = hasMoreButton
        }
    }

    override suspend fun saveIsBackToHome(isBackToHome: Boolean) {
        realm.write {
            query(AppConfigurationCollection::class, "$ID == $CONFIGURATION_ID").first()
                .find()?.isReturnedHomeAfterFinishOrder = isBackToHome
        }
    }

    companion object {
        private const val CONFIGURATION_ID = 0
        private const val ID = "id"
    }
}
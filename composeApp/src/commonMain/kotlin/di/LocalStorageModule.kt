package di

import data.local.model.AppConfigurationCollection
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val localStorageModule = module {

    single {
        RealmConfiguration.Builder(
            schema = setOf(
                AppConfigurationCollection::class,
            )
        ).compactOnLaunch().deleteRealmIfMigrationNeeded().build()
    }
    single { Realm.open(configuration = get<RealmConfiguration>()) }
}
package io.github.landrynorris.multifactor.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.NameKeystoreAlias
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.models.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordRepository(private val database: AppDatabase, private val crypto: Crypto) {

    fun getPasswordsFlow(): Flow<List<PasswordModel>> {
        return database.passwordQueries.selectAll().asFlow().mapToList(Dispatchers.IO).map { entries ->
            entries.map { entry ->
                entry.toModel(crypto)
            }
        }
    }

    fun insertPassword(model: PasswordModel) {
        val name = crypto.encrypt(model.name.encodeToByteArray(), NameKeystoreAlias)
        database.passwordQueries.insertPassword(null, name.data, name.iv, model.salt,
            model.encryptedValue, model.domain?.encodeToByteArray(), model.appId)
    }

    fun insertPasswords(vararg models: PasswordModel) {
        database.transaction {
            models.forEach {
                insertPassword(it)
            }
        }
    }

    fun deletePassword(model: PasswordModel) {
        database.passwordQueries.deleteById(model.id)
    }
}

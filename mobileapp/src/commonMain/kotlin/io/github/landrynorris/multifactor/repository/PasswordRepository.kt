package io.github.landrynorris.multifactor.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.models.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasswordRepository(private val database: AppDatabase) {

    fun getPasswordsFlow(): Flow<List<PasswordModel>> {
        return database.passwordQueries.selectAll().asFlow().mapToList().map { entries ->
            entries.map { entry ->
                entry.toModel()
            }
        }
    }

    fun insertPassword(model: PasswordModel) {
        database.passwordQueries.insertPassword(null, model.name, model.salt,
            model.encryptedValue, model.domain)
    }
}
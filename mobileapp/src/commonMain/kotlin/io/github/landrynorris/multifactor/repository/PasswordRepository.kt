package io.github.landrynorris.multifactor.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.landrynorris.multifactor.OtpDatabase
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.models.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PasswordRepository: KoinComponent {
    private val repository by inject<OtpDatabase>()

    fun getPasswordsFlow(): Flow<List<PasswordModel>> {
        return repository.passwordQueries.selectAll().asFlow().mapToList().map { entries ->
            entries.map { entry ->
                entry.toModel()
            }
        }
    }

    fun insertPassword(model: PasswordModel) {
        repository.passwordQueries.insertPassword(null, model.name, model.salt, model.encryptedValue)
    }
}
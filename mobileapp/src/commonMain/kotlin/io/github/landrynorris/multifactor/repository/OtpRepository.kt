package io.github.landrynorris.multifactor.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.landrynorris.multifactor.OtpDatabase
import io.github.landrynorris.multifactor.OtpEntry
import io.github.landrynorris.multifactor.models.OtpModel
import io.github.landrynorris.multifactor.models.toEntry
import io.github.landrynorris.multifactor.models.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OtpRepository: KoinComponent {
    private val database by inject<OtpDatabase>()

    fun createOtp(otp: OtpModel) {
        val entry = otp.toEntry()
        database.otpQueries.insertOtp(id = null, secret = entry.secret, type = entry.type, name = entry.name)
    }

    fun getOtpModelFlow(): Flow<List<OtpModel>> {
        return database.otpQueries.selectAll().asFlow().mapToList().map { entries ->
            entries.map { entry -> entry.toModel() }
        }
    }
}
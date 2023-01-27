package io.github.landrynorris.multifactor.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.landrynorris.database.AppDatabase
import io.github.landrynorris.multifactor.models.OtpModel
import io.github.landrynorris.multifactor.models.toEntry
import io.github.landrynorris.multifactor.models.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OtpRepository(private val database: AppDatabase) {

    fun createOtp(otp: OtpModel) {
        val entry = otp.toEntry()
        database.otpQueries.insertOtp(id = null, secret = entry.secret, type = entry.type,
            name = entry.name, count = entry.count)
    }

    fun getOtpModelFlow(): Flow<List<OtpModel>> {
        return database.otpQueries.selectAll().asFlow().mapToList().map { entries ->
            entries.map { entry -> entry.toModel() }
        }
    }

    fun setHotpCount(id: Long, count: Long) {
        database.otpQueries.setCountById(count = count, id = id)
    }

    fun delete(item: OtpModel) {
        database.otpQueries.deleteById(item.id)
    }
}
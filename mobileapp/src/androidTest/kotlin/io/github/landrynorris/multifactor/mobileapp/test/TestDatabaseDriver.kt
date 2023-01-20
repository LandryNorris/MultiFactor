package io.github.landrynorris.multifactor.mobileapp.test

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.github.landrynorris.database.AppDatabase

actual fun createInMemoryTestDriver(): SqlDriver {
    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        AppDatabase.Schema.create(this)
    }
}

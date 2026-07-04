package io.github.landrynorris.multifactor.mobileapp.test

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver.Companion.IN_MEMORY
import io.github.landrynorris.database.AppDatabase

actual fun createInMemoryTestDriver(): SqlDriver {
    return JdbcSqliteDriver(IN_MEMORY).also {
        AppDatabase.Schema.create(it)
    }
}

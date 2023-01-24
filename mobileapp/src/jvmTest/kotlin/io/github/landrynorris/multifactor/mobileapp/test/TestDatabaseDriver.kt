package io.github.landrynorris.multifactor.mobileapp.test

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import io.github.landrynorris.database.AppDatabase

actual fun createInMemoryTestDriver(): SqlDriver {
    return JdbcSqliteDriver(IN_MEMORY).also {
        AppDatabase.Schema.create(it)
    }
}

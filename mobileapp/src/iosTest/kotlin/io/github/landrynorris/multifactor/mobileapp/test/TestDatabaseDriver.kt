package io.github.landrynorris.multifactor.mobileapp.test

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.drivers.native.NativeSqliteDriver
import io.github.landrynorris.database.AppDatabase

actual fun createInMemoryTestDriver(): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Schema, "test.db")
}

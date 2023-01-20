package io.github.landrynorris.multifactor.mobileapp.test

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.landrynorris.database.AppDatabase

actual fun createInMemoryTestDriver(): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Schema, "test.db")
}

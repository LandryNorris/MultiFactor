package io.github.landrynorris.multifactor.mobileapp.test

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import io.github.landrynorris.database.AppDatabase

var index = 0

actual fun createInMemoryTestDriver(): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Schema, "test.db")
}

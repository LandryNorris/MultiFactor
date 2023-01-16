package io.github.landrynorris.multifactor.mobileapp.test

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import io.github.landrynorris.database.AppDatabase

var index = 0

actual fun createInMemoryTestDriver(): SqlDriver {
    val schema = AppDatabase.Schema
    return NativeSqliteDriver(DatabaseConfiguration(name = "test-$index.db",
        version = schema.version,
        create = { connection ->
            wrapConnection(connection) { schema.create(it) }
        },
        upgrade = { connection, oldVersion, newVersion ->
            wrapConnection(connection) {
                schema.migrate(it, oldVersion, newVersion)
            }
        },
        inMemory = true
    ))
}

package io.github.landrynorris.app.mobileapp.test

import app.cash.sqldelight.db.SqlDriver

expect fun createInMemoryTestDriver(): SqlDriver

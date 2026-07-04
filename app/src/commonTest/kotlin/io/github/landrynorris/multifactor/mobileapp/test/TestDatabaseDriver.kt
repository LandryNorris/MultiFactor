package io.github.landrynorris.multifactor.mobileapp.test

import app.cash.sqldelight.db.SqlDriver

expect fun createInMemoryTestDriver(): SqlDriver

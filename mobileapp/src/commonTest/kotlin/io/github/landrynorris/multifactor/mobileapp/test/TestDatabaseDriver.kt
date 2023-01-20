package io.github.landrynorris.multifactor.mobileapp.test

import com.squareup.sqldelight.db.SqlDriver

expect fun createInMemoryTestDriver(): SqlDriver

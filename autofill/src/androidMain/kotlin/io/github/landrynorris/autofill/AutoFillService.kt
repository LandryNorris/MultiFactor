package io.github.landrynorris.autofill

import android.os.CancellationSignal
import android.service.autofill.*
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.landrynorris.database.AppDatabase

class AutoFillService: AutofillService() {

    override fun onFillRequest(request: FillRequest,
                               cancellationSignal: CancellationSignal, callback: FillCallback) {

    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {

    }

    private val database by lazy {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "multifactor-database")
        AppDatabase(driver)
    }
}
package io.github.landrynorris.autofill

import android.os.CancellationSignal
import android.service.autofill.*
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.github.landrynorris.autofill.parser.AssistStructureParser
import io.github.landrynorris.autofill.parser.inlineSpec
import io.github.landrynorris.autofill.response.AutoFillResponse
import io.github.landrynorris.database.AppDatabase

class AutoFillService: AutofillService() {

    override fun onFillRequest(request: FillRequest,
                               cancellationSignal: CancellationSignal, callback: FillCallback) {
        val structure = request.fillContexts.last().structure
        val parsedStructure = AssistStructureParser().parse(structure)

        val response = AutoFillResponse.create(this, parsedStructure, request.inlineSpec)

        callback.onSuccess(response)
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        val structure = request.fillContexts.last().structure
        val parsedStructure = AssistStructureParser().parse(structure)

        println("Parsed structure for save is $parsedStructure")
    }

    private val database by lazy {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "multifactor-database")
        AppDatabase(driver)
    }
}

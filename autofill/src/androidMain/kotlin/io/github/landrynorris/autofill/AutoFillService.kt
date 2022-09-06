package io.github.landrynorris.autofill

import android.os.CancellationSignal
import android.service.autofill.*

class AutoFillService: AutofillService() {
    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {

    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {

    }

}
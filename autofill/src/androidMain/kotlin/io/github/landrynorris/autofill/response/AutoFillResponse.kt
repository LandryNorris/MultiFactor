package io.github.landrynorris.autofill.response

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.inline.InlinePresentationSpec
import io.github.landrynorris.autofill.parser.ParsedStructure
import io.github.landrynorris.autofill.ui.createInline
import io.github.landrynorris.autofill.ui.createText

object AutoFillResponse {
    fun create(context: Context, parsed: ParsedStructure,
               imeSpec: InlinePresentationSpec?): FillResponse {
        val response = FillResponse.Builder()

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        if(parsed.passwordNode != null) {
            response.addDataset(createDataSet(parsed.passwordNode, pendingIntent,
                context, "Password", "password", imeSpec))
        }

        if(parsed.usernameNode != null) {
            response.addDataset(createDataSet(parsed.usernameNode, pendingIntent,
                context, "Username", "username", imeSpec))
        }

        return response.build()
    }

    private fun createDataSet(id: AutofillId, pendingIntent: PendingIntent, context: Context,
                              title: String, value: String, imeSpec: InlinePresentationSpec?): Dataset {
        val dataset = Dataset.Builder()
        val usernameView = createText(context, title)
        if(imeSpec != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val slice = createInline(pendingIntent, imeSpec, title)
            dataset.setValue(id,
                AutofillValue.forText(value),
                usernameView, slice)
        } else {
            dataset.setValue(id,
                AutofillValue.forText(value),
                usernameView)
        }

        return dataset.build()
    }
}
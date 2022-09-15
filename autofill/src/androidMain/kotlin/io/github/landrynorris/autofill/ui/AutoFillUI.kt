package io.github.landrynorris.autofill.ui

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.slice.Slice
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Build
import android.service.autofill.FillResponse
import android.service.autofill.InlinePresentation
import android.widget.RemoteViews
import android.widget.inline.InlinePresentationSpec
import androidx.annotation.RequiresApi
import androidx.autofill.inline.v1.InlineSuggestionUi

@RequiresApi(Build.VERSION_CODES.R)
internal fun createInline(intent: PendingIntent, imeSpec: InlinePresentationSpec,
                 text: String, icon: Icon? = null): InlinePresentation =
    InlinePresentation(createSlice(text, icon = icon, intent = intent), imeSpec, false)

@SuppressLint("RestrictedApi")
@RequiresApi(Build.VERSION_CODES.R)
private fun createSlice(text: String, subtitle: String = "", description: String = "",
               icon: Icon? = null,
               intent: PendingIntent): Slice {
    val builder = InlineSuggestionUi.newContentBuilder(intent)
        .setContentDescription(description)

    if(text.isNotBlank()) builder.setTitle(text)
    if(subtitle.isNotBlank()) builder.setSubtitle(subtitle)
    if(icon != null) builder.setStartIcon(icon)

    return builder.build().slice
}

internal fun createText(context: Context, text: String): RemoteViews =
    RemoteViews(context.packageName, android.R.layout.simple_list_item_1).also {
        it.setTextViewText(android.R.id.text1, text)
    }

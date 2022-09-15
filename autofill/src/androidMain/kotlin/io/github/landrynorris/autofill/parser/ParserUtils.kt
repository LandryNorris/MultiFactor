package io.github.landrynorris.autofill.parser

import android.app.assist.AssistStructure
import android.app.assist.AssistStructure.ViewNode
import android.os.Build
import android.service.autofill.FillRequest
import android.text.InputType
import java.util.*

val FillRequest.inlineSpec get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    inlineSuggestionsRequest?.inlinePresentationSpecs?.last()
} else {
    null
}

val ViewNode.clues: List<String> get() {
    val hints = mutableListOf(text.toString(), idEntry.toString(), hint)

    autofillHints?.let { hints.addAll(it) }
    autofillOptions?.let { options -> hints.addAll(options.map { it.toString() }) }
    htmlInfo?.attributes?.let { attrs -> hints.addAll(attrs.map { it.second }) }

    return hints.filterNotNull()
}

fun ViewNode.containsKeywords(keywords: List<String>) =
    clues.any { clue -> keywords.any { clue.contains(it) } }

val ViewNode.children get() = (0 until childCount).map { getChildAt(it) }

fun ViewNode.htmlAttr(name: String) = htmlInfo?.attributes?.find { it.first == name }?.second

val ViewNode.htmlTag get() = htmlInfo?.tag?.lowercase(Locale.ENGLISH)

val ViewNode.isEditText get() = inputType and InputType.TYPE_CLASS_TEXT > 0

val ViewNode.isHtmlInputField get() = htmlTag == "input"

val ViewNode.isButton get() = when {
    className?.contains("Button") == true -> true
    htmlTag == "button" -> true
    !isHtmlInputField -> false
    else -> when(htmlAttr("type")) {
        "submit" -> true
        "button" -> true
        else -> false
    }
}

val AssistStructure.rootNodes get() =
    (0 until windowNodeCount).map { getWindowNodeAt(0).rootViewNode }

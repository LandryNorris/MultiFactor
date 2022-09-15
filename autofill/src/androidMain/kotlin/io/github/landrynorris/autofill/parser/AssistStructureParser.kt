package io.github.landrynorris.autofill.parser

import android.app.assist.AssistStructure
import android.app.assist.AssistStructure.ViewNode
import android.view.View
import android.view.autofill.AutofillId

class AssistStructureParser {
    private val passwordKeywords = listOf(View.AUTOFILL_HINT_PASSWORD, "password")
    private val usernameKeywords = listOf(View.AUTOFILL_HINT_USERNAME,
        View.AUTOFILL_HINT_EMAIL_ADDRESS, "username", "account name", "email", "user name",
        "account_name")

    fun parse(structure: AssistStructure): ParsedStructure {
        val rootNodes = structure.rootNodes
        val passwordIds = rootNodes.mapNotNull { it.findMatchingKeywords(passwordKeywords) }
        val usernameIds = rootNodes.mapNotNull { it.findMatchingKeywords(usernameKeywords) }
        val focusedId = rootNodes.firstNotNullOfOrNull { it.focusedChild() }

        println("Focused view: $focusedId")
        println("Found ${usernameIds.size} username nodes")
        println("Found ${passwordIds.size} password nodes")

        return ParsedStructure(passwordIds.firstOrNull(), usernameIds.firstOrNull())
    }

    private fun ViewNode.focusedChild(): AutofillId? {
        if(this.isFocused) return autofillId

        return children.firstNotNullOfOrNull {
            it.focusedChild()
        }
    }

    private fun ViewNode.findMatchingKeywords(keywords: List<String>): AutofillId? {
        println("Clues for $autofillId are ${clues.joinToString(", ")}")
        if(doesEditTextMatch(this, keywords) || doesHtmlInputFieldMatch(this, keywords))
            return this.autofillId

        return children.firstNotNullOfOrNull {
            it.findMatchingKeywords(keywords)
        }
    }

    private fun doesEditTextMatch(node: ViewNode, keywords: List<String>): Boolean =
        node.isEditText && node.containsKeywords(keywords) && node.autofillId != null

    private fun doesHtmlInputFieldMatch(node: ViewNode, keywords: List<String>): Boolean =
        node.isHtmlInputField && node.containsKeywords(keywords) && node.autofillId != null
}

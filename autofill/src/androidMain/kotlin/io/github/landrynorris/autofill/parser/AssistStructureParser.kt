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
        val pkg = structure.activityComponent.packageName
        val focusedRoot = structure.focusedRoot
            ?: return ParsedStructure()
        val passwordId = focusedRoot.findMatchingKeywords(passwordKeywords)
        val usernameId = focusedRoot.findMatchingKeywords(usernameKeywords)
        val focusedId = focusedRoot.focusedChild()

        return ParsedStructure(pkg = pkg, passwordNode = passwordId,
            usernameNode = usernameId, focused = focusedId)
    }

    private fun ViewNode.focusedChild(): AutofillId? {
        if(this.isFocused) return autofillId

        return children.firstNotNullOfOrNull {
            it.focusedChild()
        }
    }

    private fun ViewNode.findMatchingKeywords(keywords: List<String>): AutofillId? {
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

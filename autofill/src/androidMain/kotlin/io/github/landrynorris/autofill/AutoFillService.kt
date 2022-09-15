package io.github.landrynorris.autofill

import android.app.assist.AssistStructure
import android.os.CancellationSignal
import android.service.autofill.*
import android.view.autofill.AutofillId
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.landrynorris.autofill.parser.AssistStructureParser
import io.github.landrynorris.autofill.parser.inlineSpec
import io.github.landrynorris.autofill.response.AutoFillResponse
import io.github.landrynorris.database.AppDatabase

class AutoFillService: AutofillService() {

    override fun onFillRequest(request: FillRequest,
                               cancellationSignal: CancellationSignal, callback: FillCallback) {
        val structure = request.fillContexts.last().structure
        val parsedStructure = AssistStructureParser().parse(structure)

        AutoFillResponse.create(this, parsedStructure, request.inlineSpec)
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        traverseStructure(request.fillContexts.last().structure)
    }

    private fun traverseStructure(structure: AssistStructure): AutofillId? {
        val windowNodes: List<AssistStructure.WindowNode> =
            structure.run {
                (0 until windowNodeCount).map { getWindowNodeAt(it) }
            }

        windowNodes.forEach { windowNode: AssistStructure.WindowNode ->
            val viewNode: AssistStructure.ViewNode? = windowNode.rootViewNode
            return traverseNode(viewNode)
        }
        return null
    }

    private fun traverseNode(viewNode: AssistStructure.ViewNode?): AutofillId? {
        if (viewNode?.autofillHints?.isNotEmpty() == true) {
            // If the client app provides autofill hints, you can obtain them using:
            // viewNode.getAutofillHints();
            println("Autofill hint: ${viewNode.autofillHints?.joinToString(", ")}")
            if(viewNode.autofillHints?.any { it.contains("password") } == true)
                return viewNode.autofillId
        } else {
            // Or use your own heuristics to describe the contents of a view
            // using methods such as getText() or getHint().
            println("No autofill hint: ${viewNode?.hint}")
        }

        val children: List<AssistStructure.ViewNode>? =
            viewNode?.run {
                (0 until childCount).map { getChildAt(it) }
            }

        children?.forEach { childNode: AssistStructure.ViewNode ->
            return traverseNode(childNode)
        }
        return null
    }

    private val database by lazy {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "multifactor-database")
        AppDatabase(driver)
    }
}
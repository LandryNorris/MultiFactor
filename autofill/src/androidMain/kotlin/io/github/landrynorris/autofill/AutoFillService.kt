package io.github.landrynorris.autofill

import android.app.assist.AssistStructure
import android.os.CancellationSignal
import android.service.autofill.*
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.landrynorris.database.AppDatabase

class AutoFillService: AutofillService() {

    override fun onFillRequest(request: FillRequest,
                               cancellationSignal: CancellationSignal, callback: FillCallback) {
        traverseStructure(request.fillContexts.last().structure)
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        traverseStructure(request.fillContexts.last().structure)
    }

    private fun traverseStructure(structure: AssistStructure) {
        val windowNodes: List<AssistStructure.WindowNode> =
            structure.run {
                (0 until windowNodeCount).map { getWindowNodeAt(it) }
            }

        windowNodes.forEach { windowNode: AssistStructure.WindowNode ->
            val viewNode: AssistStructure.ViewNode? = windowNode.rootViewNode
            traverseNode(viewNode)
        }
    }

    private fun traverseNode(viewNode: AssistStructure.ViewNode?) {
        if (viewNode?.autofillHints?.isNotEmpty() == true) {
            // If the client app provides autofill hints, you can obtain them using:
            // viewNode.getAutofillHints();
            println("Autofill hint: ${viewNode.autofillHints?.joinToString(", ")}")
        } else {
            // Or use your own heuristics to describe the contents of a view
            // using methods such as getText() or getHint().
            println("No autofill hint")
        }

        val children: List<AssistStructure.ViewNode>? =
            viewNode?.run {
                (0 until childCount).map { getChildAt(it) }
            }

        children?.forEach { childNode: AssistStructure.ViewNode ->
            traverseNode(childNode)
        }
    }


    private val database by lazy {
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "multifactor-database")
        AppDatabase(driver)
    }
}
package io.github.landrynorris.app.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.app.Directories
import io.github.landrynorris.app.export.PasswordExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Clock

interface PasswordExportLogic {
    val state: StateFlow<PasswordExportData>

    fun setExportPassword(password: String)
    suspend fun export()
    fun show()
    fun dismiss()
}

class PasswordExportComponent(
    val context: ComponentContext,
    val exporter: PasswordExporter,
): KoinComponent, ComponentContext by context, PasswordExportLogic {
    override val state = MutableStateFlow(PasswordExportData())
    private val directories: Directories by inject()

    override fun setExportPassword(password: String) {
        state.update { it.copy(encryptionPassword = password) }
    }

    override suspend fun export() {
        val password = state.value.encryptionPassword

        val fileContents = exporter
            .createExportFileContents(password.encodeToByteArray())

        saveExportFile(fileContents)
        state.update { PasswordExportData() }
    }

    override fun show() {
        state.update { it.copy(isShowing = true) }
    }

    override fun dismiss() {
        state.update { it.copy(isShowing = false) }
    }

    private fun saveExportFile(contents: String) {
        val name = "MultiFactorExport-" + Clock.System.now().toEpochMilliseconds() + ".ex"
        val path = Path(directories.downloadsDirectory, name)

        SystemFileSystem.sink(path).buffered().use { sink ->
            sink.writeString(contents)
        }
    }
}

data class PasswordExportData(
    val encryptionPassword: String = "",
    val isShowing: Boolean = false,
)

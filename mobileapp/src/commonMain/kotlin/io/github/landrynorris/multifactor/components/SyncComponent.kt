package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.multifactor.sync.client.CheckClient
import io.github.landrynorris.multifactor.sync.client.SyncClient
import io.github.landrynorris.multifactor.sync.server.createSyncServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

interface SyncLogic {
    val state: MutableStateFlow<SyncState>

    fun startServer()
    fun connect()
    fun enterCode(code: String)
}

class SyncComponent(val context: ComponentContext): ComponentContext by context, SyncLogic {

    override val state = MutableStateFlow(SyncState())

    override fun startServer() {
        val code = Random.nextInt(10000).toString().padStart(4, '0')
        createSyncServer(code)
        state.update { it.copy(serverRunning = true, serverCode = code) }
    }

    override fun connect() {
        val code = state.value.enteredCode
        val syncClient = SyncClient()
        val client = CheckClient()
        CoroutineScope(Dispatchers.Default).launch {
            client.findServerIpPortPair {
                syncClient.checkServer(it, code)
            }
        }
    }

    override fun enterCode(code: String) {
        state.update { it.copy(enteredCode = code) }
    }
}

data class SyncState(val serverRunning: Boolean = false,
                     val serverCode: String? = null,
                     val enteredCode: String = "")

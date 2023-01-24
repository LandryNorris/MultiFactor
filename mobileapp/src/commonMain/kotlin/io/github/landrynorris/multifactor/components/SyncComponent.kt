package io.github.landrynorris.multifactor.components

import com.arkivanov.decompose.ComponentContext
import io.github.landrynorris.encryption.Crypto
import io.github.landrynorris.multifactor.models.PasswordModel
import io.github.landrynorris.multifactor.repository.PasswordRepository
import io.github.landrynorris.multifactor.sync.client.CheckClient
import io.github.landrynorris.multifactor.sync.client.SyncClient
import io.github.landrynorris.multifactor.sync.server.createSyncServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

interface SyncLogic {
    val state: MutableStateFlow<SyncState>

    fun startServer()
    fun connect()
    fun enterCode(code: String)
}

class SyncComponent(val context: ComponentContext,
                    private val crypto: Crypto,
                    private val passwordRepository: PasswordRepository):
    ComponentContext by context, SyncLogic {

    override val state = MutableStateFlow(SyncState())
    private val syncClient = SyncClient()
    private val client = CheckClient()

    override fun startServer() {
        val code = Random.nextInt(10000).toString().padStart(4, '0')
        createSyncServer(code, ::onPasswordReceived)
        state.update { it.copy(serverRunning = true, serverCode = code) }
    }

    override fun connect() {
        val code = state.value.enteredCode
        CoroutineScope(Dispatchers.Default).launch {
            val ipPortPair = client.findServerIpPortPair {
                syncClient.checkServer(it, code)
            }
            println("Found connection at $ipPortPair")

            //ToDo: handle case where no connection was found.
            if(ipPortPair != null) syncPasswords(ipPortPair)
        }
    }

    private suspend fun syncPasswords(ipPortPair: String) {
        val passwords = passwordRepository.getPasswordsFlow().first()

        passwords.forEach {
            val decrypted = crypto.decrypt(it.encryptedValue, it.salt).decodeToString()
            println("Sending password $decrypted")
            syncClient.sharePassword(ipPortPair, it.name, decrypted)
        }
    }

    override fun enterCode(code: String) {
        state.update { it.copy(enteredCode = code) }
    }

    private fun onPasswordReceived(name: String, password: String) {
        println("Received password $password")
        val encrypted = crypto.encrypt(password.encodeToByteArray())
        val model = PasswordModel(-1L, name, encrypted.iv,
            encrypted.data, null, null)
        passwordRepository.insertPassword(model)
    }
}

data class SyncState(val serverRunning: Boolean = false,
                     val serverCode: String? = null,
                     val enteredCode: String = "")

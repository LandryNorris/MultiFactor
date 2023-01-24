package io.github.landrynorris.multifactor.sync.shared

import io.ktor.resources.*

@Resource("/share")
data class Share(val name: String, val password: String)

@Resource("/check")
data class Check(val code: String)

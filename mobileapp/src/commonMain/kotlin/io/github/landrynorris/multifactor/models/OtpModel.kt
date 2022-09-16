package io.github.landrynorris.multifactor.models

import io.github.landrynorris.database.OtpEntry
import io.github.landrynorris.otp.Hotp
import io.github.landrynorris.otp.Otp
import io.github.landrynorris.otp.Totp

data class OtpModel(val id: Long, val otp: Otp)

fun OtpModel.toEntry() = when(otp) {
    is Hotp -> OtpEntry(id, otp.secret, otp.name, otp.counter, 0L)
    is Totp -> OtpEntry(id, otp.secret, otp.name, null, 1L)
}

fun OtpEntry.toModel() = OtpModel(
    id = id,
    otp = if(type == 0L) Hotp(secret, name, count ?: 0) else Totp(secret, name)
)

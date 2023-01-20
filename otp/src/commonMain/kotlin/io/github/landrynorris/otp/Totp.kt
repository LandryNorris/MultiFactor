package io.github.landrynorris.otp

import kotlinx.datetime.Clock

data class Totp(override val secret: String, override val name: String,
                private val timeStep: Int = 30, override val codeLength: Int = 6):
    Otp(secret, name, codeLength) {
    private var overwrittenTime: Long? = null
    private val clock = Clock.System

    override fun getValue(): ByteArray {
        return t.toBytes()
    }

    fun setTime(newTimeSeconds: Long) {
        overwrittenTime = newTimeSeconds
    }

    val progress: Float get() {
        val time = getTimeMillis()
        val timeStepMs = timeStep*1000
        return (time % timeStepMs).toFloat() / timeStepMs
    }

    private val t: Long get() {
        val time = getTime()
        return time/timeStep
    }

    /**
     * returns the time IN SECONDS to use in the calculation of the pin.
     */
    private fun getTime(): Long {
        return if(overwrittenTime != null) {
            overwrittenTime!!
        } else {
            clock.now().epochSeconds
        }
    }

    private fun getTimeMillis(): Long {
        return if(overwrittenTime != null) {
            overwrittenTime!!*1000 //convert to ms
        } else {
            clock.now().toEpochMilliseconds()
        }
    }
}

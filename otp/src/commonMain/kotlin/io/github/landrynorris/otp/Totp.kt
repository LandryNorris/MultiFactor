package io.github.landrynorris.otp

import kotlinx.datetime.Clock

data class Totp(override val secret: ByteArray, override val name: String,
                private val timeStep: Int = 30, override val codeLength: Int = 6):
    Otp(secret, name, codeLength) {
    private var overwrittenTime: Long? = null
    private val clock = Clock.System

    constructor(secret: String, name: String, timeStep: Int = 30, codeLength: Int = 6):
            this(Base32.decode(secret), name, timeStep, codeLength)

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Totp

        if (!secret.contentEquals(other.secret)) return false
        if (name != other.name) return false
        if (timeStep != other.timeStep) return false
        if (codeLength != other.codeLength) return false
        if (overwrittenTime != other.overwrittenTime) return false
        if (clock != other.clock) return false

        return true
    }

    override fun hashCode(): Int {
        var result = secret.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + timeStep
        result = 31 * result + codeLength
        result = 31 * result + (overwrittenTime?.hashCode() ?: 0)
        result = 31 * result + clock.hashCode()
        return result
    }
}

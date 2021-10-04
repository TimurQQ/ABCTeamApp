package com.ilyasov.abcteamapp.entity

import com.ilyasov.abcteamapp.util.BASE_MOVE_SPEED

class Enemy(
    private val deadCallback: () -> Unit,
    private val updateCallback: (Float) -> Unit
) :
    Thread() {
    private var deadTimer: Long = BASE_MOVE_SPEED
    private var lastTime = System.nanoTime()
    var diedFlag = f

    override fun run() {
        while (!diedFlag) {
            val now = System.nanoTime()
            val deltaTime = now - lastTime
            lastTime = now
            if (deadTimer > 0) {
                deadTimer -= deltaTime
                updateCallback.invoke(deadTimer.toFloat() / BASE_MOVE_SPEED)
            } else {
                diedFlag = true
                break
            }
        }
        deadCallback.invoke()
    }
}
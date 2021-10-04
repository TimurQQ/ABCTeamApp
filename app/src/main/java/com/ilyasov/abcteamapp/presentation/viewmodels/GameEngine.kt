package com.ilyasov.abcteamapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.ilyasov.abcteamapp.entity.Enemy
import com.ilyasov.abcteamapp.entity.Hole
import com.ilyasov.abcteamapp.util.BASE_TIME_TO_RESPAWN
import com.ilyasov.abcteamapp.util.GAME_END_TIME
import kotlin.random.Random

class GameEngine : Thread() {
    var places: List<Hole> = listOf(
        Hole(0, null), Hole(1, null), Hole(2, null),
        Hole(3, null), Hole(4, null), Hole(5, null),
        Hole(6, null), Hole(7, null), Hole(8, null),
    )
    val deleteLiveData: MutableLiveData<Hole> = MutableLiveData()
    val animateLiveData: MutableLiveData<Pair<Hole, Float>> = MutableLiveData()
    val endGameLiveData: MutableLiveData<Any?> = MutableLiveData()
    val secondsLiveData: MutableLiveData<Long> = MutableLiveData()
    private var delayTime = BASE_TIME_TO_RESPAWN
    private var startGameTime = System.nanoTime()
    private var lastTime = startGameTime

    override fun run() {
        while (true) {
            val now = System.nanoTime()
            secondsLiveData.postValue(now - startGameTime)
            if (isTimeToEnd(now)) {
                endGameLiveData.postValue(null)
                break
            }
            val deltaTime = now - lastTime
            lastTime = now
            startDelayedRespawn(deltaTime)
        }
    }

    private fun isTimeToEnd(now: Long) = now - startGameTime >= GAME_END_TIME

    private fun startDelayedRespawn(delta: Long) {
        if (delayTime > 0) {
            delayTime -= delta
        } else {
            respawnEnemy()
            delayTime = BASE_TIME_TO_RESPAWN
        }
    }

    private fun respawnEnemy() {
        val pos = Random.nextInt(9)
        pos.let {
            places[pos].enemy = Enemy(
                deadCallback = {
                    places[pos].enemy = null
                    deleteLiveData.postValue(places[pos])
                },
                updateCallback = { alpha ->
                    animateLiveData.postValue(Pair(places[pos], alpha))
                }
            )
            places[pos].enemy!!.start()
        }
    }
}
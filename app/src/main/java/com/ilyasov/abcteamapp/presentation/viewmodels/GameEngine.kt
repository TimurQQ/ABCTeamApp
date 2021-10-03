package com.ilyasov.abcteamapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ilyasov.abcteamapp.entity.Enemy
import com.ilyasov.abcteamapp.entity.Hole
import com.ilyasov.abcteamapp.util.BASE_TIME_TO_RESPAWN
import kotlin.random.Random

class GameEngine : Thread() {
    private var timeToRespawn = BASE_TIME_TO_RESPAWN
    var places: List<Hole> = listOf(
        Hole(0, null), Hole(1, null), Hole(2, null),
        Hole(3, null), Hole(4, null), Hole(5, null),
        Hole(6, null), Hole(7, null), Hole(8, null),
    )
    val deleteLiveData: MutableLiveData<Hole> = MutableLiveData()
    val animateLiveData: MutableLiveData<Pair<Hole, Float>> = MutableLiveData()
    val heartLiveData: MutableLiveData<Any?> = MutableLiveData()
    var simultaneouslyEnemies = 0
    private var delayTime = timeToRespawn
    var gameOverFlag = false
    private var lastTime = System.nanoTime()

    private fun getNewEnemiesCount() {
        val count: Int = Random.nextInt(100)
        simultaneouslyEnemies = when {
            count < 70 -> 1
            count in 70..89 -> 2
            count in 90..94 -> 3
            count in 95..97 -> 4
            else -> 5
        }
    }

    override fun run() {
        while (!gameOverFlag) {
            val now = System.nanoTime()
            val deltaTime = now - lastTime
            lastTime = now
            startDelayedRespawn(deltaTime)
        }
    }

    private fun startDelayedRespawn(delta: Long) {
        if (delayTime > 0) {
            delayTime -= delta;
        } else {
            if (checkAllPositions()) {
                getNewEnemiesCount()
                respawnEnemy()
                delayTime = timeToRespawn
            } else {
                delayTime++
            }
        }
    }

    private fun checkAllPositions(): Boolean {
        var count = 0
        for (place in places) {
            if (place.enemy != null) {
                count++
                if (count == places.size) {
                    return false
                }
            }
        }
        return true
    }

    private fun respawnEnemy() {
        if (simultaneouslyEnemies > 0) {
            val pos = selectPosition()
            pos?.let {
                places[pos].enemy = Enemy({
                    places[pos].enemy = null
                    deleteLiveData.postValue(places[pos])
                }, { alpha ->
                    animateLiveData.postValue(Pair(places[pos], alpha))
                }, {
                    heartLiveData.postValue(null)
                })
                places[pos].enemy!!.start()
                simultaneouslyEnemies--
                Log.d("Enemies", places.toString())
            }
            respawnEnemy()
        }
        Log.d("Enemies", "---------------------------------")
    }

    private fun selectPosition(): Int? {
        val randomPlace = Random.nextInt(9)
        return if (places[randomPlace].enemy != null) {
            null
        } else {
            randomPlace
        }
    }
}
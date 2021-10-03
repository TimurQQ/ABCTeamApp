package com.ilyasov.abcteamapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController
import com.ilyasov.abcteamapp.R
import com.ilyasov.abcteamapp.presentation.fragments.base.BaseFragment
import com.ilyasov.abcteamapp.presentation.viewmodels.GameEngine
import com.ilyasov.abcteamapp.util.SAVED_SCORE
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.max

class GameFragment : BaseFragment(R.layout.fragment_game) {
    private val gameEngine = GameEngine()
    private lateinit var viewHolders: List<AppCompatImageButton>
    private lateinit var heartViews: List<AppCompatImageButton>
    private var score: Int = 0
    private var brokenHearts = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolders = listOf(
            mole0_0, mole0_1, mole0_2,
            mole1_0, mole1_1, mole1_2,
            mole2_0, mole2_1, mole2_2
        )
        heartViews = listOf(heart_1, heart_2, heart_3, heart_4, heart_5)
        for (i in viewHolders.indices) {
            viewHolders[i].setOnClickListener { v ->
                if (gameEngine.places[i].enemy != null) {
                    viewHolders[i].alpha = 0F
                    gameEngine.places[i].enemy!!.diedFlag = true
                    gameEngine.places[i].enemy = null
                    v.alpha = 0F
                    score++
                    tvScore.text = "Score: $score"
                }
            }
        }
        gameEngine.start()
        gameEngine.deleteLiveData.observe(viewLifecycleOwner) { hole ->
            Log.d("HOLE", "DELETE $hole")
            viewHolders[hole.number].alpha = 0F
        }
        gameEngine.animateLiveData.observe(viewLifecycleOwner) { hole_alpha ->
            viewHolders[hole_alpha.first.number].alpha = hole_alpha.second
        }
        gameEngine.heartLiveData.observe(viewLifecycleOwner) {
            synchronized(brokenHearts) {
                heartViews[brokenHearts].alpha = 0F
                brokenHearts++
                if (brokenHearts >= 5) {
                    gameEngine.gameOverFlag = true
                    val savedScore = sharedPrefs.getInt(SAVED_SCORE, 0)
                    with(sharedPrefs.edit()) {
                        putInt(SAVED_SCORE, max(savedScore, score))
                        apply()
                    }
                    findNavController().navigate(R.id.action_game_over)
                }
            }
        }
    }
}
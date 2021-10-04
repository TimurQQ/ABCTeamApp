package com.ilyasov.abcteamapp.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.fragment.findNavController
import com.ilyasov.abcteamapp.R
import com.ilyasov.abcteamapp.presentation.fragments.base.BaseFragment
import com.ilyasov.abcteamapp.presentation.viewmodels.GameEngine
import com.ilyasov.abcteamapp.util.CUR_SCORE
import com.ilyasov.abcteamapp.util.GAME_END_TIME
import com.ilyasov.abcteamapp.util.ONE_SECOND_IN_NANO
import com.ilyasov.abcteamapp.util.SAVED_SCORE
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.max

class GameFragment : BaseFragment(R.layout.fragment_game) {
    private val gameEngine = GameEngine()
    private lateinit var viewHolders: List<AppCompatImageButton>
    private var score: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewHolders = listOf(
            mole0_0, mole0_1, mole0_2,
            mole1_0, mole1_1, mole1_2,
            mole2_0, mole2_1, mole2_2
        )
        for (i in viewHolders.indices) {
            viewHolders[i].setOnClickListener { v ->
                gameEngine.places[i].enemy?.let { hitEnemy(i, v) }
            }
        }
        gameEngine.start()
        setupObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun hitEnemy(i /*index*/: Int, v /*enemy view*/: View) {
        v.alpha = 0F
        gameEngine.places[i].enemy!!.diedFlag = true
        gameEngine.places[i].enemy = null
        tvScore.text = "Score: ${++score}"
    }

    private fun setupObservers() {
        gameEngine.deleteLiveData.observe(viewLifecycleOwner) { hole ->
            viewHolders[hole.number].alpha = 0F
        }
        gameEngine.animateLiveData.observe(viewLifecycleOwner) { hole_alpha ->
            viewHolders[hole_alpha.first.number].alpha = hole_alpha.second
        }
        gameEngine.secondsLiveData.observe(viewLifecycleOwner) { nanoSeconds ->
            tvTimer.text = ((GAME_END_TIME - nanoSeconds) / ONE_SECOND_IN_NANO).toString()
        }
        gameEngine.endGameLiveData.observe(viewLifecycleOwner) {
            val savedScore = sharedPrefs.getInt(SAVED_SCORE, 0)
            with(sharedPrefs.edit()) {
                putInt(SAVED_SCORE, max(savedScore, score))
                putInt(CUR_SCORE, score)
                apply()
            }
            findNavController().navigate(R.id.action_game_over)
        }
    }
}
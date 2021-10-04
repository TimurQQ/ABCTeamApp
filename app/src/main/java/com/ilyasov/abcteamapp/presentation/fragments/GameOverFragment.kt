package com.ilyasov.abcteamapp.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ilyasov.abcteamapp.R
import com.ilyasov.abcteamapp.presentation.fragments.base.BaseFragment
import com.ilyasov.abcteamapp.util.CUR_SCORE
import kotlinx.android.synthetic.main.fragment_game_over.*

class GameOverFragment : BaseFragment(R.layout.fragment_game_over) {
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRetry.setOnClickListener { findNavController().navigate(R.id.retry_game) }
        btnToStart.setOnClickListener { findNavController().navigate(R.id.to_start_menu) }
        tvPoints.text = "You have ${sharedPrefs.getInt(CUR_SCORE, 0)} points"
    }
}
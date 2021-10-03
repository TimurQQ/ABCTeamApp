package com.ilyasov.abcteamapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ilyasov.abcteamapp.R
import com.ilyasov.abcteamapp.presentation.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_gameover.*

class GameOverFragment : BaseFragment(R.layout.fragment_gameover) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRetry.setOnClickListener {
            findNavController().navigate(R.id.retry_game)
        }
        btnToStart.setOnClickListener {
            findNavController().navigate(R.id.to_start_menu)
        }
    }
}
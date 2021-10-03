package com.ilyasov.abcteamapp.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ilyasov.abcteamapp.R
import com.ilyasov.abcteamapp.presentation.fragments.base.BaseFragment
import com.ilyasov.abcteamapp.util.SAVED_SCORE
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : BaseFragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_start_game)
        }
        tvBestScore.text = "The best SCORE:${sharedPrefs.getInt(SAVED_SCORE, 0)}"
    }
}
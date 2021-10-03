package com.ilyasov.abcteamapp.presentation.fragments.base

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.ilyasov.abcteamapp.util.APP_PREFERENCES

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    val sharedPrefs: SharedPreferences by lazy {
        requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }
}
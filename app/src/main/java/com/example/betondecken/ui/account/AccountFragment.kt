// src/main/java/com/example/betondecken/ui/account/AccountFragment.kt
package com.example.betondecken.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.betondecken.R

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el nuevo diseño del fragmento de cuenta
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
}

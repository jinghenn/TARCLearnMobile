package com.example.tarclearn.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tarclearn.R

class ProfileFragment : Fragment() {
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //get user info from shared preference
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.pref_user),
            Context.MODE_PRIVATE
        )

        return inflater.inflate(R.layout.drawer_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvUsername: TextView = requireView().findViewById(R.id.tvUsername)
        val tvUid: TextView = requireView().findViewById(R.id.tvUserId)

        val uid = sharedPref.getString(getString(R.string.key_user_id), "null")
        val username = sharedPref.getString(getString(R.string.key_username), "null")

        tvUsername.setText(username)
        tvUid.setText(uid)
    }
}
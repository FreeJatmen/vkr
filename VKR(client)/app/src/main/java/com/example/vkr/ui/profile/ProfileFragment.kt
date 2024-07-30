package com.example.vkr.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.vkr.R
import com.example.vkr.ui.profile.authVacancies.AuthVacanciesActivity
import com.example.vkr.ui.profile.authorization.AuthActivity
import com.example.vkr.ui.profile.resume.ResumeActivity
import com.example.vkr.ui.profile.settings.SettingsActivity

class ProfileFragment : Fragment() {

    private lateinit var settingsButton: Button
    private lateinit var myVacanciesButton: Button
    private lateinit var authButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var myResumeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        settingsButton = view.findViewById(R.id.settings_button)
        settingsButton.setOnClickListener {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }

        myVacanciesButton = view.findViewById(R.id.my_vacancies_button)
        myVacanciesButton.setOnClickListener {
            startActivity(Intent(requireContext(), AuthVacanciesActivity::class.java))
        }

        myResumeButton = view.findViewById(R.id.my_resume_button)
        myResumeButton.setOnClickListener {
            startActivity(Intent(requireContext(), ResumeActivity::class.java))
        }

        authButton = view.findViewById(R.id.auth_button)
        authButton.setOnClickListener {
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }

        updateUI(token)

        return view
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null).toString()
        updateUI(token)
    }

    private fun updateUI(token: String?) {
        if (token == "null") {
            authButton.visibility = View.VISIBLE
            settingsButton.visibility = View.GONE
            myVacanciesButton.visibility = View.GONE
            myResumeButton.visibility = View.GONE
        } else {
            authButton.visibility = View.GONE
            settingsButton.visibility = View.VISIBLE
            myVacanciesButton.visibility = View.VISIBLE
            myResumeButton.visibility = View.VISIBLE
        }
    }
}

package com.example.vkr.ui.utilities

import com.example.vkr.ui.retrofit.Vacancies

interface OnVacancyClickListener {
    fun onItemClick(vacancy: Vacancies)
}
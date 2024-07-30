package com.example.vkr.ui.profile.authVacancies

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.retrofit.RetrofitClient
import com.example.vkr.ui.retrofit.Vacancies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthUpdateVacancyActivity: AppCompatActivity() {

    private lateinit var vacancyId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_vacancy)

        vacancyId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        val nameInput = findViewById<EditText>(R.id.name_vacancy_input)
        val paymentInput = findViewById<EditText>(R.id.payment_vacancy_input)
        val expertiseInput = findViewById<EditText>(R.id.expertise_vacancy_input)
        val employmentInput = findViewById<EditText>(R.id.employment_vacancy_input)
        val descriptionInput = findViewById<EditText>(R.id.description_vacancy_input)

        job = CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                val vacancy = RetrofitClient.apiService.getAuthVacancy(token, vacancyId)
                println(vacancy)
                nameInput.setText(vacancy.body()?.name)
                paymentInput.setText(vacancy.body()?.payment)
                expertiseInput.setText(vacancy.body()?.expertise)
                employmentInput.setText(vacancy.body()?.employment)
                descriptionInput.setText(vacancy.body()?.description)
            }
        }

        val updateButton: Button = findViewById(R.id.update_vacancy_button)
        updateButton.setOnClickListener {
            if (validateInput(nameInput, paymentInput, expertiseInput, employmentInput, descriptionInput)) {
                val vacancies = Vacancies("", "", nameInput.text.toString(), paymentInput.text.toString(), expertiseInput.text.toString(),
                    employmentInput.text.toString(), descriptionInput.text.toString())
                job = CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.apiService.updateVacancy(token, vacancyId, vacancies)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AuthUpdateVacancyActivity, "Вакансия изменена успешно", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            println(response)
                            Toast.makeText(this@AuthUpdateVacancyActivity, "Ошибка при изменении вакансии", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val canselButton: Button = findViewById(R.id.cansel_update_vacancy_button)
        canselButton.setOnClickListener {
            finish()
        }

    }

    private fun validateInput(vararg inputs: EditText): Boolean {
        var isValid = true
        inputs.forEach { input ->
            if (input.text.toString().isEmpty()) {
                input.error = "Это поле обязательно для заполнения"
                isValid = false
            }
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
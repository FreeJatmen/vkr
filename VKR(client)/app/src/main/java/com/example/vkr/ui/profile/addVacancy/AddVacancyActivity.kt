package com.example.vkr.ui.profile.addVacancy

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.retrofit.RetrofitClient.apiService
import com.example.vkr.ui.retrofit.Vacancies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddVacancyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vacancy)

        val nameInput = findViewById<EditText>(R.id.name_vacancy_input)
        val paymentInput = findViewById<EditText>(R.id.payment_vacancy_input)
        val expertiseInput = findViewById<EditText>(R.id.expertise_vacancy_input)
        val employmentInput = findViewById<EditText>(R.id.employment_vacancy_input)
        val descriptionInput = findViewById<EditText>(R.id.description_vacancy_input)
        val submitButton = findViewById<Button>(R.id.submit_button)

        submitButton.setOnClickListener {
            if (validateInput(nameInput, paymentInput, expertiseInput, employmentInput, descriptionInput)) {
                val vacancies = Vacancies("", "", nameInput.text.toString(), paymentInput.text.toString(), expertiseInput.text.toString(),
                    employmentInput.text.toString(), descriptionInput.text.toString())
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("auth_token", null)

                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main) {
                        val response = apiService.addVacancies(token.toString(), vacancies)
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddVacancyActivity, "Вакансия успешно добавлена", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            println(response)
                            Toast.makeText(this@AddVacancyActivity, "Ошибка при добавлении вакансии", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
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
}
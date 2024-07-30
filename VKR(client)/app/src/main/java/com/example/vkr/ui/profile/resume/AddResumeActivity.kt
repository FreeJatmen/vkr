package com.example.vkr.ui.profile.resume

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vkr.R
import com.example.vkr.ui.retrofit.Resume
import com.example.vkr.ui.retrofit.RetrofitClient
import com.example.vkr.ui.retrofit.Vacancies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddResumeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_resume)

        val nameInput = findViewById<EditText>(R.id.name_resume_input)
        val surnameInput = findViewById<EditText>(R.id.surname_resume_input)
        val contactsInput = findViewById<EditText>(R.id.contacts_input)
        val skillsInput = findViewById<EditText>(R.id.skills_input)
        val aboutYourself = findViewById<EditText>(R.id.about_yourself_input)
        val resumeNameInput = findViewById<EditText>(R.id.resumeName_input)
        val submitButton = findViewById<Button>(R.id.submit_button)

        submitButton.setOnClickListener {
            if (validateInput(nameInput, surnameInput, contactsInput, skillsInput, aboutYourself, resumeNameInput)) {
                val resume = Resume("", "", nameInput.text.toString(), surnameInput.text.toString(), contactsInput.text.toString(),
                    skillsInput.text.toString(), aboutYourself.text.toString(), resumeNameInput.text.toString())
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("auth_token", null)

                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.apiService.addResume(token.toString(), resume)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddResumeActivity, "Резюме успешно добавлено", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            println(response)
                            Toast.makeText(this@AddResumeActivity, "Ошибка при добавлении резюме", Toast.LENGTH_SHORT).show()
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
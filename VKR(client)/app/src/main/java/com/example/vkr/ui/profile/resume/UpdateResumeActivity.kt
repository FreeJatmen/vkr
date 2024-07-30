package com.example.vkr.ui.profile.resume

import android.content.Context
import android.content.SharedPreferences
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateResumeActivity : AppCompatActivity() {
    private lateinit var resumeId: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_resume)

        resumeId = intent.getStringExtra("id").toString()

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("auth_token", null).toString()

        val resumeNameInput = findViewById<EditText>(R.id.resumeName_input)
        val nameInput = findViewById<EditText>(R.id.name_resume_input)
        val surnameInput = findViewById<EditText>(R.id.surname_resume_input)
        val contactsInput = findViewById<EditText>(R.id.contacts_input)
        val skillsInput = findViewById<EditText>(R.id.skills_input)
        val aboutYourself = findViewById<EditText>(R.id.about_yourself_input)

        job = CoroutineScope(Dispatchers.IO).launch {
            val resume = RetrofitClient.apiService.getResume(token, resumeId)
            withContext(Dispatchers.Main) {
                resumeNameInput.setText(resume.body()?.resumeName)
                nameInput.setText(resume.body()?.name)
                surnameInput.setText(resume.body()?.surname)
                contactsInput.setText(resume.body()?.contacts)
                skillsInput.setText(resume.body()?.skills)
                aboutYourself.setText(resume.body()?.aboutYourself)
            }
        }

        val updateButton: Button = findViewById(R.id.update_resume_button)
        updateButton.setOnClickListener {
            if (validateInput(nameInput, surnameInput, contactsInput, skillsInput, aboutYourself, resumeNameInput)) {
                val resume = Resume("", "", nameInput.text.toString(), surnameInput.text.toString(), contactsInput.text.toString(),
                    skillsInput.text.toString(), aboutYourself.text.toString(), resumeNameInput.text.toString())
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("auth_token", null)
                job = CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.apiService.updateResume(token.toString(), resumeId,  resume)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@UpdateResumeActivity, "Резюме успешно изменено", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            println(response)
                            Toast.makeText(this@UpdateResumeActivity, "Ошибка при изменении резюме", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val canselButton: Button = findViewById(R.id.cansel_update_resume_button)
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
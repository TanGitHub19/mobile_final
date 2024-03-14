package com.mita.gamebuddymobile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mita.gamebuddymobile.api.RetrofitClient
import com.mita.gamebuddymobile.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPage : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var age: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signupbutton: Button

    private lateinit var checkbox: CheckBox

    private var gender: Int = -1 // Default value for gender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        age = findViewById(R.id.age)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmpassword)
        signupbutton = findViewById(R.id.signupbutton)
        checkbox = findViewById(R.id.checkBox)

        signupbutton.isEnabled = false

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            signupbutton.isEnabled = isChecked
            if (isChecked) {
                showCustomDialogBox()
            }
        }

        val logInRed: TextView = findViewById(R.id.AlreadyHaveAccLogIn)
        logInRed.setOnClickListener {
            val intent = Intent(this, LogInPage::class.java)
            startActivity(intent)
        }

        signupbutton.setOnClickListener {
            val name = username.text.toString().trim()
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()
            val ageText = age.text.toString().trim()
            val confirmPassword = confirmPassword.text.toString().trim()

            if (password == confirmPassword) {
                // Check if age is a valid number
                if (ageText.isNotEmpty() && ageText.matches("\\d+".toRegex())) {
                    val age = ageText.toInt()
                    // Passwords match, age is valid, and gender is selected, proceed with registration
                    if (gender != -1) {
                        registerUser(name, email, password, age, gender)
                    } else {
                        Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        val maleRadioButton: RadioButton = findViewById(R.id.male)
        val femaleRadioButton: RadioButton = findViewById(R.id.female)
        val othersRadioButton: RadioButton = findViewById(R.id.others)

        maleRadioButton.setOnClickListener {
            gender = GENDER_MALE // Set gender to male
        }

        femaleRadioButton.setOnClickListener {
            gender = GENDER_FEMALE // Set gender to female
        }

        othersRadioButton.setOnClickListener {
            gender = GENDER_OTHERS // Set gender to others
        }
    }

    private fun showCustomDialogBox() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnConfirm: Button = dialog.findViewById(R.id.confirmdialog)
        val btnCancel: Button = dialog.findViewById(R.id.canceldialog)

        btnConfirm.setOnClickListener {
            checkbox.isChecked = true
            Toast.makeText(this, "Confirm", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            checkbox.isChecked = false
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun registerUser(name: String, email: String, password: String, age: Int, gender: Int) {
        val apiService = RetrofitClient.apiService

        val user = User(id = 0, name = name, email = email, password = password, age = age, gender = gender)

        apiService.register(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignUpPage, "Registration successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@SignUpPage, HomePage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("Registration", "Registration failed with response code: ${response.code()}")
                    Toast.makeText(this@SignUpPage, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Registration", "Failed to execute registration request", t)
                Toast.makeText(this@SignUpPage, "Error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val GENDER_MALE = 0
        private const val GENDER_FEMALE = 1
        private const val GENDER_OTHERS = 2
    }
}

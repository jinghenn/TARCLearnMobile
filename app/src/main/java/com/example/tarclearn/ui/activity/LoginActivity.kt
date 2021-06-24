package com.example.tarclearn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tarclearn.R
import com.example.tarclearn.databinding.ActivityLoginBinding
import com.example.tarclearn.factory.LoginViewModelFactory
import com.example.tarclearn.repository.UserRepository
import com.example.tarclearn.viewmodel.login.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        //initialize user repository
        val repository = UserRepository()
        //create a view model using factory design pattern
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(LoginViewModel::class.java)

        binding.loginViewModel = viewModel

        binding.btnLogin.setOnClickListener {
            val uid = binding.txtUserId.text.toString().trim()
            val pw = binding.txtPassword.text.toString().trim()
            if (uid != "" && pw != "") {
                viewModel.login(uid, pw)
            }
            if (uid == "") {
                binding.layoutUserId.isErrorEnabled = true
                binding.layoutUserId.error = "User ID is required"
            }
            if (pw == "") {
                binding.layoutPassword.isErrorEnabled = true
                binding.layoutPassword.error = "Password is required"
            }
        }
        binding.txtUserId.doAfterTextChanged {
            if (it.toString() == "") {
                binding.layoutUserId.isErrorEnabled = true
                binding.layoutUserId.error = "User ID is required"
            } else {
                binding.layoutUserId.isErrorEnabled = false
            }
        }
        binding.txtPassword.doAfterTextChanged {
            if (it.toString() == "") {
                binding.layoutPassword.isErrorEnabled = true
                binding.layoutPassword.error = "Password is required"
            } else {
                binding.layoutPassword.isErrorEnabled = false
            }
        }

        viewModel.isAuthenticated.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                val sharedPref = this.getSharedPreferences(
                    getString(R.string.pref_user),
                    MODE_PRIVATE
                )
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.key_is_logged_in), true)
                    putString(
                        getString(R.string.key_user_id),
                        viewModel.user.value!!.userId
                    )
                    putString(
                        getString(R.string.key_username),
                        viewModel.user.value!!.username
                    )
                    putString(
                        getString(R.string.key_email),
                        viewModel.user.value!!.email
                    )
                    putBoolean(
                        getString(R.string.key_is_lecturer),
                        viewModel.user.value!!.isLecturer
                    )

                    commit()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        viewModel.loginError.observe(this, Observer {
            if (it) {
                binding.layoutPassword.isErrorEnabled = true
                binding.layoutPassword.error = "Incorrect User ID or password"
            }
        })
    }
}
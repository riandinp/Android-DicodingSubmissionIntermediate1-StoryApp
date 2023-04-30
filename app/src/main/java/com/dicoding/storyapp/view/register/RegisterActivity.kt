package com.dicoding.storyapp.view.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.view.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnSignUp.isEnabled = s.toString().length >= 8
            }

        })
    }

    private fun setupViewModel() {
        registerViewModel.register.observe(this) { isSuccess ->
            if (isSuccess) {
                val dialogBuilder = AlertDialog.Builder(this)
                    .setTitle("Registrasi Berhasil")
                    .setMessage("Akun anda berhasil didaftar. Silahkan Login Terlebih dahulu untuk mengakses daftar story")
                    .setPositiveButton("Oke") { _,_ ->
                        LoginActivity.start(this)
                        finish()
                    }
                    .setOnDismissListener {
                        LoginActivity.start(this)
                        finish()
                    }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        }

        registerViewModel.snackbarText.observe(this) { text ->
            when {
                text.contains("taken") -> {
                    binding.edRegisterEmail.error = getString(R.string.email_created)
                    binding.edRegisterEmail.requestFocus()
                }
                text.contains("created") ->{
                    // Do Nothing
                }
                text.contains("must be a valid email") -> {
                    binding.edRegisterEmail.error = getString(R.string.email_must_valid)
                    binding.edRegisterEmail.requestFocus()
                }
                text.contains("Password must be at least 6 characters long") -> {
                    binding.edRegisterPassword.error = getString(R.string.password_invalid_input)
                    binding.edRegisterPassword.requestFocus()
                }
                else -> Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
            }
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(value: Boolean) {
        binding.btnSignIn.isEnabled = !value
        binding.btnSignUp.isInvisible = value
        binding.pbLoadingScreen.isVisible = value
    }

    private fun setupAction() {
        with(binding) {
            btnSignIn.setOnClickListener {
                LoginActivity.start(this@RegisterActivity)
            }

            btnSignUp.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()
                when {
                    name.isEmpty() -> {
                        edRegisterName.error = "Nama tidak boleh kosong"
                        edRegisterName.requestFocus()
                    }
                    email.isEmpty() -> {
                        edRegisterEmail.error = "Email tidak boleh kosong"
                        edRegisterEmail.requestFocus()
                    }
                    password.isEmpty() -> {
                        edRegisterPassword.error = "Passoword tidak boleh kosong"
                        edRegisterPassword.requestFocus()
                    }
                    else -> {
                        registerViewModel.register(name, email, password)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, RegisterActivity::class.java)
            context.startActivity(starter)
        }
    }
}
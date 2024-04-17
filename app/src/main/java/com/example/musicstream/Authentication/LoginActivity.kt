package com.example.musicstream.Authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicstream.MainActivity
import com.example.musicstream.R
import com.example.musicstream.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            login(email,password)
        }
    }
    private fun login(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener (this){ task ->

            if(task.isSuccessful){
                //code for logging in user
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity,"User does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}
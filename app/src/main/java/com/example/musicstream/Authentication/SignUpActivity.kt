package com.example.musicstream.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicstream.MainActivity
import com.example.musicstream.R
import com.example.musicstream.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var btnSignUp : Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        btnSignUp = binding.signUp

        btnSignUp.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),email)){
                binding.email.setError("Invalid email")
                return@setOnClickListener
            }
            if(password.length<6){
                binding.password.setError("Length Should be 6 char")
                return@setOnClickListener
            }
            signUp(name,email,password)
        }

        binding.loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    private fun signUp(name: String, email: String, password: String) {
        setInProgress(true)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful) {
                    setInProgress(false)
                    addUserToDataBase(name,email,mAuth.currentUser?.uid!!)
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    setInProgress(false)
                    Toast.makeText(this,"Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun setInProgress(inProgress : Boolean){
        if(inProgress){
            btnSignUp.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            btnSignUp.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun addUserToDataBase(name: String, email: String, uid: String?) {
        mDatabaseRef = FirebaseDatabase.getInstance().reference
        mDatabaseRef.child("user").child(uid!!).setValue(User(name,email,uid))
    }
}
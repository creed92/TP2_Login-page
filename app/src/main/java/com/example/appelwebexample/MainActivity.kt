package com.example.appelwebexample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.appelwebexample.`class`.User
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {


    @SuppressLint("WrongViewCast", "MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1 = findViewById<Button>(R.id.register)
        val btn = findViewById<Button>(R.id.login)
        val frame1 = findViewById<FrameLayout>(R.id.frame1)

        //activate the frameregister
        val fragment1 = supportFragmentManager.findFragmentByTag("fragment1")
        if (fragment1 == null) {
            val fragTransaction = supportFragmentManager.beginTransaction()
            fragTransaction.replace(R.id.frame1, Fragmentregister(), "fragment1").commit()
        }

        //activate the framelogin
        val fragment2 = supportFragmentManager.findFragmentByTag("fragment2")
        if (fragment2 == null) {
            val fragTransaction2 = supportFragmentManager.beginTransaction()
            fragTransaction2.replace(R.id.frame, Fragmentlogin(), "fragment2").commit()
        }

        //register frame visibility off
        frame1.visibility= View.GONE

        //to register page (fun to be called in register fragment)
        btn1.setOnClickListener {
            toRegister()
        }

        //to user list button
        btn.setOnClickListener {

            //if password and username are correct it takes you to user list page
            if (login()) {
                val intent = Intent(this, UserListActivity::class.java)
                startActivity(intent)
            } else {

                //else if the logins are incorrect or empty it will display this custom toast
                val inflater = layoutInflater
                val layout: View = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))
                val text: TextView = layout.findViewById(R.id.tvText)
                text.text = "Invalid username or password"
                with (Toast(applicationContext)) {
                    setGravity(Gravity.CENTER_VERTICAL, 0, 900)
                    duration = Toast.LENGTH_SHORT
                    view = layout
                    show()
                }
            }
        }
    }


    //function to hide elements with animation
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hide(view: View) {
        view.animate()
            .translationY(windowManager?.currentWindowMetrics?.bounds?.height()?.toFloat()
                ?: 1000f)
            .alpha(0f)
            .setDuration(1000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    //function to show elements with animation
    @RequiresApi(Build.VERSION_CODES.R)
    private fun show(view: View) {
        view.alpha = 0f
        view.y = windowManager?.currentWindowMetrics?.bounds?.height()?.toFloat() ?: 1000f
        view.visibility = View.VISIBLE

        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)

    }

    //function that takes u to the login page
    @RequiresApi(Build.VERSION_CODES.R)
    fun toLogin(){
        val btn1 = findViewById<Button>(R.id.register)
        val btn = findViewById<Button>(R.id.login)
        val frame1 = findViewById<FrameLayout>(R.id.frame1)
        val frame = findViewById<FrameLayout>(R.id.frame)
        show(btn)
        show(btn1)
        hide(frame1)
        show(frame)
    }

    //function that takes u to the register page
    @RequiresApi(Build.VERSION_CODES.R)
    fun toRegister(){
        val btn1 = findViewById<Button>(R.id.register)
        val btn = findViewById<Button>(R.id.login)
        val frame1 = findViewById<FrameLayout>(R.id.frame1)
        val frame = findViewById<FrameLayout>(R.id.frame)
        hide(btn)
        hide(btn1)
        hide(frame)
        show(frame1)

    }

    //fun for the register page to save password and username
    fun saveCredentials() {
        val passwordRegister = findViewById<TextView>(R.id.newPassword).text.toString()
        val usernameRegister = findViewById<TextView>(R.id.newUsername).text.toString()

        if (passwordRegister.isBlank() || usernameRegister.isBlank()) {
            Log.d("SAVE_CREDENTIALS", "Password or username is missing")
            return
        }

        Log.d("SAVE_CREDENTIALS", "Username: $usernameRegister Password: $passwordRegister")

        val hashedPassword = hashPassword(passwordRegister)
        Log.d("SAVE_CREDENTIALS", "Hashed Password: $hashedPassword")

        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(usernameKey, usernameRegister)
        editor.putString(passwordKey, hashedPassword)
        editor.apply()

        Log.d("SAVE_CREDENTIALS", "Credentials saved successfully")
    }

    //fun for the login page to check the password and username
    private fun login(): Boolean {
        val passwordLogin = findViewById<TextView>(R.id.password).text.toString()
        val usernameLogin = findViewById<TextView>(R.id.username).text.toString()

        if (passwordLogin.isBlank() || usernameLogin.isBlank()) {
            Log.d("LOGIN", "Password or username is missing")
            return false
        }

        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString(usernameKey, null)
        val savedPassword = sharedPreferences.getString(passwordKey, null)

        if (savedUsername == null || savedPassword == null) {
            Log.d("LOGIN", "No credentials found")
            return false
        }

        val hashedPassword = hashPassword(passwordLogin)

        Log.d("LOGIN", "Username: $usernameLogin Password: $passwordLogin")
        Log.d("LOGIN", "Hashed Password: $hashedPassword")

        return savedUsername == usernameLogin && savedPassword == hashedPassword
    }

    //fun to hash the password
    private fun hashPassword(password: String): String {
        val salt = "MySaltValue" // Replace with your own salt value
        val md = MessageDigest.getInstance("SHA-256")
        val saltedPassword = "$password$salt"
        md.update(saltedPassword.toByteArray(Charsets.UTF_8))
        val digest = md.digest()
        return Base64.encodeToString(digest, Base64.DEFAULT)
    }
    private val sharedPreferencesKey = "MY_SHARED_PREFERENCES"
    private val usernameKey = "USERNAME"
    private val passwordKey = "PASSWORD"









}
package com.example.appelwebexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.text.Editable
import android.view.View

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.appelwebexample.viewModels.UserViewModel

class UserDetailActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var nameTextView: TextView
    private lateinit var birthdateTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var companyTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var comStreetTextView: TextView
    private lateinit var comCityTextView: TextView
    private lateinit var comZipTextView: TextView
    private lateinit var streetTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var zipcodeTextView: TextView
    private lateinit var noteTextView: EditText
    private lateinit var mailTextView: TextView
    private lateinit var avatarImageView:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        nameTextView = findViewById(R.id.nameTextView)
        birthdateTextView = findViewById(R.id.birthdateTextView)
        genreTextView = findViewById(R.id.genreTextView)
        companyTextView = findViewById(R.id.companyTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        streetTextView = findViewById(R.id.streetTextView)
        cityTextView = findViewById(R.id.cityTextView)
        zipcodeTextView = findViewById(R.id.zipcodeTextView)
        avatarImageView=findViewById(R.id.imageView)
        comStreetTextView = findViewById(R.id.comStreetTextView)
        comCityTextView = findViewById(R.id.comCityTextView)
        comZipTextView = findViewById(R.id.comZipTextView)
        mailTextView = findViewById(R.id.mail)
        noteTextView = findViewById(R.id.note)
        val applyChangesButton = findViewById<Button>(R.id.apply_button)
        val cancelChangesButton = findViewById<Button>(R.id.cancel_button)

        //getting the id form the user list activity
        val userId = intent.getIntExtra("user_id", -1)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.getUser(userId.toString())

        //setting the observer
        userViewModel.currentUser.observe(this) { user ->
            //user
            nameTextView.text = "${user.firstname} ${user.lastname}"
            //making the birthdate looking like a birthdate
            birthdateTextView.text = user.birthdate.substring(0, 10)
            genreTextView.text = user.genre
            companyTextView.text = user.company.name
            mailTextView.text = user.email
            noteTextView.setText(Editable.Factory.getInstance().newEditable(user.note))


            //company
            descriptionTextView.text = user.company.description
            comStreetTextView.text = user.company.address.street
            comCityTextView.text = user.company.address.city
            comZipTextView.text = user.company.address.zipcode
            //adress user
            streetTextView.text = user.address.street
            cityTextView.text = user.address.city
            zipcodeTextView.text = user.address.zipcode


            //glide for user avatar
            Glide.with(this)
                .load(user.avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(avatarImageView)
        }

        //button to get back to the list of users
        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
            finish()
        }



        // Set the OnFocusChangeListener on the note EditText
        noteTextView.setOnFocusChangeListener { _, hasFocus ->
            // Show the buttons if the EditText gains focus
            if (hasFocus) {
                applyChangesButton.visibility = View.VISIBLE
                cancelChangesButton.visibility = View.VISIBLE
            } else {
                // Hide the buttons if the EditText loses focus
                applyChangesButton.visibility = View.GONE
                cancelChangesButton.visibility = View.GONE
            }
        }

        //apply note changes button
        applyChangesButton.setOnClickListener {
            val newNote = noteTextView.text.toString()
            userViewModel.currentUser.value?.let { currentUser ->
                val updatedUser = currentUser.copy(note = newNote)
                userViewModel.updateUser(updatedUser)
            }
            applyChangesButton.visibility = View.GONE
            cancelChangesButton.visibility = View.GONE
        }

        //cancel note changes
        cancelChangesButton.setOnClickListener {
            userViewModel.currentUser.value?.let { currentUser ->
                noteTextView.setText(currentUser.note)
            }
            applyChangesButton.visibility = View.GONE
            cancelChangesButton.visibility = View.GONE
        }

    }
}
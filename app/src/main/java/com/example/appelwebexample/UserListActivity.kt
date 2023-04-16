package com.example.appelwebexample

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appelwebexample.`class`.User
import com.example.appelwebexample.`class`.UserAdapter
import com.example.appelwebexample.viewModels.UserViewModel
import androidx.appcompat.widget.SearchView

class UserListActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.usersrecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(listOf(), this::onUserClick)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ColorDrawable(Color.TRANSPARENT))})

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.getUsers()

        //adapter for the filtered users
        userViewModel.users.observe(this) { users ->
            adapter.updateUsers(users)
        }


         // Initializing the searchView and setting a listener
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //update the user list
                adapter.filter.filter(newText)
                return false
            }
        })

        //button to get back to the login menu
        val backButton = findViewById<Button>(R.id.backToList)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
    //function to pass the id for the user details activity page
    private fun onUserClick(user: User) {
        val intent = Intent(this, UserDetailActivity::class.java)
        intent.putExtra("user_id", user.id)
        startActivity(intent)
    }


}

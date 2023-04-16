package com.example.appelwebexample

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.R)
class Fragmentregister : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmentregister, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //cancel button to get back to login menu
        val cancelBut = view.findViewById<Button>(R.id.quitter)
        cancelBut.setOnClickListener {
            (activity as MainActivity?)!!.toLogin()
        }

        //validate button to validate username and password
        val validate = view.findViewById<Button>(R.id.validateBut)
        validate.setOnClickListener {

            val passwordEditText = view.findViewById<TextView>(R.id.newPassword)
            val usernameEditText = view.findViewById<TextView>(R.id.newUsername)
            val password = passwordEditText.text.toString()
            val username = usernameEditText.text.toString()

            //check if empty
            if (password.isEmpty() || username.isEmpty()) {

                //calling the custom Toast to be displayed
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container))
                val text = layout.findViewById<TextView>(R.id.tvText)
                text.text = "Please choose both username and password"
                val toast = Toast(activity)
                toast.duration = Toast.LENGTH_SHORT
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 900) // added gravity here
                toast.view = layout
                toast.show()

            } else {
                //else save the new password and username and takes you to login page
                (activity as MainActivity?)!!.saveCredentials()
                (activity as MainActivity?)!!.toLogin()
            }
        }

    }


}
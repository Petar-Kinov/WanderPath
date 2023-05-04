package com.example.wanderpath.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.wanderpath.data.model.User
import com.example.wanderpath.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var usernameET: EditText
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var verifyPasswordET: EditText
    private lateinit var signUpBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        usernameET = binding.nameET
        emailET = binding.emailET
        passwordET = binding.passwordET
        verifyPasswordET = binding.verifyPasswordET

        signUpBtn.setOnClickListener {
            val user = User(
                username = usernameET.text.toString(),
                email = emailET.text.toString(),
                password = passwordET.text.toString(),
                registrationTokens = mutableListOf()
            )


        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
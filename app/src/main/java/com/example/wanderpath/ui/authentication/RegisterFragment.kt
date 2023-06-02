package com.example.wanderpath.ui.authentication

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wanderpath.R
import com.example.wanderpath.data.model.User
import com.example.wanderpath.databinding.FragmentRegisterBinding
import com.example.wanderpath.domain.auth.AuthViewModel
import com.example.wanderpath.ui.login.LoginViewModelFactory
import com.example.wanderpath.util.EmailValidator

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(requireActivity(), LoginViewModelFactory()).get(AuthViewModel::class.java)
    }

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
        signUpBtn = binding.signUpBtn

        setCheckMarkListener(listOf(usernameET, emailET, passwordET, verifyPasswordET))

        signUpBtn.setOnClickListener {
            if (listOf(
                    usernameET,
                    emailET,
                    passwordET,
                    verifyPasswordET
                ).all { it.compoundDrawables[2] != null }
            ) {
                val user = User(
                    username = usernameET.text.toString(),
                    email = emailET.text.toString(),
                    password = passwordET.text.toString(),
                    registrationTokens = mutableListOf()
                )

                authViewModel.signup(user)

            }
        }

        authViewModel.signUpIsResult.observe(requireActivity(), Observer {
            val result = it ?: return@Observer


            findNavController().navigate(R.id.mainActivity)
//            requireActivity().setResult(Activity.RESULT_OK)
//            requireActivity().finish()
        })

        return binding.root
    }

    private fun setCheckMarkListener(editTextViewList: List<TextView>) {
        for (editTextView in editTextViewList) {
            editTextView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //ignore
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //ignore
                }

                override fun afterTextChanged(text: Editable) {
                    when (editTextView) {
                        usernameET -> setCheckMark(editTextView, text.isNotEmpty())
                        emailET -> setCheckMark(
                            editTextView,
                            EmailValidator.isEmailValid(text.toString())
                        )

                        passwordET -> setCheckMark(editTextView, text.length >= 6)
                        verifyPasswordET -> setCheckMark(
                            editTextView,
                            text.toString() == binding.verifyPasswordET.text.toString()
                        )
                    }
                }
            }).also { textWatcher ->
                // Set the TextWatcher as a tag on the EditText view
                editTextView.tag = textWatcher
            }
        }
    }

    private fun setCheckMark(textView: TextView, isConditionMet: Boolean) {
        if (isConditionMet) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.ic_baseline_check_24, 0
            )
        } else {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
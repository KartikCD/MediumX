package com.kartikcd.mediumx.ui.auth

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.material.snackbar.Snackbar
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentSignUpBinding
import com.kartikcd.mediumx.util.Resource

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private var validation: AwesomeValidation? = null
    val viewModel: AuthViewModel by activityViewModels()
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        validation = AwesomeValidation(ValidationStyle.BASIC)
        progressDialog = ProgressDialog(activity)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addValidation()

        _binding?.registerButton?.setOnClickListener {
            if (validation!!.validate()) {
                _binding?.apply {
                    viewModel.signup(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString(),
                        usernameEditText.text.toString()
                    )
                }
            }
        }

        viewModel.user.observe({ lifecycle }) {
            when(it) {
                is Resource.Success -> {
                    hideProgressDialog()
                    Snackbar.make(
                        view,
                        "User registered successfully.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    Snackbar.make(
                        view,
                        it.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                is Resource.Loading -> {
                    showProgressDialog("Registering user...")
                }
            }
        }

    }

    fun addValidation() {
        _binding?.apply {
            validation?.addValidation(usernameEditText, RegexTemplate.NOT_EMPTY, "Username cannot be empty")
            validation?.addValidation(emailEditText, Patterns.EMAIL_ADDRESS, "Please enter valid email address.")
            validation?.addValidation(passwordEditText, "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$", "Please enter valid password")
            validation?.addValidation(repeatPasswordEditText, passwordEditText, "Both password should be matching")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        validation = null
        progressDialog = null
    }

    fun showProgressDialog(message: String) {
        progressDialog?.setTitle("Message")
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.show()
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }
}
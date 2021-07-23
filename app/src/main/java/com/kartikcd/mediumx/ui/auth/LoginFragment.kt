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
import com.kartikcd.mediumx.databinding.FragmentLoginBinding
import com.kartikcd.mediumx.util.Resource

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private var validation: AwesomeValidation? = null
    val viewModel: AuthViewModel by activityViewModels()
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentLoginBinding.inflate(layoutInflater, container, false)

        validation = AwesomeValidation(ValidationStyle.BASIC)
        progressDialog = ProgressDialog(activity)

        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addValidation()
        _binding?.apply {
            loginButton.setOnClickListener {
                if (validation!!.validate()) {
                    viewModel.login(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                }
            }
        }

        viewModel.user.observe({ lifecycle })  {
            when(it) {
                is Resource.Success -> {
                    hideProgressDialog()
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
                    showProgressDialog("Logging in user...")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        validation = null
        progressDialog = null
    }

    fun addValidation() {
        _binding?.apply {
            validation?.addValidation(emailEditText, Patterns.EMAIL_ADDRESS, "Please enter valid email address")
            validation?.addValidation(passwordEditText, RegexTemplate.NOT_EMPTY, "password cannot be empty")
        }
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

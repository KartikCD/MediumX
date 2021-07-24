package com.kartikcd.mediumx.ui.profile

import android.app.ProgressDialog
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.kartikcd.api.models.entities.Update
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentProfileBinding
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.extensions.loadImage
import com.kartikcd.mediumx.util.Resource
import java.lang.Exception
import java.net.URL

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private var validation: AwesomeValidation? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val factory = ProfileViewModelFactory(requireActivity().application, MediumXRepository())
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        validation = AwesomeValidation(ValidationStyle.BASIC)

        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addValidation()
        viewModel.getCurrentUser()

        viewModel.user.observe({ lifecycle }) { response ->
            when(response) {
                is Resource.Success -> {
                    _binding?.apply {
                        hideProgressDialog()
                        response.data.let {
                            usernameEditText.setText(it?.user?.username)
                            emailEditText.setText(it?.user?.email)
                            if (it?.user?.bio != null) {
                                bioEditText.setText(it?.user?.bio)
                            }
                            if (it?.user?.image != null) {
                                if (Patterns.WEB_URL.matcher(it?.user?.image).matches()) {
                                    avatarImageView.visibility = View.VISIBLE
                                    avatarImageView.loadImage(it?.user?.image, true)
                                }
                                avatarImageView.visibility = View.GONE
                            } else {
                                avatarImageView.visibility = View.GONE
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    Toast.makeText(activity, response.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    showProgressDialog("Loading profile. Please wait...")
                }
            }
        }

        _binding?.updateButton?.setOnClickListener {
            if (validation!!.validate()) {
                showProgressDialog("Updating profile. Please wait...")
                _binding?.apply {
                    val user = Update(
                        username = usernameEditText.text.toString(),
                        email = emailEditText.text.toString()
                    )
                    if (!imageEditText.text!!.isEmpty()) {
                        println("Debug: Clicked")
                        if (Patterns.WEB_URL.matcher(imageEditText.text.toString()).matches()) {
                            user.image = imageEditText.text.toString()
                        } else {
                            Toast.makeText(activity, "Please enter valid image url", Toast.LENGTH_LONG).show()
                        }
                    }
                    if (!bioEditText.text!!.isEmpty()) {
                        user.bio = bioEditText.text.toString()
                    }
                    viewModel.updateUser(user)
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

    fun isValid(url: String): Boolean {
        try {
            URL(url).toURI()
            return true
        } catch (e: Exception) {
            return false
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

    fun addValidation() {
        _binding?.apply {
            validation?.addValidation(usernameEditText, RegexTemplate.NOT_EMPTY, "Username cannot be empty.")
            validation?.addValidation(emailEditText, Patterns.EMAIL_ADDRESS, "Please enter valid email address.")
        }
    }

}
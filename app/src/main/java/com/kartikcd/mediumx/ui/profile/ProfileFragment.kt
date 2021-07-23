package com.kartikcd.mediumx.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private var validation: AwesomeValidation? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        validation = AwesomeValidation(ValidationStyle.BASIC)

        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addValidation()

        _binding?.updateButton?.setOnClickListener {
            if (validation!!.validate()) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        validation = null
    }

    fun addValidation() {
        _binding?.apply {
            validation?.addValidation(usernameEditText, RegexTemplate.NOT_EMPTY, "Username cannot be empty.")
            validation?.addValidation(emailEditText, Patterns.EMAIL_ADDRESS, "Please enter valid email address.")
        }
    }

}
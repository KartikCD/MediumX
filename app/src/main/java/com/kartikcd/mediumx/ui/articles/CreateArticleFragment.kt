package com.kartikcd.mediumx.ui.articles

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.material.snackbar.Snackbar
import com.kartikcd.api.models.entities.Article
import com.kartikcd.api.models.entities.ArticleBody
import com.kartikcd.api.models.requests.ArticleRequest
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentCreateArticleBinding
import com.kartikcd.mediumx.domain.MediumXRepository
import com.kartikcd.mediumx.util.Resource

class CreateArticleFragment : Fragment() {

    private var _binding: FragmentCreateArticleBinding? = null
    private lateinit var viewModel: ArticleViewModel
    private var validation: AwesomeValidation? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateArticleBinding.inflate(layoutInflater, container, false)
        val factory = ArticleViewModelFactory(requireActivity().application, MediumXRepository())
        viewModel = ViewModelProvider(this, factory).get(ArticleViewModel::class.java)
        validation = AwesomeValidation(ValidationStyle.BASIC)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addValidation()

        _binding?.createArticleButton?.setOnClickListener {
            if (validation!!.validate()) {
                showProgressDialog("Adding article. Please wait...")
                _binding?.apply {
                    val tags = tagsEditText.text?.split("\\s+")
                    val articleRequest = ArticleRequest(
                        ArticleBody(
                            body = bodyEditText.text.toString(),
                            description = descriptionEditText.text.toString(),
                            title = titleEditText.text.toString(),
                            tagList = tags!!
                        )
                    )
                    viewModel.addArticle(articleRequest)
                }
            }
        }

        viewModel.article.observe({ lifecycle }) {
            when(it) {
                is Resource.Loading -> {
                    hideProgressDialog()
                }
                is Resource.Success -> {
                    hideProgressDialog()
                    Snackbar.make(
                        view,
                        "Article added successfully.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    _binding?.apply {
                        titleEditText.setText("")
                        descriptionEditText.setText("")
                        bodyEditText.setText("")
                        tagsEditText.setText("")
                    }
                }
                is Resource.Error -> {
                    hideProgressDialog()
                    Snackbar.make(
                        view,
                        it.message.toString(),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun addValidation() {
        _binding?.apply {
            validation?.addValidation(titleEditText, RegexTemplate.NOT_EMPTY, "Please enter title")
            validation?.addValidation(descriptionEditText, RegexTemplate.NOT_EMPTY, "Please enter title")
            validation?.addValidation(bodyEditText, RegexTemplate.NOT_EMPTY, "Please enter title")
            validation?.addValidation(titleEditText, RegexTemplate.NOT_EMPTY, "Please enter title")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        validation = null
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
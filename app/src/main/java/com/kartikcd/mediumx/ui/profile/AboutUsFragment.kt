package com.kartikcd.mediumx.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kartikcd.mediumx.R
import com.kartikcd.mediumx.databinding.FragmentAboutUsBinding
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutUsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewGroup: ViewGroup =
            inflater.inflate(R.layout.fragment_about_us, container, false) as ViewGroup
        val element = Element().setTitle("I would like to thanks INsanityDesign.com for Realworld.io api and is licensed under MIT. You can checkout the full source at Github.com")
        val aboutPage: View? = AboutPage(activity)
            .isRTL(false)
            .setImage(R.drawable.logo)
            .setDescription("This app is an exemplary project to show how a Medium.com clone (called MediumX) is built using NativeScript to connect to any other backend from realworld api\n\nRealworld example projects have been initiated by Thinkster.io to provide realworld scenario as a tutorial to see how any different backend/frontend programming language and/or framework could work out in your project.")
            .addItem(element)
            .addGroup("Follow")
            .addWebsite("https://realworld.io", "Realworld Website")
            .addGitHub("https://github.com/gothinkster/realworld", "'gothinkster' github")
            .addGroup("Connect with us")
            .addEmail("chawdakartik@gmail.com")
            .addTwitter("chawdakartik1")
            .addGitHub("KartikCD")
            .addInstagram("kartikc_0123")
            .addYoutube("Kartik Chawda")
            .create()
        viewGroup.addView(aboutPage)
        return viewGroup

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
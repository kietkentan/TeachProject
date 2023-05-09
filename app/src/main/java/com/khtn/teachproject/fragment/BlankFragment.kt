package com.khtn.teachproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.khtn.teachproject.R
import com.khtn.teachproject.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        textView = view.findViewById(R.id.text_view)
        textView.setOnClickListener {
            viewModel.login("", "", save = false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { isLogin ->
            when (isLogin) {
                true -> {
                    textView.text = "Login Success"
                }
                false -> {
                    textView.text = "Login Fail"
                }
            }
        }
    }
}
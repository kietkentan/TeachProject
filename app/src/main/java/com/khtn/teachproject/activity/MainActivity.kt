package com.khtn.teachproject.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.khtn.teachproject.R
import com.khtn.teachproject.helper.LocaleHelper
import com.khtn.teachproject.helper.SupportLanguage


class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private var bundle: Bundle? = null

    private var isEnglish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (bundle != null)
            super.onCreate(bundle)
        else
            super.onCreate(savedInstanceState)

        // Order is important ;)
        loadLanguage()
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)

        isEnglish = checkEnglish()

        button.setOnClickListener {
            // Change Locale
            LocaleHelper.setLocale(
                this@MainActivity,
                if (isEnglish) SupportLanguage.VIETNAM.lang
                else SupportLanguage.ENGLISH.lang
            )
            updateUi()
        }
    }

    private fun updateUi() {
        // Order is important ;)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun loadLanguage() {
        LocaleHelper.loadLanguageConfig(this@MainActivity)
    }

    private fun checkEnglish(): Boolean {
        val str = LocaleHelper.getLanguage(this@MainActivity)
        if (str == "en")
            return true
        return false
    }
}
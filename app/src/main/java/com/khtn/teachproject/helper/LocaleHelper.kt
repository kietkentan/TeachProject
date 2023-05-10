@file:Suppress("DEPRECATION")

package com.khtn.teachproject.helper

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.khtn.teachproject.utils.SharedPrefConstants
import java.util.*
import kotlin.jvm.internal.Intrinsics

object LocaleHelper {
    fun onAttach(context: Context): Context {
        return setLocale(context, getPersistedData(context))
    }

    fun getLanguage(context: Context?): String {
        Intrinsics.checkNotNullParameter(context, "context")
        return getPersistedData(context!!)
    }

    fun setLocale(
        context: Context,
        language: String
    ): Context {
        persistLanguage(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            updateResources(context, language)
        else
            updateResourcesLegacy(context, language)
    }

    private fun persistLanguage(
        context: Context,
        language: String
    ) {
        SharedPreferencesManager(context).saveByKey(SharedPrefConstants.LANGUAGE, language)
    }

    private fun getPersistedData(context: Context): String {
        return SharedPreferencesManager(context).retrieveByKey(SharedPrefConstants.LANGUAGE)
            ?: return SupportLanguage.VIETNAMESE.lang
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(
        context: Context,
        language: String
    ): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(
        context: Context,
        language: String
    ): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun loadLanguageConfig(context: Context) {
        val str = getPersistedData(context)
        if (str == SupportLanguage.ENGLISH.lang) {
            setLocale(context, SupportLanguage.ENGLISH.lang)
        } else {
            setLocale(context, SupportLanguage.VIETNAMESE.lang)
        }
    }
}
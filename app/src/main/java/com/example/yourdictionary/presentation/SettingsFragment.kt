package com.example.yourdictionary.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yourdictionary.R
import com.example.yourdictionary.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding ?: throw RuntimeException("FragmentSettingsBinding == null")
    private var radioLanguage: Int = NOT_SELECTED
    private var radioStyle: Int = NOT_SELECTED


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parseParams()
        setRGListener()
    }

    private fun setRGListener(){
        binding.rgLanguages.setOnCheckedChangeListener{ _, checkedId ->
            val language = when(checkedId){
                R.id.lngEnglish -> ENGLISH
                R.id.lngFrench -> FRENCH
                R.id.lngGermany -> GERMANY
                else -> throw RuntimeException("unknown radio button")
            }
            val settings =
                requireContext().getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putString(LANGUAGE_MODE, language)
            editor.apply()
            viewModel.setCurrentLanguage(language)
        }
        binding.rgStyles.setOnCheckedChangeListener{ _, checkedId ->
            val style = when(checkedId){
                R.id.rbRusFirst -> {
                    viewModel.switchLanguageToRussian()
                    FROM_RUSSIAN
                }
                R.id.rbForeignFirst -> {
                    viewModel.switchLanguageToForeign()
                    TO_RUSSIAN
                }
                else -> throw RuntimeException("unknown radio button")
            }
            val settings =
                requireContext().getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
            val editor = settings.edit()
            editor.putString(STYLE_MODE, style)
            editor.apply()
        }
    }


    private fun parseParams(){
        val args = requireArguments()
        if(!args.containsKey(LANGUAGE_MODE)){
            throw RuntimeException("Param language mode is absent")
        }
        if(!args.containsKey(STYLE_MODE)){
            throw RuntimeException("Param view mode is absent")
        }
        radioLanguage = when(args.getString(LANGUAGE_MODE)){
            ENGLISH -> R.id.lngEnglish
            FRENCH -> R.id.lngFrench
            GERMANY -> R.id.lngGermany
            else ->  throw RuntimeException("Param language mode is absent")
        }
        radioStyle = when(args.getString(STYLE_MODE)){
            FROM_RUSSIAN -> R.id.rbRusFirst
            TO_RUSSIAN -> R.id.rbForeignFirst
            else -> throw RuntimeException("Param style mode is absent")
        }
        binding.rgLanguages.check(radioLanguage)
        binding.rgStyles.check(radioStyle)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {

        private const val LANGUAGE_MODE = "language_mode"
        private const val STYLE_MODE = "style_mode"
        private const val SETTINGS_STORAGE = "settings_storage"
        private const val NOT_SELECTED = 0
        private lateinit var viewModel: MainViewModel
        const val ENGLISH = "en"
        const val FRENCH = "fr"
        const val GERMANY = "de"
        const val FROM_RUSSIAN = "from_russian"
        const val TO_RUSSIAN = "to_russian"

        fun newInstance(context: Context, vm: MainViewModel): SettingsFragment {
            viewModel = vm
            val settings: SharedPreferences =
                context.getSharedPreferences(SETTINGS_STORAGE, Context.MODE_PRIVATE)
            val chooseLanguageMode = settings.getString(LANGUAGE_MODE, ENGLISH)
            val chooseViewMode = settings.getString(STYLE_MODE, TO_RUSSIAN)

            return SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(LANGUAGE_MODE, chooseLanguageMode)
                    putString(STYLE_MODE, chooseViewMode)
                }
            }
        }
    }
}
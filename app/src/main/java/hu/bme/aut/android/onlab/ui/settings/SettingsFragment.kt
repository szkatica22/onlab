package hu.bme.aut.android.onlab.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(){

    companion object {
        private const val KEY_THEME = "AppTheme"
        private const val RED = R.style.AppTheme_Red
        private const val ORANGE = R.style.AppTheme_Orange
        private const val YELLOW = R.style.AppTheme_Yellow
        private const val GREEN = R.style.AppTheme_Green
        private const val BLUE = R.style.AppTheme_Blue
        private const val PURPLE = R.style.AppTheme_Purple
    }

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

//    private var navC: NavController? = null
    private var currentTheme = PURPLE

    private val binding get() = _binding!!

//    var sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//    private var name: String? = null
//    private var mode: Boolean = false
//    private var theme: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentTheme = PreferenceManager.getDefaultSharedPreferences(this.context).getInt(KEY_THEME, PURPLE)
        super.onCreateView(inflater, container, savedInstanceState)

        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_settingsFragment)
        }

//        val appSettingPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("AppSettingsPref", 0)
//        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
//        val night_mode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

//        if(night_mode){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }

        loadSettings(/*night_mode, sharedPrefsEdit*/)

        return root
    }

    fun loadSettings(/*night_mode: Boolean, sharedPrefsEdit: SharedPreferences.Editor*/) {

//        val appSettingPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("AppSettingsPref", 0)
//        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
//        val night_mode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val name: String? = sp.getString("name", "")
//        name = sp.getString("name", "")
//        val picture: Picture?
        val mode: Boolean = sp.getBoolean("mode", false)
//        mode = sp.getBoolean("mode", false)
        val theme: String? = sp.getString("theme", "purple")
//        theme = sp.getString("theme", "purple")

        binding.tvName.text = "Name: $name"
        if(!mode){
            binding.tvMode.text = "Dark mode: off"
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            delegate.applyDayNight()
        } else {
            binding.tvMode.text = "Dark mode: on"
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            delegate.applyDayNight()
        }

//        if(night_mode){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            sharedPrefsEdit.putBoolean("NightMode", false)
//            sharedPrefsEdit.apply()
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            sharedPrefsEdit.putBoolean("NightMode", true)
//            sharedPrefsEdit.apply()
//        }


        binding.tvTheme.text = "Theme: $theme"
        currentTheme = when(theme){
            "red" -> RED
            "orange" -> ORANGE
            "yellow" -> YELLOW
            "green" -> GREEN
            "blue" -> BLUE
            else -> PURPLE
        }
        layoutInflater.context.setTheme(currentTheme)
//        layoutInflater.context.setTheme(currentTheme)
//        PreferenceManager.getDefaultSharedPreferences(this.context).edit()
//            .putInt(KEY_THEME, currentTheme).apply()
//        Log.d("THEME:", currentTheme.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
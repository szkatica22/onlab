package hu.bme.aut.android.onlab.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.User
import hu.bme.aut.android.onlab.databinding.FragmentSettingsBinding
import java.util.*

class SettingsFragment : Fragment(){

//    companion object {
//        const val KEY_THEME = "AppTheme"
//        private const val RED = R.style.AppTheme_Red
//        private const val ORANGE = R.style.AppTheme_Orange
//        private const val YELLOW = R.style.AppTheme_Yellow
//        private const val GREEN = R.style.AppTheme_Green
//        private const val BLUE = R.style.AppTheme_Blue
//        const val PURPLE = R.style.AppTheme_Purple
//    }

    companion object {
        val ENG = "en"
        val HUN = "hu"
    }

    val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    var name: String? = null
    var mode: Boolean = false
    var theme: String = "purple"
    var language: String = ENG

//    private var navC: NavController? = null
//    private var currentTheme = PURPLE

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        currentTheme = PreferenceManager.getDefaultSharedPreferences(this.context).getInt(KEY_THEME, PURPLE)
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

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
//        db.collection("users").whereEqualTo("email", firebaseUser?.email).get().
//        addOnSuccessListener { snapshots ->
//            if (snapshots != null) {
//                if (snapshots.documents.isNotEmpty()) {
//                    name = snapshots.documents[0]["name"].toString()
//                    binding.tvName.text = "Name: ${snapshots.documents[0]["name"]}"
//                    if(snapshots.documents[0]["mode"] == false){
//                        binding.tvMode.text = "Dark mode: off"
//
//                    } else {
//                        binding.tvMode.text = "Dark mode: on"
//                    }
//                    binding.tvTheme.text = "Theme: ${snapshots.documents[0]["theme"]}"
//                }
//            }
//        }
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)
        name = sp.getString("name", "")
        mode = sp.getBoolean("mode", false)
        theme = sp.getString("theme", "purple")!!
        language = sp.getString("language", "en")!!


        //Change Application Language
        setLocate()

        loadSettings()
        saveSettings()

    }

    fun setLocate(){

        val locale = Locale(language)
        val configuration: Configuration? = context?.resources?.configuration
//        configuration?.locale = locale
        configuration?.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        val cont = configuration?.let { context?.createConfigurationContext(it) }

//        val locale = Locale(language)
//        val config = Configuration()

//        Locale.setDefault(locale)
//        config.locale = locale
//        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
//
//        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
//        editor.putString("language", language)
//        editor.apply()
    }

    fun saveSettings(){
//        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//        val name: String? = sp.getString("name", "")
////        name = sp.getString("name", "")
////        val picture: Picture?
//        val mode: Boolean = sp.getBoolean("mode", false)
////        mode = sp.getBoolean("mode", false)
//        val theme: String? = sp.getString("theme", "purple")

        db.collection("users").whereEqualTo("email", firebaseUser?.email).get().
        addOnSuccessListener { snapshots ->
            if (snapshots != null) {
                if(snapshots.documents.isNotEmpty()){
                    // Update user informations
                    db.collection("users").document(snapshots.documents[0].id).
                    update("name", name, "theme", theme, "language", language, "mode", mode)
                    Toast.makeText(this.context, "Changes saved", Toast.LENGTH_SHORT).show()

                } else{
                    // Save new user infromation
                    val new_usr = User(firebaseUser?.email, name, null, theme, language, mode)
                    db.collection("users").add(new_usr)
                    Toast.makeText(this.context, "User informations saved", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun loadSettings(/*night_mode: Boolean, sharedPrefsEdit: SharedPreferences.Editor*/) {

//        val appSettingPrefs: SharedPreferences = this.requireActivity().getSharedPreferences("AppSettingsPref", 0)
//        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()
//        val night_mode: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)

//        val name: String? = sp.getString("name", "")
////        val picture: Picture?
//        val mode: Boolean = sp.getBoolean("mode", false)
//        val theme: String? = sp.getString("theme", "purple")

        db.collection("users").whereEqualTo("email", firebaseUser?.email).get().
        addOnSuccessListener { snapshots ->
            if (snapshots != null) {
                if (snapshots.documents.isNotEmpty()) {
                    binding.tvName.text = "Name: ${snapshots.documents[0]["name"]}"
                    if(snapshots.documents[0]["mode"] == false){
                        binding.tvMode.text = "Dark mode: off"

                    } else {
                        binding.tvMode.text = "Dark mode: on"
                    }
                    binding.tvTheme.text = "Theme: ${snapshots.documents[0]["theme"]}"
                    binding.tvLanguage.text = snapshots.documents[0]["language"].toString()
                }
            }
        }


//        binding.tvName.text = "Name: ${sp.getString("name", "")}"
//        if(!sp.getBoolean("mode", false)){
//            binding.tvMode.text = "Dark mode: off"
////            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
////            delegate.applyDayNight()
//        } else {
//            binding.tvMode.text = "Dark mode: on"
////            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
////            delegate.applyDayNight()
//        }

//        if(night_mode){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            sharedPrefsEdit.putBoolean("NightMode", false)
//            sharedPrefsEdit.apply()
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            sharedPrefsEdit.putBoolean("NightMode", true)
//            sharedPrefsEdit.apply()
//        }


//        binding.tvTheme.text = "Theme: ${sp.getString("theme", "purple")}"

//        currentTheme = when(theme){
//            "red" -> RED
//            "orange" -> ORANGE
//            "yellow" -> YELLOW
//            "green" -> GREEN
//            "blue" -> BLUE
//            else -> PURPLE
//        }
//        sp.edit().putInt(KEY_THEME, currentTheme).apply()
//        requireActivity().recreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package hu.bme.aut.android.onlab.ui.settings

import android.content.SharedPreferences
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
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.User
import hu.bme.aut.android.onlab.databinding.FragmentSettingsBinding

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

    val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

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

    fun saveSettings(){
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val name: String? = sp.getString("name", "")
//        name = sp.getString("name", "")
//        val picture: Picture?
        val mode: Boolean = sp.getBoolean("mode", false)
//        mode = sp.getBoolean("mode", false)
        val theme: String? = sp.getString("theme", "purple")

        db.collection("users").whereEqualTo("email", firebaseUser?.email).
        addSnapshotListener { snapshots, error ->
            if (error != null){
                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
            }
            if (snapshots != null) {
                if(snapshots.documents.isNotEmpty()){
                    // Update user informations
                    db.collection("users").document(snapshots.documents[0].id).
                    update("name", name, "theme", theme, "mode", mode)


                } else{
                    // Save new user infromation
                    val new_usr = User(firebaseUser?.email, name, null, theme, mode)
                    db.collection("users").add(new_usr)
                }
            }
        }

    }

    fun loadSettings(/*night_mode: Boolean, sharedPrefsEdit: SharedPreferences.Editor*/) {

        saveSettings()

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
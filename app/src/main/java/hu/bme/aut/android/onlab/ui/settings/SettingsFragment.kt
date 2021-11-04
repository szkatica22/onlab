package hu.bme.aut.android.onlab.ui.settings

import android.content.SharedPreferences
import android.graphics.Picture
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(){
    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

//    private var navC: NavController? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_settingsFragment)
        }

        loadSettings()

        return root
    }

    fun loadSettings() {

        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val name: String? = sp.getString("name", "")
//        val picture: Picture?
        val mode: Boolean = sp.getBoolean("mode", false)
        val theme: String? = sp.getString("theme", "purple")

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
        binding.tvTheme.text = "Theme: $theme"


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
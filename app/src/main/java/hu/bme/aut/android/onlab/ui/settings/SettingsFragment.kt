package hu.bme.aut.android.onlab.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.onlab.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment()/*, View.OnClickListener */{
    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    //private var navC: NavController? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // Teszt gomb a kovi fragmentre valtashoz
//        binding.testButton.setOnClickListener{
////            val action = ChangeSettingsFragment.actionNavSettingsToNavChangeSettings()
//            findNavController().navigate(R.id.action_nav_settings_to_nav_change_settings)
//        }

        return root
    }

    // Masodik probalkozas
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        navC = Navigation.findNavController(view)
//        view.findViewById<Button>(R.id.test_button).setOnClickListener(this)
//    }
//
//    override fun onClick(v: View?){
//        navC?.navigate(R.id.action_nav_settings_to_nav_change_settings)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
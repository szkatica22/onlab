package hu.bme.aut.android.onlab.ui.flag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentFlagBinding

class FlagFragment : Fragment(){
    private lateinit var flagViewModel: FlagViewModel
    private var _binding: FragmentFlagBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flagViewModel =
            ViewModelProvider(this).get(FlagViewModel::class.java)

        _binding = FragmentFlagBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFlag
        flagViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // Teszt gomb a kovi fragmentre valtashoz
        binding.testButton6.setOnClickListener{
            findNavController().navigate(R.id.action_nav_flag2_to_nav_new_recipie)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
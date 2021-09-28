package hu.bme.aut.android.onlab.ui.new_flag

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
import hu.bme.aut.android.onlab.databinding.FragmentNewFlagBinding

class NewFlagFragment : Fragment(){
    private lateinit var newFlagViewModel: NewFlagViewModel
    private var _binding: FragmentNewFlagBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newFlagViewModel =
            ViewModelProvider(this).get(NewFlagViewModel::class.java)

        _binding = FragmentNewFlagBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNewFlag
        newFlagViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // Teszt gomb a kovi fragmentre valtashoz
        binding.testButton4.setOnClickListener{
            findNavController().navigate(R.id.action_nav_new_flag_to_nav_recipie2)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package hu.bme.aut.android.onlab.ui.recipies

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
import hu.bme.aut.android.onlab.databinding.FragmentRecipiesBinding

class RecipiesFragment : Fragment() {

    private lateinit var recipiesViewModel: RecipiesViewModel
    private var _binding: FragmentRecipiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipiesViewModel =
            ViewModelProvider(this).get(RecipiesViewModel::class.java)

        _binding = FragmentRecipiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRecipies
        recipiesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // Teszt gomb a kovi fragmentre valtashoz
        binding.testButton5.setOnClickListener{
            findNavController().navigate(R.id.action_nav_recipies_to_nav_new_flag)
        }
        // Teszt gomb a kovi fragmentre valtashoz
        binding.testButton7.setOnClickListener{
            findNavController().navigate(R.id.action_nav_recipies_to_nav_flag2)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
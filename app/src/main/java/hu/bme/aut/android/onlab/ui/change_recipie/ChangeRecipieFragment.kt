package hu.bme.aut.android.onlab.ui.change_recipie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.android.onlab.databinding.FragmentChangeRecipieBinding
import hu.bme.aut.android.onlab.databinding.FragmentRecipieBinding

class ChangeRecipieFragment : Fragment(){
    private lateinit var changeRecipieViewModel: ChangeRecipieViewModel
    private var _binding: FragmentChangeRecipieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changeRecipieViewModel =
            ViewModelProvider(this).get(ChangeRecipieViewModel::class.java)

        _binding = FragmentChangeRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textChangeRecipie
        changeRecipieViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
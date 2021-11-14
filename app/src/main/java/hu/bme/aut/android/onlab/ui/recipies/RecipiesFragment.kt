package hu.bme.aut.android.onlab.ui.recipies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.databinding.FragmentRecipiesBinding
import hu.bme.aut.android.onlab.ui.change_recipie.ChangeItem

class RecipiesFragment : Fragment() {

    private lateinit var recipiesViewModel: RecipiesViewModel
    private var _binding: FragmentRecipiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var flagitemAdapter: FlagItemAdapter

    var flag_list = mutableListOf(FlagItem("Drinks"), FlagItem("Soups"), FlagItem("Main courses"), FlagItem("Desserts"))

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

        flagitemAdapter = FlagItemAdapter(flag_list)
        binding.rvFlags.adapter = flagitemAdapter
        binding.rvFlags.layoutManager = LinearLayoutManager(this.context)

        // Teszt gomb a kovi fragmentre valtashoz
        binding.fltBtnAddNewFlag.setOnClickListener{
//            findNavController().navigate(R.id.action_nav_recipies_to_nav_new_flag)
            val v = inflater.inflate(R.layout.add_flag, null)
            val flag = v.findViewById<EditText>(R.id.et_new_flag_name)
            val add_dialog = AlertDialog.Builder(inflater.context)
            add_dialog.setView(v)
            add_dialog.setPositiveButton("Ok"){
                    dialog,_->
                addFlag(FlagItem(flag.text.toString()))
                Toast.makeText(inflater.context, "Adding new flag", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            add_dialog.setNegativeButton("Cancel"){
                    dialog,_->
                Toast.makeText(inflater.context, "Cancel", Toast.LENGTH_SHORT).show()
            }
            add_dialog.create()
            add_dialog.show()
        }
//        // Teszt gomb a kovi fragmentre valtashoz
//        binding.testButton7.setOnClickListener{
//            findNavController().navigate(R.id.action_nav_recipies_to_nav_flag2)
//        }


        return root
    }

    fun addFlag(flag: FlagItem){
        flag_list.add(flag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
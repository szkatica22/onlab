package hu.bme.aut.android.onlab.ui.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.onlab.databinding.FragmentShoppingListBinding

class ShoppinglistFragment : Fragment() {

    private lateinit var shoppinglistViewModel: ShoppinglistViewModel
    private var _binding: FragmentShoppingListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var listitemAdapter: ListItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shoppinglistViewModel =
            ViewModelProvider(this).get(ShoppinglistViewModel::class.java)

        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textShoppinglist
//        shoppinglistViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        listitemAdapter = ListItemAdapter(mutableListOf())
        binding.rvShoppingList.adapter = listitemAdapter
        binding.rvShoppingList.layoutManager = LinearLayoutManager(this.context)

        binding.btnShoppingListAddItem.setOnClickListener {
            val shoppingTitle = binding.etShoppingTitle.text.toString()
//            Log.d("FRAGMENT-ADD-title:", "$shoppingTitle")
            if(shoppingTitle.isNotEmpty()){
                val list_item = ListItem(shoppingTitle)
//                Log.d("ADD-ListItem: ", "$list_item")
                listitemAdapter.addItem(list_item)
                Log.d("ADD-ADAPTER: ", "${listitemAdapter.getItems()}")
                binding.etShoppingTitle.text.clear()
            }
        }

        binding.clearTheListBtn.setOnClickListener {
            listitemAdapter.deletePurchasedItems()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
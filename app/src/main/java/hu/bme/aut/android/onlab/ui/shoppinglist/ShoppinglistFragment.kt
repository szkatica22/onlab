package hu.bme.aut.android.onlab.ui.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Flag
import hu.bme.aut.android.onlab.data.ShoppingItem
import hu.bme.aut.android.onlab.databinding.FragmentShoppingListBinding

class ShoppinglistFragment : Fragment() {

    private lateinit var shoppinglistViewModel: ShoppinglistViewModel
    private var _binding: FragmentShoppingListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var listitemAdapter: ListItemAdapter
    private val db = Firebase.firestore

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

        listitemAdapter = ListItemAdapter(this.context)
        binding.rvShoppingList.adapter = listitemAdapter
        binding.rvShoppingList.layoutManager = LinearLayoutManager(this.context)

        binding.btnShoppingListAddItem.setOnClickListener {
            val shoppingTitle = binding.etShoppingTitle.text.toString()
            if(shoppingTitle.isNotEmpty()){
                val list_item = ShoppingItem(shoppingTitle, checked = false)

                // Upload to Firebase
                uploadItem(list_item, inflater)
//                listitemAdapter.addItem(list_item)

                binding.etShoppingTitle.text.clear()
            }
        }

        binding.clearTheListBtn.setOnClickListener { 
//            listitemAdapter.deletePurchasedItems()
            deleteItem(inflater)
        }
        initFlagListener()

        return root
    }

    fun uploadItem(new_item: ShoppingItem, inflater: LayoutInflater){
        db.collection("shopping_list").add(new_item).addOnSuccessListener {
            Toast.makeText(inflater.context, "Item created", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(inflater.context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    fun deleteItem(inflater: LayoutInflater){

        db.collection("shopping_list").whereEqualTo("checked", true).
        addSnapshotListener { snapshot, e ->
            if(e != null){
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
            if(snapshot != null){
                for(doc in snapshot.documents){
                    db.collection("shopping_list").document(doc.id).delete()
                    Toast.makeText(inflater.context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun initFlagListener() {
        db.collection("shopping_list").
        addSnapshotListener { snapshots, error ->
            if (error != null){
                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documentChanges) {
                when(dc.type) {
                    com.google.firebase.firestore.DocumentChange.Type.ADDED -> listitemAdapter.addItem(dc.document.toObject<ShoppingItem>())
//                    com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> listitemAdapter.changeChecked(dc.document.toObject<ShoppingItem>())
                    com.google.firebase.firestore.DocumentChange.Type.REMOVED -> listitemAdapter.deletePurchasedItems()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
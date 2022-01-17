package hu.bme.aut.android.onlab.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var searchitemAdapter: SearchItemAdapter
    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var search_text: String

    // TODO: itt a kodot atirni, hogy azokat a recepteket jelenitse meg, amik a textviewban levo szoveghez passzolnak es az adott felhasznalohoz tartoznak

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchitemAdapter = SearchItemAdapter(this.context)
        binding.rvSearchList.adapter = searchitemAdapter
        binding.rvSearchList.layoutManager = LinearLayoutManager(this.context)

//        binding.btnShoppingListAddItem.setOnClickListener {
//            val shoppingTitle = binding.etShoppingTitle.text.toString()
//            if(shoppingTitle.isNotEmpty()){
//                val search_item = Recipie()
//                val list_item = ShoppingItem(shoppingTitle, author = firebaseUser?.email, checked = false)
//
//                // Upload to Firebase
//                uploadItem(list_item)
//
//                binding.etShoppingTitle.text.clear()
//            }
//        }
        binding.btnSearch.setOnClickListener {
            search_text = binding.etSearchTitle.text.toString()
            searchitemAdapter.clearList()
            if(search_text != ""){
                initFlagListener()
//                db.collection("recipies").whereEqualTo("author", firebaseUser?.email)
//                    .get().addOnSuccessListener { querySnapshots ->
//                        if (querySnapshots != null){
//                            if (querySnapshots.documents.isNotEmpty()){
//                                for (doc in querySnapshots.documents){
//                                    val rec_name = doc["name"].toString().lowercase()
//                                    if(rec_name.contains(search_text)){
//                                        list += doc["name"].toString()
//                                    }
//                                }
//                            }
//                        }
//                    }
            }
        }

//        initFlagListener()

        return root
    }

    fun initFlagListener() {
        // TODO: Ide attenni azt a vizsgalatot amit fent csinaltam...
        db.collection("recipies").whereEqualTo("author", firebaseUser?.email)
            .get().addOnSuccessListener { querySnapshots ->
                if (querySnapshots != null){
                    if (querySnapshots.documents.isNotEmpty()){
                        for (dc in querySnapshots!!.documentChanges){
                            val rec_name = dc.document["name"].toString().lowercase()
                            if(rec_name.contains(search_text)){
                                when(dc.type) {
                                    com.google.firebase.firestore.DocumentChange.Type.ADDED -> searchitemAdapter.addItem(dc.document.toObject<Recipie>())
//                                    com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> listitemAdapter.changeChecked(dc.document.toObject<ShoppingItem>())
//                                    com.google.firebase.firestore.DocumentChange.Type.REMOVED -> listitemAdapter.deletePurchasedItems()
                                }
                            }
                        }
                    }
                }
            }

//        db.collection("recipies").
//        addSnapshotListener { snapshots, error ->
//            if (error != null){
//                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
//                return@addSnapshotListener
//            }
//
//            for (dc in snapshots!!.documentChanges) {
//                if (dc.document["author"] == firebaseUser?.email){
//                    when(dc.type) {
////                        com.google.firebase.firestore.DocumentChange.Type.ADDED -> listitemAdapter.addItem(dc.document.toObject<Recipie>())
////                    com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> listitemAdapter.changeChecked(dc.document.toObject<ShoppingItem>())
////                        com.google.firebase.firestore.DocumentChange.Type.REMOVED -> listitemAdapter.deletePurchasedItems()
//                    }
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
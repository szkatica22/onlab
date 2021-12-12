package hu.bme.aut.android.onlab.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentFavouritesBinding

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel
    private var _binding: FragmentFavouritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val fav = true

    private lateinit var favouriteItemAdapter: FavouriteItemAdapter

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
            ViewModelProvider(this).get(FavouritesViewModel::class.java)

        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favouriteItemAdapter = FavouriteItemAdapter()
        binding.rvFavourites.adapter = favouriteItemAdapter
        binding.rvFavourites.layoutManager = LinearLayoutManager(this.context)

        initFavouriteListener()

        return root
    }

    fun initFavouriteListener(){
        db.collection("recipies").whereEqualTo("favourite", fav).
        addSnapshotListener { snapshots, error ->
            if (error != null){
                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if(snapshots != null){
                if(snapshots.documents.isNotEmpty()){
                    for (dc in snapshots.documentChanges) {
                        if(dc.document["author"] == firebaseUser?.email){
                            when(dc.type) {
                                com.google.firebase.firestore.DocumentChange.Type.ADDED -> favouriteItemAdapter.addRecipie(dc.document.toObject<Recipie>())
//                            com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> Toast.makeText(this.context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
//                            com.google.firebase.firestore.DocumentChange.Type.REMOVED -> Toast.makeText(this.context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package hu.bme.aut.android.onlab.ui.shares

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
import hu.bme.aut.android.onlab.databinding.FragmentSharesBinding

class SharesFragment : Fragment() {

    private lateinit var sharesViewModel: SharesViewModel
    private var _binding: FragmentSharesBinding? = null

    private val binding get() = _binding!!

    private lateinit var sharedItemAdapter: SharedItemAdapter

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharesViewModel =
            ViewModelProvider(this).get(SharesViewModel::class.java)

        _binding = FragmentSharesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedItemAdapter = SharedItemAdapter()
        binding.rvShares.adapter = sharedItemAdapter
        binding.rvShares.layoutManager = LinearLayoutManager(this.context)

        initSharesListener()

        return root
    }

    fun initSharesListener(){
        db.collection("recipies").whereNotEqualTo("author", firebaseUser?.email).
        addSnapshotListener { snapshots, error ->
            if(error != null){
                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if(snapshots != null){
                if(snapshots.documents.isNotEmpty()){
                    for(doc in snapshots.documentChanges){
                        if(doc.document["shares"].toString().contains(firebaseUser?.email.toString())){
                            when(doc.type) {
                                com.google.firebase.firestore.DocumentChange.Type.ADDED -> sharedItemAdapter.addRecipie(doc.document.toObject<Recipie>())
                                com.google.firebase.firestore.DocumentChange.Type.REMOVED -> sharedItemAdapter.deleteRecipie(doc.document.toObject<Recipie>())
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
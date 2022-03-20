package hu.bme.aut.android.onlab.ui.flag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.asMavericksArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.RecipeArgs
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentFlagBinding

class FlagFragment : Fragment(){
    private lateinit var flagViewModel: FlagViewModel
    private var _binding: FragmentFlagBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recipieitemAdapter: RecipieItemAdapter

    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flagViewModel =
            ViewModelProvider(this).get(FlagViewModel::class.java)

        _binding = FragmentFlagBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get data from previous Fragment
        val flag = this.arguments?.get("flag").toString()

        recipieitemAdapter = RecipieItemAdapter(this.context)
        binding.rvRecipies.adapter = recipieitemAdapter
        binding.rvRecipies.layoutManager = LinearLayoutManager(this.context)

        val textView: TextView = binding.tvFlagName
        flagViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = flag
        })

        // New recipie gomb
        binding.fltBtnNewRecipie.setOnClickListener {
            val args = RecipeArgs("").asMavericksArgs()
            findNavController().navigate(R.id.action_nav_flag2_to_nav_new_recipie, args)
        }

        // Delete gomb - elozo fragmentre lep at
        binding.btnDeleteFlag.setOnClickListener{
            deleteFlag(flag)
            findNavController().navigate(R.id.action_nav_flag_to_nav_recipies)
        }
        initRecipieListener(flag)

        return root
    }

    fun deleteFlag(flag: String){
        // Update recipies
        db.collection("recipies").whereArrayContains("flags", flag).get().
        addOnSuccessListener { snapshots ->
            if(snapshots != null && snapshots.documents.isNotEmpty()){
                for(doc in snapshots.documents){
                    if(doc["author"] == firebaseUser?.email){
                        // Delete extra flag
                        var tmp = doc.data?.get("flags").toString()
                        tmp = tmp.drop(1)
                        tmp = tmp.dropLast(1)
                        var tmp_flags = tmp.split(", ")
                        tmp_flags = tmp_flags.minus(flag)

                        // Update recipie - delete the extra flag from recipie's flags
                        db.collection("recipies").document(doc.id).update("flags", tmp_flags)
                    }
                }
            }
        }

        // Delete Flag
        db.collection("flags").whereEqualTo("name", flag).get().
        addOnSuccessListener { snapshot ->
            if(snapshot != null){
                for(doc in snapshot.documents){
                    if(doc["creator"] == firebaseUser?.email){
                        db.collection("flags").document(doc.id).delete()
                        Toast.makeText(this.context, "Flag deleted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun initRecipieListener(flag: String) {

        db.collection("recipies").whereArrayContains("flags", flag).
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
                                com.google.firebase.firestore.DocumentChange.Type.ADDED -> recipieitemAdapter.addRecipie(dc.document.toObject<Recipie>())
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
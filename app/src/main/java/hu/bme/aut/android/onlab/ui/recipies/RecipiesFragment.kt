package hu.bme.aut.android.onlab.ui.recipies

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Flag
import hu.bme.aut.android.onlab.databinding.FragmentRecipiesBinding

class RecipiesFragment : Fragment() {

    private lateinit var recipiesViewModel: RecipiesViewModel
    private var _binding: FragmentRecipiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var flagitemAdapter: FlagItemAdapter
    private val db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

//    var flag_list = mutableListOf<Flag>() //mutableListOf(Flag("Drinks"), Flag("Soups"), Flag("Main courses"), Flag("Desserts"))

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

        flagitemAdapter = FlagItemAdapter(this.context)
        binding.rvFlags.adapter = flagitemAdapter
        binding.rvFlags.layoutManager = LinearLayoutManager(this.context)

        binding.fltBtnSearch.setOnClickListener{
            findNavController().navigate(R.id.action_nav_recipies_to_searchFragment)
        }

        binding.fltBtnAddNewFlag.setOnClickListener{
            val v = inflater.inflate(R.layout.add_flag, null)
            val flag = v.findViewById<EditText>(R.id.et_new_flag_name)
            val add_dialog = AlertDialog.Builder(inflater.context)
            add_dialog.setView(v)
            add_dialog.setPositiveButton("Ok"){
                    dialog,_->
                val new_flag = Flag(flag.text.toString(), firebaseUser?.email)

                // Check flag already exists or not
                db.collection("flags").whereEqualTo("name", new_flag.name).get().
                addOnSuccessListener { querySnapshots ->
                    if (querySnapshots != null) {
                        if(querySnapshots.documents.isNotEmpty()){
                            if(querySnapshots.documents[0]["creator"] == firebaseUser?.email){
                                Toast.makeText(this.context, "Flag already exist!", Toast.LENGTH_SHORT).show()
                            }
                        } else{
                                // Save to Firebase (before that i would like to check the author, just in case)
                                if(new_flag.creator == firebaseUser?.email){
                                    uploadFlag(new_flag)
                                    Toast.makeText(this.context, "Adding new flag", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
                dialog.dismiss()
            }
            add_dialog.setNegativeButton("Cancel"){
                    dialog,_->
                Toast.makeText(this.context, "Cancel", Toast.LENGTH_SHORT).show()
            }
            add_dialog.create()
            add_dialog.show()
        }

        initFlagListener()

        return root
    }

    fun uploadFlag(new_flag: Flag){
        if(new_flag.creator == firebaseUser?.email){ // check it, just in case
            db.collection("flags").add(new_flag).addOnSuccessListener {
                Toast.makeText(this.context, "Flag created", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun initFlagListener() {
        db.collection("flags").
        addSnapshotListener { snapshots, error ->
            if (error != null){
                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documentChanges) {
                if(dc.document["creator"] == firebaseUser?.email) {
                    when (dc.type) {
                        com.google.firebase.firestore.DocumentChange.Type.ADDED -> flagitemAdapter.
                        addFlag(dc.document.toObject<Flag>())
                        com.google.firebase.firestore.DocumentChange.Type.REMOVED -> flagitemAdapter.
                        deleteFlag(dc.document.toObject<Flag>())
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
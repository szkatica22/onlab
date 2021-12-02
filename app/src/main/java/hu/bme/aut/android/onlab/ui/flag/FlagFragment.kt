package hu.bme.aut.android.onlab.ui.flag

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.R
import hu.bme.aut.android.onlab.data.Flag
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
//        Log.d("FLAG DATA: ", flag?.get("flag").toString())

        recipieitemAdapter = RecipieItemAdapter(this.context)
        binding.rvRecipies.adapter = recipieitemAdapter
        binding.rvRecipies.layoutManager = LinearLayoutManager(this.context)

        val textView: TextView = binding.tvFlagName
        flagViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = flag
        })

        // New recipie gomb
        binding.fltBtnNewRecipie.setOnClickListener {
            findNavController().navigate(R.id.action_nav_flag2_to_nav_new_recipie)
        }

        // Delete gomb - elozo fragmentre lep at
        binding.btnDeleteFlag.setOnClickListener{
            deleteFlag(flag, inflater)
            findNavController().navigate(R.id.action_nav_flag_to_nav_recipies)
        }

        initRecipieListener(flag)

        return root
    }

    fun deleteFlag(flag: String, inflater: LayoutInflater){
        // Todo I: Eloszor torolni a receptek flag-jet ha van bennuk ilyen flag - szerintem mukodik!!
        // Todo II: Flag torlese
        // Update recipies
        db.collection("recipies").whereArrayContains("flags", flag).
        addSnapshotListener { snapshots, e ->
            if(e != null){
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
            if(snapshots != null && snapshots.documents.isNotEmpty()){
                for(doc in snapshots.documents){
                    // Delete extra flag
                    var tmp = doc.data?.get("flags").toString()
                    tmp = tmp.drop(1)
                    tmp = tmp.dropLast(1)
                    var tmp_flags = tmp.split(", ")
                    tmp_flags = tmp_flags.minus(flag)

                    // Update recipie - delete the extra flag from recipie's flags
                    db.collection("recipies").document(doc.id).update("flags", tmp_flags)
                    Toast.makeText(inflater.context, "Recipie updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Delete Flag
        db.collection("flags").whereEqualTo("name", flag).
        addSnapshotListener { snapshot, e ->
            if(e != null){
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
            if(snapshot != null){
                for(doc in snapshot.documents){
                    db.collection("flags").document(doc.id).delete()
                    Toast.makeText(inflater.context, "Flag deleted", Toast.LENGTH_SHORT).show()
//                    return@addSnapshotListener
                }
            }
        }
    }

    fun initRecipieListener(flag: String) {
        db.collection("recipies").whereEqualTo("flag", flag).
        addSnapshotListener { snapshots, error ->
            if (error != null){
                Toast.makeText(this.context, error.toString(), Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            if(snapshots != null){
                if(snapshots.documents.isEmpty()){
                    for (dc in snapshots.documentChanges) {
                        when(dc.type) {
                            com.google.firebase.firestore.DocumentChange.Type.ADDED -> recipieitemAdapter.addRecipie(dc.document.toObject<Recipie>())
                            com.google.firebase.firestore.DocumentChange.Type.MODIFIED -> Toast.makeText(this.context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
                            com.google.firebase.firestore.DocumentChange.Type.REMOVED -> Toast.makeText(this.context, dc.document.data.toString(), Toast.LENGTH_SHORT).show()
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
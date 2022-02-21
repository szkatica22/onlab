package hu.bme.aut.android.onlab.ui.recipie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.onlab.data.Recipie
import hu.bme.aut.android.onlab.databinding.FragmentRecipieBinding

class RecipieFragment: Fragment(){
    private lateinit var recipieViewModel: RecipieViewModel
    private var _binding: FragmentRecipieBinding? = null

    private val binding get() = _binding!!
    private var db = Firebase.firestore
    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private lateinit var recipieController: RecipieController
    private var prep_title: String = "Preparation"

    private var other_users: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recipieViewModel =
            ViewModelProvider(this).get(RecipieViewModel::class.java)

        _binding = FragmentRecipieBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val rec_name = this.arguments?.get("recipiename").toString()

        // Save the other users into a list
        db.collection("recipies").whereNotEqualTo("author", firebaseUser?.email).
        get().addOnSuccessListener { snapshots ->
            if(snapshots.documents.isNotEmpty()){
                for(doc in snapshots.documents){
                    if(doc["author"] != firebaseUser?.email && !other_users.contains(doc["author"].
                        toString())){
                        other_users += doc["author"].toString()
                    }
                }
            }
            db.collection("recipies").whereEqualTo("name", rec_name).get().
            addOnSuccessListener { snapshot ->
                if(snapshot.documents.isNotEmpty()){
                    val tmp_data = snapshot.documents[0].data
                    val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
                        tmp_data.get("flags") as List<String?>?, tmp_data.get("imageUrls") as List<String?>?,
                        tmp_data.get("time").toString(), tmp_data.get("abundance").toString(),
                        tmp_data.get("author").toString(), tmp_data.get("ingredients") as Map<String?, String?>?,
                        tmp_data.get("steps") as List<String?>?, tmp_data.get("shares") as List<String?>?)

                    recipieController = RecipieController(this, tmp_rec, prep_title, other_users, inflater)
                    binding.ervRecipie.setController(recipieController)

                    recipieController.requestModelBuild()

                    binding.ervRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
                        RecyclerView.VERTICAL))
                }
            }
        }

//        db.collection("recipies").whereEqualTo("name", rec_name).get().
//        addOnSuccessListener { snapshot ->
//            if(snapshot.documents.isNotEmpty()){
//                val tmp_data = snapshot.documents[0].data
//                val tmp_rec = Recipie(rec_name, tmp_data?.get("favourite") as Boolean,
//                    tmp_data?.get("flags") as List<String?>?, tmp_data?.get("imageUrls") as List<String?>?,
//                    tmp_data?.get("time").toString(), tmp_data?.get("abundance").toString(),
//                    tmp_data?.get("author").toString(), tmp_data?.get("ingredients") as List<String?>?,
//                    tmp_data?.get("steps") as List<String?>?)
//
//                recipieController = RecipieController(this.context, tmp_rec, prep_title, other_users, inflater)
//                binding.ervRecipie.setController(recipieController)
//
//                recipieController.requestModelBuild()
//                binding.ervRecipie.addItemDecoration(DividerItemDecoration(requireActivity(),
//                    RecyclerView.VERTICAL))
//            }
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}